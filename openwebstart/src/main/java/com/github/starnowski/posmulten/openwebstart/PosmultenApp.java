package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class PosmultenApp extends JFrame {
    public static final String CREATION_SCRIPTS_TEXTFIELD_NAME = "creationScripts";
    public static final String DROP_SCRIPTS_TEXTFIELD_NAME = "dropScripts";
    public static final String CHECKING_SCRIPTS_TEXTFIELD_NAME = "checkingScripts";
    public static final String CONFIGURATION_TEXTFIELD_NAME = "configuration";
    private final YamlSharedSchemaContextFactory factory;
    private final JTextArea inputTextArea;
    private final JTextArea creationScriptsTextArea;
    private final JTextArea dropScriptsTextArea;
    private final JTextArea checkingScriptsTextArea;

    public PosmultenApp(YamlSharedSchemaContextFactory factory) {
        this.factory = factory;
//        setMiglayout(new LC().wrapAfter(1), new AC().align("center"), new AC());
        setMiglayout();
        setName("Posmulten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        inputTextArea = prepareScriptTextArea(CONFIGURATION_TEXTFIELD_NAME);
        creationScriptsTextArea = prepareScriptTextArea(CREATION_SCRIPTS_TEXTFIELD_NAME, false);
        dropScriptsTextArea = prepareScriptTextArea(DROP_SCRIPTS_TEXTFIELD_NAME, false);
        checkingScriptsTextArea = prepareScriptTextArea(CHECKING_SCRIPTS_TEXTFIELD_NAME, false);
        inputTextArea.setToolTipText("Configuration");

        JButton submitButton = new JButton("Submit");
        submitButton.setName("submitBtn");
        submitButton.addActionListener(e -> {
            System.out.println("actionPerformed : " + e.getActionCommand());
            String inputCode = inputTextArea.getText();
            try {
                ISharedSchemaContext context = factory.build(inputCode, DefaultDecoratorContext.builder().build());
                creationScriptsTextArea.setText(context.getSqlDefinitions().stream().map(definition -> definition.getCreateScript()).collect(Collectors.joining("\n")));
                creationScriptsTextArea.setVisible(true);
                LinkedList<SQLDefinition> stack = new LinkedList<>();
                context.getSqlDefinitions().forEach(stack::push);
                dropScriptsTextArea.setText(stack.stream().map(definition -> definition.getDropScript()).collect(Collectors.joining("\n")));
                dropScriptsTextArea.setVisible(true);
                checkingScriptsTextArea.setText(context.getSqlDefinitions().stream().filter(definition -> definition.getCheckingStatements() != null).flatMap(definition -> definition.getCheckingStatements().stream()).collect(Collectors.joining("\n")));
                checkingScriptsTextArea.setVisible(true);
            } catch (InvalidConfigurationException ex) {
                throw new RuntimeException(ex);
            } catch (SharedSchemaContextBuilderException ex) {
                throw new RuntimeException(ex);
            } catch (RuntimeException exception) {
                System.out.println("exception");
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(inputTextArea));
        panel.add(new JScrollPane(creationScriptsTextArea));
        panel.add(new JScrollPane(dropScriptsTextArea));
        panel.add(new JScrollPane(checkingScriptsTextArea));
        panel.add(submitButton);
        add(panel);

        pack();
    }

    public PosmultenApp() {
        this(new YamlSharedSchemaContextFactory());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PosmultenApp app = new PosmultenApp();
                app.setVisible(true);
            }
        });
    }

    private JTextArea prepareScriptTextArea(String name) {
        return prepareScriptTextArea(name, true);
    }

    private JTextArea prepareScriptTextArea(String name, boolean visibleByDefault) {
        JTextArea textArea = new JTextArea(10, 20);
        textArea.setName(name);
        textArea.setVisible(visibleByDefault);
        return textArea;
    }

    public void setMiglayout(LC layout, AC columns, AC rows) {
        setLayout(new MigLayout(layout, columns, rows));
    }

    public void setMiglayout() {
        setMiglayout(new LC(), new AC(), new AC());
    }
}