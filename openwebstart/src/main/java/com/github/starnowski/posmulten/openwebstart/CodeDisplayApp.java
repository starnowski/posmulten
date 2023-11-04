package com.github.starnowski.posmulten.openwebstart;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CodeDisplayApp {
    public CodeDisplayApp(YamlSharedSchemaContextFactory factory) {
        this.factory = factory;
        JFrame frame = new JFrame("Code Display App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        inputTextArea = new JTextArea(10, 40);
        outputTextArea1 = new JTextArea(10, 40);
        outputTextArea2 = new JTextArea(10, 40);
        outputTextArea3 = new JTextArea(10, 40);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputCode = inputTextArea.getText();
                outputTextArea1.setText(inputCode);
                outputTextArea2.setText(inputCode);
                outputTextArea3.setText(inputCode);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(inputTextArea));
        panel.add(new JScrollPane(outputTextArea1));
        panel.add(new JScrollPane(outputTextArea2));
        panel.add(new JScrollPane(outputTextArea3));
        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private final YamlSharedSchemaContextFactory factory;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea1;
    private JTextArea outputTextArea2;
    private JTextArea outputTextArea3;

    public CodeDisplayApp() {
        this(new YamlSharedSchemaContextFactory());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CodeDisplayApp();
            }
        });
    }
}