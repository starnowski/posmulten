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
import java.awt.*;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class PosmultenApp extends JFrame {
    public static final String CREATION_SCRIPTS_TEXTFIELD_NAME = "creationScripts";
    public static final String DROP_SCRIPTS_TEXTFIELD_NAME = "dropScripts";
    public static final String CHECKING_SCRIPTS_TEXTFIELD_NAME = "checkingScripts";
    public static final String CONFIGURATION_TEXTFIELD_NAME = "configuration";
    public static final String SCRIPTS_PANEL_NAME = "scriptsPanel";
    private final YamlSharedSchemaContextFactory factory;
    private final JTextArea inputTextArea;
    private final JTextArea creationScriptsTextArea;
    private final JTextArea dropScriptsTextArea;
    private final JTextArea checkingScriptsTextArea;
    private final JPanel scriptsPanel;

    public PosmultenApp(YamlSharedSchemaContextFactory factory) {
        this.factory = factory;
//        setMiglayout(new LC().wrapAfter(1), new AC().align("center"), new AC());
        setMiglayout();
        setName("Posmulten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);

        inputTextArea = prepareScriptTextArea(CONFIGURATION_TEXTFIELD_NAME);
        creationScriptsTextArea = prepareScriptTextArea(CREATION_SCRIPTS_TEXTFIELD_NAME);
        dropScriptsTextArea = prepareScriptTextArea(DROP_SCRIPTS_TEXTFIELD_NAME);
        checkingScriptsTextArea = prepareScriptTextArea(CHECKING_SCRIPTS_TEXTFIELD_NAME);
        inputTextArea.setToolTipText("Configuration");

        JButton submitButton = new JButton("Submit");
        submitButton.setName("submitBtn");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        scriptsPanel = new JPanel();
        scriptsPanel.setName(SCRIPTS_PANEL_NAME);
        scriptsPanel.setLayout(new BoxLayout(scriptsPanel, BoxLayout.Y_AXIS));
        scriptsPanel.add(new JScrollPane(creationScriptsTextArea));
        scriptsPanel.add(new JScrollPane(dropScriptsTextArea));
        scriptsPanel.add(new JScrollPane(checkingScriptsTextArea));
        panel.add(scriptsPanel);
        submitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(submitButton, BorderLayout.CENTER);
        add(panel);

        scriptsPanel.setVisible(false);

        pack();


        submitButton.addActionListener(e -> {
            System.out.println("actionPerformed : " + e.getActionCommand());
            String inputCode = inputTextArea.getText();
            try {
                ISharedSchemaContext context = factory.build(inputCode, DefaultDecoratorContext.builder().build());
                creationScriptsTextArea.setText(context.getSqlDefinitions().stream().map(definition -> definition.getCreateScript()).collect(Collectors.joining("\n")));
                LinkedList<SQLDefinition> stack = new LinkedList<>();
                context.getSqlDefinitions().forEach(stack::push);
                dropScriptsTextArea.setText(stack.stream().map(definition -> definition.getDropScript()).collect(Collectors.joining("\n")));
                checkingScriptsTextArea.setText(context.getSqlDefinitions().stream().filter(definition -> definition.getCheckingStatements() != null).flatMap(definition -> definition.getCheckingStatements().stream()).collect(Collectors.joining("\n")));
                scriptsPanel.setVisible(true);
            } catch (InvalidConfigurationException ex) {
                throw new RuntimeException(ex);
            } catch (SharedSchemaContextBuilderException ex) {
                throw new RuntimeException(ex);
            } catch (RuntimeException exception) {
                System.out.println("exception");
            }
        });
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
        JTextArea textArea = new JTextArea(10, 300);
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