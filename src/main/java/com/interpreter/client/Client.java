package com.interpreter.client;

import com.interpreter.solvers.Interpreter;
import com.interpreter.solvers.Lexer;
import com.interpreter.solvers.Parser;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Client {

    private static GUI gui;

    public static void main(String[] args) {
        gui = new GUI();
        gui.buildGUI();
    }

    public void go() {
        gui = new GUI();
        gui.buildGUI();
    }

    public void interpret() {

        String input = gui.getInput();

        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        interpreter.getContext().setOutput(gui.getOutputArea());
        interpreter.interpret();
        interpreter.getContext().getGlobalScope().forEach((key, value) -> System.out.println(key + " = " + value));

        /*StringBuilder expression = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                expression.append(line);
            }
            reader.close();

        } catch (IOException ex) {

        }*/
    }
}
