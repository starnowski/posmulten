package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.postgresql.core.context.comparable.SharedSchemaContextComparator;

import javax.swing.*;
import java.util.stream.Collectors;

public class SharedSchemaContextComparableResultsPanel extends JTabbedPane {

    public static final String CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME = "creationScriptsDifferencesExistedOnlyOnLeftTextArea";
    public static final String CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME = "creationScriptsDifferencesExistedOnlyOnRightTextArea";
    public static final String DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME = "dropScriptsDifferencesExistedOnlyOnLeftTextArea";
    public static final String DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME = "dropScriptsDifferencesExistedOnlyOnRightTextArea";
    public static final String CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME = "dropScriptsDifferencesExistedOnlyOnLeftTextArea";
    public static final String CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME = "dropScriptsDifferencesExistedOnlyOnRightTextArea";
    public static final String CREATION_SCRIPTS_TAB_NAME = "creationScriptsTab";
    private final JTextArea creationScriptsDifferencesExistedOnlyOnLeftTextArea;
    private final JTextArea creationScriptsDifferencesExistedOnlyOnRightTextArea;
    private final JTextArea dropScriptsDifferencesExistedOnlyOnLeftTextArea;
    private final JTextArea dropScriptsDifferencesExistedOnlyOnRightTextArea;
    private final JTextArea checkingScriptsDifferencesExistedOnlyOnLeftTextArea;
    private final JTextArea checkingScriptsDifferencesExistedOnlyOnRightTextArea;

    public SharedSchemaContextComparableResultsPanel() {
        JTabbedPane creationScriptsTabbedPanel = new JTabbedPane();
        creationScriptsTabbedPanel.setName(CREATION_SCRIPTS_TAB_NAME);
        creationScriptsDifferencesExistedOnlyOnLeftTextArea = prepareScriptTextArea(CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME);
        creationScriptsTabbedPanel.addTab("Exist only in previous", new JScrollPane(creationScriptsDifferencesExistedOnlyOnLeftTextArea));

        creationScriptsDifferencesExistedOnlyOnRightTextArea = prepareScriptTextArea(CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME);
        creationScriptsTabbedPanel.addTab("Exist only in new", new JScrollPane(creationScriptsDifferencesExistedOnlyOnRightTextArea));
        addTab("Creation Scripts", creationScriptsTabbedPanel);

        JTabbedPane dropScriptsTabbedPanel = new JTabbedPane();
        dropScriptsDifferencesExistedOnlyOnLeftTextArea = prepareScriptTextArea(DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME);
        dropScriptsTabbedPanel.addTab("Exist only in previous", new JScrollPane(dropScriptsDifferencesExistedOnlyOnLeftTextArea));

        dropScriptsDifferencesExistedOnlyOnRightTextArea = prepareScriptTextArea(DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME);
        dropScriptsTabbedPanel.addTab("Exist only in new", new JScrollPane(dropScriptsDifferencesExistedOnlyOnRightTextArea));
        addTab("Drop Scripts", dropScriptsTabbedPanel);

        JTabbedPane checkingScriptsTabbedPanel = new JTabbedPane();
        checkingScriptsDifferencesExistedOnlyOnLeftTextArea = prepareScriptTextArea(CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME);
        checkingScriptsTabbedPanel.addTab("Exist only in previous", new JScrollPane(checkingScriptsDifferencesExistedOnlyOnLeftTextArea));

        checkingScriptsDifferencesExistedOnlyOnRightTextArea = prepareScriptTextArea(CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME);
        checkingScriptsTabbedPanel.addTab("Exist only in new", new JScrollPane(checkingScriptsDifferencesExistedOnlyOnRightTextArea));
        addTab("Checking Scripts", checkingScriptsTabbedPanel);
    }

    public void displayDiff(SharedSchemaContextComparator.SharedSchemaContextComparableResults sharedSchemaContextComparableResults) {
        creationScriptsDifferencesExistedOnlyOnLeftTextArea.setText(sharedSchemaContextComparableResults.getCreationScriptsDifferences().getExistedOnlyOnLeft().stream().collect(Collectors.joining("\n")));
        creationScriptsDifferencesExistedOnlyOnRightTextArea.setText(sharedSchemaContextComparableResults.getCreationScriptsDifferences().getExistedOnlyOnRight().stream().collect(Collectors.joining("\n")));
        //TODO
    }

    private JTextArea prepareScriptTextArea(String name) {
        JTextArea textArea = new JTextArea(10, 300);
        textArea.setName(name);
        textArea.setVisible(true);
        return textArea;
    }

}
