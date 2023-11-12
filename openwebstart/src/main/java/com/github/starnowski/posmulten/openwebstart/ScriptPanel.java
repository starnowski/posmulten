package com.github.starnowski.posmulten.openwebstart;

import javax.swing.*;
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
        if (scriptLines == null) {
            scriptsTextArea.setText("");
        } else {
            if (displayWithReverseOrder) {
                Stack<String> stack = new Stack<>();
                scriptLines.forEach(script -> stack.push(script));
                scriptsTextArea.setText(stack.stream().collect(joining("\n")));
            } else {
                scriptsTextArea.setText(scriptLines.stream().collect(joining("\n")));
            }
        }
    }

    private JTextArea prepareScriptTextArea(String name, boolean visibleByDefault) {
        JTextArea textArea = new JTextArea(10, 300);
        textArea.setName(name);
        textArea.setVisible(visibleByDefault);
        return textArea;
    }
}
