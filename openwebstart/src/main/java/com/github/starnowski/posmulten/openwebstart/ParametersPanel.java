package com.github.starnowski.posmulten.openwebstart;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ParametersPanel extends JPanel {

    public static final String ADD_PARAMETER_BTN_NAME = "addParameterBtn";
    public static final String PARAMETER_KEY_TEXTAREA_NAME_PREFIX = "parameterKey";
    public static final String PARAMETER_VALUE_TEXTAREA_NAME_PREFIX = "parameterValue";
    public static final String PARAMETER_REMOVE_BTN_PREFIX = "parameterRemove";
    private final JPanel addButtonPanel;
    private final JPanel parametersPanel;
    private final Map<Integer, ParameterPanel> parameterPanelList = new HashMap<>();
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
            int currentIndex = parameterSequence++;
            ParameterPanel parameterPanel = new ParameterPanel(currentIndex);
            parameterPanelList.put(currentIndex, parameterPanel);
            parametersPanel.add(parameterPanel);
            parametersPanel.revalidate();
            parametersPanel.repaint();
        });
    }

    void removeParameterPanel(int parameterIndex) {
        parametersPanel.remove(parameterPanelList.get(parameterIndex));
        parameterPanelList.remove(parameterIndex);
        parametersPanel.revalidate();
        parametersPanel.repaint();
    }

    public Map<String, String> getParameters() {
        return parameterPanelList.values().stream().collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    private class ParameterPanel extends JPanel {

        private final JTextArea parameterKeyTextArea;
        private final JTextArea parameterValueTextArea;

        public ParameterPanel(int parameterIndex) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            parameterKeyTextArea = new JTextArea(1, 40);
            parameterKeyTextArea.setName(PARAMETER_KEY_TEXTAREA_NAME_PREFIX + parameterIndex);
            add(parameterKeyTextArea);
            parameterValueTextArea = new JTextArea(1, 40);
            parameterValueTextArea.setName(PARAMETER_VALUE_TEXTAREA_NAME_PREFIX + parameterIndex);
            add(parameterValueTextArea);
            JButton submitButton = new JButton("Remove");
            submitButton.setName(PARAMETER_REMOVE_BTN_PREFIX + parameterIndex);
            add(submitButton);
            submitButton.addActionListener(e -> ParametersPanel.this.removeParameterPanel(parameterIndex));
        }

        public String getKey() {
            return parameterKeyTextArea.getText();
        }

        public String getValue() {
            return parameterValueTextArea.getText();
        }
    }
}
