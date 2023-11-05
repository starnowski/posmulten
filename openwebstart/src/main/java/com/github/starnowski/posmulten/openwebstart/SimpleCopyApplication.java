package com.github.starnowski.posmulten.openwebstart;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import java.lang.reflect.InvocationTargetException;

import static javax.swing.SwingUtilities.invokeAndWait;

public class SimpleCopyApplication extends JFrame {
    private static final long serialVersionUID = 1L;

    public SimpleCopyApplication() {
//        setMiglayout(new LC().wrapAfter(1), new AC().align("center"), new AC());
        setMiglayout();

        final JTextField textField = new JTextField();
        textField.setName("textToCopy");
        JButton button = new JButton("Copy text to label");
        button.setName("copyButton");
        final JLabel label = new JLabel();
        label.setName("copiedText");

        button.addActionListener(e -> {
            label.setText(textField.getText());
        });

        add(textField);
        add(button);
        add(label);

        pack();
    }

    public void setMiglayout(LC layout, AC columns, AC rows) {
        setLayout(new MigLayout(layout, columns, rows));
    }

    public void setMiglayout() {
        setMiglayout(new LC(), new AC(), new AC());
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException, InvocationTargetException {
        invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new SimpleCopyApplication();
                frame.setVisible(true);
            }
        });
    }
}