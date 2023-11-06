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
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class PosmultenApp extends JFrame {
    public static final String CREATION_SCRIPTS_TEXTFIELD_NAME = "creationScripts";
    public static final String DROP_SCRIPTS_TEXTFIELD_NAME = "dropScripts";
    public static final String CHECKING_SCRIPTS_TEXTFIELD_NAME = "checkingScripts";
    public static final String CONFIGURATION_TEXTFIELD_NAME = "configuration";
    public static final String ERROR_TEXTFIELD_NAME = "errors";
    public static final String ERROR_PANEL_NAME = "errors-panel";
    public static final String SCRIPTS_PANEL_NAME = "scriptsPanel";
    private final YamlSharedSchemaContextFactory factory;
    private final JTextArea inputTextArea;
    private final JTextArea creationScriptsTextArea;
    private final JTextArea dropScriptsTextArea;
    private final JTextArea checkingScriptsTextArea;
    private final JTextArea errorTextArea;
    private final JPanel scriptsPanel;
    private final JPanel errorPanel;
    private final ParametersPanel parametersPanel;

    public PosmultenApp(YamlSharedSchemaContextFactory factory) {
        this.factory = factory;
        setMiglayout();
        setName("Posmulten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);

        inputTextArea = prepareScriptTextArea(CONFIGURATION_TEXTFIELD_NAME);
        creationScriptsTextArea = prepareScriptTextArea(CREATION_SCRIPTS_TEXTFIELD_NAME);
        dropScriptsTextArea = prepareScriptTextArea(DROP_SCRIPTS_TEXTFIELD_NAME);
        checkingScriptsTextArea = prepareScriptTextArea(CHECKING_SCRIPTS_TEXTFIELD_NAME);
        errorTextArea = prepareScriptTextArea(ERROR_TEXTFIELD_NAME);
        inputTextArea.setToolTipText("Configuration");

        JButton submitButton = new JButton("Submit");
        submitButton.setName("submitBtn");
        parametersPanel = new ParametersPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createCenteredLabel("Yaml configuration"));
        panel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);
        submitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(submitButton);
        panel.add(parametersPanel);
        scriptsPanel = prepareScriptsPanel();
        errorPanel = prepareErrorPanel();
        panel.add(scriptsPanel);
        panel.add(errorPanel);
        add(panel);

        scriptsPanel.setVisible(false);
        errorPanel.setVisible(false);

        pack();

        submitButton.addActionListener(prepareActionListenerForSubmitButton());
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

    private ActionListener prepareActionListenerForSubmitButton() {
        return e -> {
            scriptsPanel.setVisible(false);
            errorPanel.setVisible(false);
            String inputCode = inputTextArea.getText();
            try {
                ISharedSchemaContext context = factory.build(inputCode, DefaultDecoratorContext.builder().build());
                creationScriptsTextArea.setText(context.getSqlDefinitions().stream().map(definition -> definition.getCreateScript()).collect(joining("\n")));
                LinkedList<SQLDefinition> stack = new LinkedList<>();
                context.getSqlDefinitions().forEach(stack::push);
                dropScriptsTextArea.setText(stack.stream().map(definition -> definition.getDropScript()).collect(joining("\n")));
                checkingScriptsTextArea.setText(context.getSqlDefinitions().stream().filter(definition -> definition.getCheckingStatements() != null).flatMap(definition -> definition.getCheckingStatements().stream()).collect(joining("\n")));
                scriptsPanel.setVisible(true);
            } catch (InvalidConfigurationException ex) {
                errorTextArea.setText(ex.getErrorMessages().stream().collect(joining("\n")));
                errorPanel.setVisible(true);
            } catch (SharedSchemaContextBuilderException ex) {
                errorTextArea.setText(ex.getMessage());
                errorPanel.setVisible(true);
            } catch (RuntimeException ex) {
                errorTextArea.setText(ex.getMessage());
                errorPanel.setVisible(true);
            }
        };
    }

    private JPanel prepareScriptsPanel() {
        JPanel panel = new JPanel();
        panel.setName(SCRIPTS_PANEL_NAME);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createCenteredLabel("Creation scripts"));
        panel.add(new JScrollPane(creationScriptsTextArea));
        panel.add(createCenteredLabel("Drop scripts"));
        panel.add(new JScrollPane(dropScriptsTextArea));
        panel.add(createCenteredLabel("Checking statements scripts"));
        panel.add(new JScrollPane(checkingScriptsTextArea));
        return panel;
    }

    private JPanel prepareErrorPanel() {
        JPanel panel = new JPanel();
        panel.setName(ERROR_PANEL_NAME);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createCenteredLabel("Error during configuration processing!"));
        panel.add(new JScrollPane(errorTextArea));
        return panel;
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

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(JButton.CENTER_ALIGNMENT);
        return label;
    }

    public void setMiglayout(LC layout, AC columns, AC rows) {
        setLayout(new MigLayout(layout, columns, rows));
    }

    public void setMiglayout() {
        setMiglayout(new LC(), new AC(), new AC());
    }
}