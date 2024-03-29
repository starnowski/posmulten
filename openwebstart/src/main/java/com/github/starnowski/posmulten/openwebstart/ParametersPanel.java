package com.github.starnowski.posmulten.openwebstart;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ParametersPanel extends JPanel {

    public static final String ADD_PARAMETER_BTN_NAME = "addParameterBtn";
    public static final String PARAMETER_KEY_TEXTAREA_NAME_PREFIX = "parameterKey";
    public static final String PARAMETER_VALUE_TEXTAREA_NAME_PREFIX = "parameterValue";
    public static final String PARAMETER_REMOVE_BTN_PREFIX = "parameterRemove";
    public static final String PARAMETERS_LABELS_PANEL_NAME = "ParametersLabelsPanel";
    private final JPanel addButtonPanel;
    private final JPanel parametersPanel;
    private final JPanel labelsPanel;
    private final Map<Integer, ParameterPanel> parameterPanelList = new HashMap<>();
    private int parameterSequence = 0;

    public ParametersPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        labelsPanel = prepareLabelsPanel();
        labelsPanel.setVisible(false);
        add(labelsPanel);
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
            labelsPanel.setVisible(true);
            parametersPanel.revalidate();
            parametersPanel.repaint();
        });
    }

    void removeParameterPanel(int parameterIndex) {
        parametersPanel.remove(parameterPanelList.get(parameterIndex));
        parameterPanelList.remove(parameterIndex);
        labelsPanel.setVisible(!parameterPanelList.isEmpty());
        parametersPanel.revalidate();
        parametersPanel.repaint();
    }

    public Map<String, String> getParameters() {
        return parameterPanelList.values().stream().collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    private JPanel prepareLabelsPanel()
    {
        JPanel panel = new JPanel();
        panel.setName(PARAMETERS_LABELS_PANEL_NAME);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel parameterKeyLabel = new JLabel("Parameter key");
        parameterKeyLabel.setName("ParameterKeyLabel");
        parameterKeyLabel.setAlignmentX(JButton.LEFT_ALIGNMENT);
        panel.add(parameterKeyLabel);
        panel.add(createSpacingPanel());
        JLabel parameterValueLabel = new JLabel("Parameter value");
        parameterValueLabel.setName("parameterValueLabel");
        parameterValueLabel.setAlignmentX(JButton.LEFT_ALIGNMENT);
        panel.add(parameterValueLabel);
        panel.add(createSpacingPanel());
        return panel;
    }

    private JPanel createSpacingPanel()
    {
        // Create an empty panel with a preferred size for spacing
        JPanel spacingPanel = new JPanel();
        spacingPanel.setPreferredSize(new Dimension(10, 10)); // Adjust the size as needed
        return spacingPanel;
    }

    private class ParameterPanel extends JPanel {

        private final JTextArea parameterKeyTextArea;
        private final JTextArea parameterValueTextArea;

        public ParameterPanel(int parameterIndex) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            parameterKeyTextArea = new JTextArea(1, 20);
            parameterKeyTextArea.setName(PARAMETER_KEY_TEXTAREA_NAME_PREFIX + parameterIndex);
            parameterKeyTextArea.setBorder(createTextAreaBorder());
            parameterKeyTextArea.setToolTipText("Parameter key");
            add(parameterKeyTextArea);
            add(createSpacingPanel());
            parameterValueTextArea = new JTextArea(1, 40);
            parameterValueTextArea.setName(PARAMETER_VALUE_TEXTAREA_NAME_PREFIX + parameterIndex);
            parameterValueTextArea.setBorder(createTextAreaBorder());
            parameterValueTextArea.setToolTipText("Parameter value");
            add(parameterValueTextArea);
            add(createSpacingPanel());
            JButton submitButton = new JButton("Remove");
            submitButton.setName(PARAMETER_REMOVE_BTN_PREFIX + parameterIndex);
            add(submitButton);
            submitButton.addActionListener(e -> ParametersPanel.this.removeParameterPanel(parameterIndex));
        }

        private Border  createTextAreaBorder()
        {
            return BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK), // Top and bottom border
                    BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding around the text area
            );
        }
        public String getKey() {
            return parameterKeyTextArea.getText();
        }

        public String getValue() {
            return parameterValueTextArea.getText();
        }
    }
}
