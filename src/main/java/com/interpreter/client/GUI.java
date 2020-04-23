package com.interpreter.client;

import com.interpreter.solvers.Interpreter;
import com.interpreter.solvers.Lexer;
import com.interpreter.solvers.Parser;

import javax.swing.*;

public class GUI extends JDialog {

    private JPanel contentPane;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton runButton;

    public GUI() {

        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        runButton.addActionListener(event -> interpret());
    }

    public void interpret() {
        outputArea.setText("");

        Lexer lexer = new Lexer(inputArea.getText());
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        interpreter.getContext().setOutput(outputArea);
        interpreter.interpret();
        interpreter.getContext().getGlobalScope().forEach((key, value) -> System.out.println(key + " = " + value));
    }

    public void buildGUI() {

        GUI dialog = new GUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public String getInput() {
        return inputArea.getText();
    }

    public JTextArea getOutputArea() {
        return outputArea;
    }

    public static void main(String[] args) {

        GUI dialog = new GUI();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
