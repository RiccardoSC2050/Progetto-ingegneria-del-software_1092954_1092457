package it.unibg.progetto.gui.view;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TerminalFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextArea outputArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private final JButton clearButton;

    public TerminalFrame() {
        setTitle("APP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 450);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        contentPane.setLayout(new BorderLayout(8, 8));
        setContentPane(contentPane);

        // OUTPUT
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // INPUT BAR
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        contentPane.add(bottom, BorderLayout.SOUTH);

        inputField = new JTextField();
        bottom.add(inputField, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        bottom.add(buttons, BorderLayout.EAST);

        sendButton = new JButton("Invia");
        buttons.add(sendButton);

        clearButton = new JButton("Clear");
        buttons.add(clearButton);
    }

    // ===== Metodi "View API" (usati dal Controller) =====

    public void appendLine(String s) {
        outputArea.append(s + System.lineSeparator());
        outputArea.setCaretPosition(outputArea.getDocument().getLength()); // auto-scroll
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInput() {
        inputField.setText("");
    }

    public void clearOutput() {
        outputArea.setText("");
    }

    public void focusInput() {
        inputField.requestFocusInWindow();
    }

    // ===== Hook per eventi (il controller registra i listener) =====

    public void onSubmit(Runnable handler) {
        // Enter nel field
        inputField.addActionListener(e -> handler.run());
        // Click su invia
        sendButton.addActionListener(e -> handler.run());
    }

    public void onClear(Runnable handler) {
        clearButton.addActionListener(e -> handler.run());
    }
}
