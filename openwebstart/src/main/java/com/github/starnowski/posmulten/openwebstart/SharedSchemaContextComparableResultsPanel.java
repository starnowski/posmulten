package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.postgresql.core.context.comparable.SharedSchemaContextComparator;

import javax.swing.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class SharedSchemaContextComparableResultsPanel extends JTabbedPane {

    public static final String CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME = "creationScriptsDifferencesExistedOnlyOnLeftTextArea";
    public static final String CREATION_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME = "creationScriptsDifferencesExistedOnlyOnRightTextArea";
    public static final String DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME = "dropScriptsDifferencesExistedOnlyOnLeftTextArea";
    public static final String DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME = "dropScriptsDifferencesExistedOnlyOnRightTextArea";
    public static final String CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME = "dropScriptsDifferencesExistedOnlyOnLeftTextArea";
    public static final String CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME = "dropScriptsDifferencesExistedOnlyOnRightTextArea";
    public static final String CREATION_SCRIPTS_TAB_NAME = "creationScriptsTab";
    public static final String DROP_SCRIPTS_TAB_NAME = "dropScriptsTab";
    public static final String DROP_SCRIPTS_TAB_TITLE = "Drop Scripts";
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
        dropScriptsTabbedPanel.setName(DROP_SCRIPTS_TAB_NAME);
        dropScriptsDifferencesExistedOnlyOnLeftTextArea = prepareScriptTextArea(DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME);
        dropScriptsTabbedPanel.addTab("Exist only in previous", new JScrollPane(dropScriptsDifferencesExistedOnlyOnLeftTextArea));

        dropScriptsDifferencesExistedOnlyOnRightTextArea = prepareScriptTextArea(DROP_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME);
        dropScriptsTabbedPanel.addTab("Exist only in new", new JScrollPane(dropScriptsDifferencesExistedOnlyOnRightTextArea));
        addTab(DROP_SCRIPTS_TAB_TITLE, dropScriptsTabbedPanel);

        JTabbedPane checkingScriptsTabbedPanel = new JTabbedPane();
        checkingScriptsDifferencesExistedOnlyOnLeftTextArea = prepareScriptTextArea(CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_LEFT_TEXT_AREA_NAME);
        checkingScriptsTabbedPanel.addTab("Exist only in previous", new JScrollPane(checkingScriptsDifferencesExistedOnlyOnLeftTextArea));

        checkingScriptsDifferencesExistedOnlyOnRightTextArea = prepareScriptTextArea(CHECKING_SCRIPTS_DIFFERENCES_EXISTED_ONLY_ON_RIGHT_TEXT_AREA_NAME);
        checkingScriptsTabbedPanel.addTab("Exist only in new", new JScrollPane(checkingScriptsDifferencesExistedOnlyOnRightTextArea));
        addTab("Checking Scripts", checkingScriptsTabbedPanel);
    }

    public void displayDiff(SharedSchemaContextComparator.SharedSchemaContextComparableResults sharedSchemaContextComparableResults) {
        SharedSchemaContextComparator.ComparableResult dummyResults = new SharedSchemaContextComparator.ComparableResult(null, null);
        creationScriptsDifferencesExistedOnlyOnLeftTextArea.setText(ofNullable(sharedSchemaContextComparableResults.getCreationScriptsDifferences()).orElse(dummyResults).getExistedOnlyOnLeft().stream().collect(Collectors.joining("\n")));
        creationScriptsDifferencesExistedOnlyOnRightTextArea.setText(ofNullable(sharedSchemaContextComparableResults.getCreationScriptsDifferences()).orElse(dummyResults).getExistedOnlyOnRight().stream().collect(Collectors.joining("\n")));
        dropScriptsDifferencesExistedOnlyOnLeftTextArea.setText(ofNullable(sharedSchemaContextComparableResults.getDropScriptsDifferences()).orElse(dummyResults).getExistedOnlyOnLeft().stream().collect(Collectors.joining("\n")));
        dropScriptsDifferencesExistedOnlyOnRightTextArea.setText(ofNullable(sharedSchemaContextComparableResults.getDropScriptsDifferences()).orElse(dummyResults).getExistedOnlyOnRight().stream().collect(Collectors.joining("\n")));
        //TODO
    }

    private JTextArea prepareScriptTextArea(String name) {
        JTextArea textArea = new JTextArea(10, 300);
        textArea.setName(name);
        textArea.setVisible(true);
        return textArea;
    }

}
