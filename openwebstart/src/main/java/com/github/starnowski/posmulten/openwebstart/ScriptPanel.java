package com.github.starnowski.posmulten.openwebstart;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static java.util.stream.Collectors.joining;

public class ScriptPanel extends JPanel {

    private final JTextArea scriptsTextArea;
    private final JCheckBox reverseDisplayOrderCheckBox;
    private List<String> scriptLines;

    public ScriptPanel(String scriptsTextAreaName) {
        scriptsTextArea = prepareScriptTextArea(scriptsTextAreaName, true);
        reverseDisplayOrderCheckBox = new JCheckBox("Display scripts in reverse order", null, false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(reverseDisplayOrderCheckBox);
        add(new JScrollPane(scriptsTextArea));
        reverseDisplayOrderCheckBox.addChangeListener(e -> display(reverseDisplayOrderCheckBox.isSelected()));
    }

    public void display(List<String> scriptLines) {
        this.scriptLines = scriptLines;
        display(reverseDisplayOrderCheckBox.isSelected());
    }

    public void display(List<String> scriptLines, boolean displayWithReverseOrder) {
        reverseDisplayOrderCheckBox.setSelected(displayWithReverseOrder);
        display(scriptLines);
    }

    private void display(boolean displayWithReverseOrder) {
        String scripts;
        if (scriptLines == null) {
            scripts = "";
        } else {
            if (displayWithReverseOrder) {
                LinkedList<String> stack = new LinkedList<>();
                scriptLines.forEach(stack::push);
                scripts = stack.stream().collect(joining("\n"));
            } else {
                scripts = scriptLines.stream().collect(joining("\n"));
            }
        }
        scriptsTextArea.setText(scripts);
    }

    private JTextArea prepareScriptTextArea(String name, boolean visibleByDefault) {
        JTextArea textArea = new JTextArea(10, 300);
        textArea.setName(name);
        textArea.setVisible(visibleByDefault);
        textArea.setEditable(false);
        return textArea;
    }
}
