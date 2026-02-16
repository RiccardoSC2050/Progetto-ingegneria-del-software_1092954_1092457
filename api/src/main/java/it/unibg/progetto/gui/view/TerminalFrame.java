package it.unibg.progetto.gui.view;

import java.awt.BorderLayout;
import java.awt.Font;

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
		setBounds(0, 0, 1200, 800);
		setLocationRelativeTo(null);


		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
		contentPane.setLayout(new BorderLayout(8, 8));
		setContentPane(contentPane);

		
		outputArea = new JTextArea();
		outputArea.setEditable(false);

		
		outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
		outputArea.setLineWrap(false); 
		outputArea.setWrapStyleWord(false);

		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		
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
	
	public void appendRaw(String s) {
	    outputArea.append(s);
	    outputArea.setCaretPosition(outputArea.getDocument().getLength());
	}


	

	public void onSubmit(Runnable handler) {
		inputField.addActionListener(e -> handler.run());
		sendButton.addActionListener(e -> handler.run());
	}

	public void onClear(Runnable handler) {
		clearButton.addActionListener(e -> handler.run());
	}
}