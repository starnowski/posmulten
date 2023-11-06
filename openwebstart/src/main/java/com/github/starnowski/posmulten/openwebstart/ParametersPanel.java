package com.github.starnowski.posmulten.openwebstart;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ParametersPanel extends JPanel {

    public static final String ADD_PARAMETER_BTN_NAME = "addParameterBtn";
    private final JPanel addButtonPanel;
    private final JPanel parametersPanel;
    private Map<Integer, ParameterPanel> parameterPanelList = new HashMap<>();
    private int parameterSequence = 0;

    public ParametersPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        parametersPanel = new JPanel();
        parametersPanel.setName("parametersPanel");
        parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));
        add(parametersPanel);
        addButtonPanel = new JPanel();
        JButton submitButton = new JButton("Add parameter");
        submitButton.setName(ADD_PARAMETER_BTN_NAME);
        addButtonPanel.add(submitButton);
        add(addButtonPanel);

        submitButton.addActionListener(e -> {
            System.out.println("Add new parameter");
            int currentIndex = parameterSequence++;
            ParameterPanel parameterPanel = new ParameterPanel(currentIndex);
            parameterPanelList.put(currentIndex, parameterPanel);
            parametersPanel.add(parameterPanel);
            parametersPanel.revalidate();
            parametersPanel.repaint();
        });
    }

    void remoteParameterPanel(int parameterIndex)
    {
        System.out.println("remoteParameterPanel " + parameterIndex);
        parametersPanel.remove(parameterPanelList.get(parameterIndex));
        parameterPanelList.remove(parameterIndex);
        parametersPanel.revalidate();
        parametersPanel.repaint();
        System.out.println("END remoteParameterPanel " + parameterIndex);
    }

    private class ParameterPanel extends JPanel {

        private final JTextArea parameterKeyTextArea;
        private final JTextArea parameterValueTextArea;

        public ParameterPanel(int parameterIndex) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            parameterKeyTextArea = new JTextArea(1, 40);
            parameterKeyTextArea.setName("parameterKey" + parameterIndex);
            add(parameterKeyTextArea);
            parameterValueTextArea = new JTextArea(1, 40);
            parameterValueTextArea.setName("parameterValue" + parameterIndex);
            add(parameterValueTextArea);
            JButton submitButton = new JButton("Remove");
            submitButton.setName("parameterRemove" + parameterIndex);
            add(submitButton);
            submitButton.addActionListener(e -> ParametersPanel.this.remoteParameterPanel(parameterIndex));
        }

        public String getKey()
        {
            return parameterKeyTextArea.getText();
        }

        public String getValue()
        {
            return parameterValueTextArea.getText();
        }
    }
}
