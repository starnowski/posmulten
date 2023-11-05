package com.github.starnowski.posmulten.openwebstart;

import com.github.starnowski.posmulten.configuration.core.exceptions.InvalidConfigurationException;
import com.github.starnowski.posmulten.postgresql.core.common.SQLDefinition;
import com.github.starnowski.posmulten.postgresql.core.context.ISharedSchemaContext;
import com.github.starnowski.posmulten.postgresql.core.context.decorator.DefaultDecoratorContext;
import com.github.starnowski.posmulten.postgresql.core.context.exceptions.SharedSchemaContextBuilderException;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class PosmultenApp extends JFrame {
    private final YamlSharedSchemaContextFactory factory;
    private final JTextArea inputTextArea;
    private final JTextArea outputTextArea1;
    private final JTextArea outputTextArea2;
    private final JTextArea outputTextArea3;

    public PosmultenApp(YamlSharedSchemaContextFactory factory) {
        this.factory = factory;
//        setMiglayout(new LC().wrapAfter(1), new AC().align("center"), new AC());
        setMiglayout();
        setName("Posmulten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        inputTextArea = new JTextArea(10, 20);
        outputTextArea1 = new JTextArea(10, 20);
        outputTextArea2 = new JTextArea(10, 20);
        outputTextArea3 = new JTextArea(10, 20);
        inputTextArea.setName("configuration");
        inputTextArea.setToolTipText("Configuration");
        outputTextArea1.setName("creationScripts");
        outputTextArea2.setName("dropScripts");
        outputTextArea3.setName("checkingScripts");

        JButton submitButton = new JButton("Submit");
        submitButton.setName("submitBtn");
        System.out.println("before addActionListener");
        submitButton.addActionListener(e -> {
            System.out.println("actionPerformed : " + e.getActionCommand());
            String inputCode = inputTextArea.getText();
            try {
                ISharedSchemaContext context = factory.build(inputCode, DefaultDecoratorContext.builder().build());
                outputTextArea1.setText(context.getSqlDefinitions().stream().map(definition -> definition.getCreateScript()).collect(Collectors.joining("\n")));
                LinkedList<SQLDefinition> stack = new LinkedList<>();
                context.getSqlDefinitions().forEach(stack::push);
                outputTextArea2.setText(stack.stream().map(definition -> definition.getDropScript()).collect(Collectors.joining("\n")));
                outputTextArea3.setText(context.getSqlDefinitions().stream().filter(definition -> definition.getCheckingStatements() != null).flatMap(definition -> definition.getCheckingStatements().stream()).collect(Collectors.joining("\n")));
            } catch (InvalidConfigurationException ex) {
                throw new RuntimeException(ex);
            } catch (SharedSchemaContextBuilderException ex) {
                throw new RuntimeException(ex);
            } catch (RuntimeException exception) {
                System.out.println("exception");
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(inputTextArea));
        panel.add(new JScrollPane(outputTextArea1));
        panel.add(new JScrollPane(outputTextArea2));
        panel.add(new JScrollPane(outputTextArea3));
        panel.add(submitButton);
        add(panel);

        pack();
    }

    public PosmultenApp() {
        this(new YamlSharedSchemaContextFactory());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PosmultenApp app = new PosmultenApp();
                app.setVisible(true);
            }
        });
    }

    public void setMiglayout(LC layout, AC columns, AC rows) {
        setLayout(new MigLayout(layout, columns, rows));
    }

    public void setMiglayout() {
        setMiglayout(new LC(), new AC(), new AC());
    }
}