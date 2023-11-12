package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.comparable.DefaultSharedSchemaContextComparator;
import com.github.starnowski.posmulten.postgresql.core.context.comparable.SharedSchemaContextComparator;
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
import static java.util.stream.Collectors.toList;

public class PosmultenApp extends JFrame {
    public static final String CREATION_SCRIPTS_TEXTFIELD_NAME = "creationScripts";
    public static final String DROP_SCRIPTS_TEXTFIELD_NAME = "dropScripts";
    public static final String CHECKING_SCRIPTS_TEXTFIELD_NAME = "checkingScripts";
    public static final String CONFIGURATION_TEXTFIELD_NAME = "configuration";
    public static final String PREVIOUS_CONFIGURATION_TEXTFIELD_NAME = "previousConfiguration";
    public static final String ERROR_TEXTFIELD_NAME = "errors";
    public static final String ERROR_PANEL_NAME = "errors-panel";
    public static final String SCRIPTS_PANEL_NAME = "scriptsPanel";
    public static final String DISPLAY_PARAMETERS_CHECK_BOX_NAME = "displayParametersCheckBox";
    public static final String DIFF_CONFIGURATIONS_CHECK_BOX_NAME = "diffConfigurationsCheckBox";
    public static final String DIFF_PANEL_NAME = "diffPanel";
    public static final String MAIN_TAB_PANEL_NAME = "mainTabPanel";
    private final YamlSharedSchemaContextFactory factory;
    private final JTextArea inputTextArea;
    private final JTextArea previousConfigurationInputTextArea;
    private final ScriptPanel creationScriptsTextArea;
    private final ScriptPanel dropScriptsTextArea;
    private final JTextArea checkingScriptsTextArea;
    private final JTextArea errorTextArea;
    private final JPanel scriptsPanel;
    private final JPanel errorPanel;
    private final ParametersPanel parametersPanel;
    private final JCheckBox displayParametersCheckBox;
    private final JCheckBox diffConfigurationsCheckBox;
    private final SharedSchemaContextComparableResultsPanel sharedSchemaContextComparableResultsPanel;
    private final SharedSchemaContextComparator sharedSchemaContextComparator;

    public PosmultenApp() {
        this(new YamlSharedSchemaContextFactory());
    }

    public PosmultenApp(YamlSharedSchemaContextFactory factory) {
        this(factory, new DefaultSharedSchemaContextComparator());
    }

    public PosmultenApp(YamlSharedSchemaContextFactory factory, SharedSchemaContextComparator sharedSchemaContextComparator) {
        this.factory = factory;
        this.sharedSchemaContextComparator = sharedSchemaContextComparator;
        setMiglayout();
        setName("Posmulten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Get the default toolkit
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Get the screen size
        Dimension screenSize = toolkit.getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        System.out.println("Screen size, width: " + screenSize.getWidth() + " height: " + screenSize.getHeight());
        //https://stackoverflow.com/questions/11570356/jframe-in-full-screen-java
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        inputTextArea = prepareScriptTextArea(CONFIGURATION_TEXTFIELD_NAME);
        previousConfigurationInputTextArea = prepareScriptTextArea(PREVIOUS_CONFIGURATION_TEXTFIELD_NAME);
        creationScriptsTextArea = new ScriptPanel(CREATION_SCRIPTS_TEXTFIELD_NAME);
        dropScriptsTextArea = new ScriptPanel(DROP_SCRIPTS_TEXTFIELD_NAME);
        checkingScriptsTextArea = prepareScriptTextArea(CHECKING_SCRIPTS_TEXTFIELD_NAME);
        errorTextArea = prepareScriptTextArea(ERROR_TEXTFIELD_NAME);
        inputTextArea.setToolTipText("Configuration");
        previousConfigurationInputTextArea.setToolTipText("Previous configuration to compare with");

        JButton submitButton = new JButton("Submit");
        submitButton.setName("submitBtn");
        parametersPanel = new ParametersPanel();
        displayParametersCheckBox = new JCheckBox("Use template parameters", null, false);
        displayParametersCheckBox.setName(DISPLAY_PARAMETERS_CHECK_BOX_NAME);
        diffConfigurationsCheckBox = new JCheckBox("Compare configurations", null, false);
        diffConfigurationsCheckBox.setName(DIFF_CONFIGURATIONS_CHECK_BOX_NAME);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));

        // Create a JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setName(MAIN_TAB_PANEL_NAME);
        tabbedPane.addTab("Yaml configuration", new JScrollPane(inputTextArea));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(tabbedPane, BorderLayout.CENTER);
        submitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

        optionsPanel.add(diffConfigurationsCheckBox);
        optionsPanel.add(displayParametersCheckBox);
        optionsPanel.add(submitButton);
        panel.add(optionsPanel);

        panel.add(parametersPanel);
        scriptsPanel = prepareScriptsPanel();
        errorPanel = prepareErrorPanel();
        sharedSchemaContextComparableResultsPanel = new SharedSchemaContextComparableResultsPanel();
        sharedSchemaContextComparableResultsPanel.setName(DIFF_PANEL_NAME);
        sharedSchemaContextComparableResultsPanel.setVisible(false);

        panel.add(scriptsPanel);
        panel.add(errorPanel);
        panel.add(sharedSchemaContextComparableResultsPanel);
        add(panel);

        scriptsPanel.setVisible(false);
        errorPanel.setVisible(false);
        parametersPanel.setVisible(false);

        submitButton.addActionListener(prepareActionListenerForSubmitButton());
        displayParametersCheckBox.addChangeListener(c -> {
            parametersPanel.setVisible(displayParametersCheckBox.isSelected());
        });
        diffConfigurationsCheckBox.addChangeListener(c -> {
            if (diffConfigurationsCheckBox.isSelected()) {
                if (tabbedPane.getTabCount() == 1) {
                    tabbedPane.insertTab("Previous yaml configuration to compare with", null, new JScrollPane(previousConfigurationInputTextArea), null, 1);
                }
            } else {
                if (tabbedPane.getTabCount() == 2) {
                    tabbedPane.removeTabAt(1);
                }
            }
        });
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PosmultenApp app = new PosmultenApp();
//                app.pack();
                app.setVisible(true);
            }
        });
    }

    private ActionListener prepareActionListenerForSubmitButton() {
        return e -> {
            scriptsPanel.setVisible(false);
            errorPanel.setVisible(false);
            sharedSchemaContextComparableResultsPanel.setVisible(false);
            if (diffConfigurationsCheckBox.isSelected()) {
                //TODO Handle exceptions
                ISharedSchemaContext mainContext = null;
                try {
                    mainContext = factory.build(inputTextArea.getText(), prepareDefaultDecoratorContext());
                } catch (InvalidConfigurationException ex) {
                    throw new RuntimeException(ex);
                } catch (SharedSchemaContextBuilderException ex) {
                    throw new RuntimeException(ex);
                } catch (RuntimeException ex) {
                    throw new RuntimeException(ex);
                }
                ISharedSchemaContext previousContext = null;
                try {
                    previousContext = factory.build(previousConfigurationInputTextArea.getText(), prepareDefaultDecoratorContext());
                } catch (InvalidConfigurationException ex) {
                    throw new RuntimeException(ex);
                } catch (SharedSchemaContextBuilderException ex) {
                    throw new RuntimeException(ex);
                } catch (RuntimeException ex) {
                    throw new RuntimeException(ex);
                }
                sharedSchemaContextComparableResultsPanel.displayDiff(sharedSchemaContextComparator.diff(previousContext, mainContext));
                sharedSchemaContextComparableResultsPanel.setVisible(true);
            } else {
                try {
                    ISharedSchemaContext context = factory.build(inputTextArea.getText(), prepareDefaultDecoratorContext());
                    creationScriptsTextArea.display(context.getSqlDefinitions().stream().map(definition -> definition.getCreateScript()).collect(toList()));
                    dropScriptsTextArea.display(context.getSqlDefinitions().stream().map(definition -> definition.getDropScript()).collect(toList()), true);
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
            }
        };
    }

    private DefaultDecoratorContext prepareDefaultDecoratorContext() {
        return displayParametersCheckBox.isSelected() ? DefaultDecoratorContext.builder().withReplaceCharactersMap(parametersPanel.getParameters()).build() : DefaultDecoratorContext.builder().build();
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