package com.interpreter.client;

import com.interpreter.solvers.Interpreter;
import com.interpreter.solvers.Lexer;
import com.interpreter.solvers.Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        StringBuilder expression = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                expression.append(line);
            }
            reader.close();

        } catch (IOException ex) {

        }

        Lexer lexer = new Lexer(expression.toString());
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
        interpreter.getContext().getGlobalScope().forEach((key, value) -> System.out.println(key + " = " + value));
    }
}
