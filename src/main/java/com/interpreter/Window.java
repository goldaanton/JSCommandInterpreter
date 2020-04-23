package com.interpreter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window {

    private JTextArea inputArea;
    private JTextArea outputArea;

    public void buildGUI() {
        JFrame frame = new JFrame("ChromeControl");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));

        JLabel inputLabel = new JLabel("Input");
        inputArea = new JTextArea(50, 20);
        inputArea.setMaximumSize(new Dimension(90, 40));
        JLabel outputLabel = new JLabel("Output");
        outputArea = new JTextArea(50, 10);
        JButton runButton = new JButton("Run");

        runButton.addActionListener(new RunCodeListener());

        inputPanel.add(inputLabel);
        inputPanel.add(inputArea);
        outputPanel.add(outputLabel);
        outputPanel.add(outputArea);

        frame.getContentPane().add(BorderLayout.SOUTH, runButton);
        frame.getContentPane().add(BorderLayout.WEST, inputPanel);
        frame.getContentPane().add(BorderLayout.EAST, outputPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Window gui = new Window();
        gui.buildGUI();
    }

    public class RunCodeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(inputArea.getText());
        }
    }
}
