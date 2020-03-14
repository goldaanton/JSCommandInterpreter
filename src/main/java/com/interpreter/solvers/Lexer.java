package com.interpreter.solvers;

import com.interpreter.token.Token;
import com.interpreter.token.TokenType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

    private String expression;
    private int pos;
    private char currentChar;

    private final char NONE = '\0';

    private static Map<String, Token> RESERVED_KEYWORDS;

    static {
        HashMap<String, Token> temporaryMap = new HashMap<>();

        Token programToken = new Token(TokenType.PROGRAM, "PROGRAM");
        Token varToken = new Token(TokenType.VAR, "VAR");
        Token intToken = new Token(TokenType.INTEGER, "int");
        Token doubleToken = new Token(TokenType.DOUBLE, "double");
        Token strToken = new Token(TokenType.STRING, "string");

        temporaryMap.put(programToken.getValue(String.class).orElseThrow(RuntimeException::new), programToken);
        temporaryMap.put(varToken.getValue(String.class).orElseThrow(RuntimeException::new), varToken);
        temporaryMap.put(intToken.getValue(String.class).orElseThrow(RuntimeException::new), intToken);
        temporaryMap.put(doubleToken.getValue(String.class).orElseThrow(RuntimeException::new), doubleToken);
        temporaryMap.put(strToken.getValue(String.class).orElseThrow(RuntimeException::new), strToken);

        RESERVED_KEYWORDS = Collections.unmodifiableMap(temporaryMap);
    }

    public Lexer(String expression) {
        this.expression = expression;
        this.pos = 0;
        this.currentChar = expression.charAt(pos);
    }

    private void advance() {
        pos++;
        if (pos >= expression.length())
            currentChar = NONE;
        else
            currentChar = expression.charAt(pos);
    }

    private void skipWhiteSpaces() {
        while (currentChar != NONE && Character.isWhitespace(currentChar))
            advance();
    }

    private void skipComment() {
        while (currentChar != ']')
            advance();
        currentChar = ';';
    }

    private Token getNumberToken() {
        StringBuilder number = new StringBuilder();
        while (currentChar != NONE && Character.isDigit(currentChar)) {
            number.append(currentChar);
            advance();
        }

        if (currentChar == '.') {
            number.append(currentChar);
            advance();

            while (currentChar != NONE && Character.isDigit(currentChar)) {
                number.append(currentChar);
                advance();
            }

            return new Token(TokenType.DOUBLE, Double.parseDouble(number.toString()));
        }

        return new Token(TokenType.INTEGER, Integer.parseInt(number.toString()));
    }

    private Token getString() {
        StringBuilder str = new StringBuilder();

        while (currentChar != NONE && currentChar != '\'') {
            str.append(currentChar);
            advance();
        }
        advance();
        return new Token(TokenType.STRING, str.toString());
    }

    private Token id() {
        StringBuilder result = new StringBuilder();

        while (currentChar != NONE && Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }

        return RESERVED_KEYWORDS.getOrDefault(result.toString(), new Token(TokenType.ID, result.toString()));
    }

    private Token getProcedure() {
        StringBuilder result = new StringBuilder();

        while (currentChar != NONE && Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }

        advance();
        advance();

        StringBuilder param = new StringBuilder();

        while (currentChar != '\'') {
            param.append(currentChar);
            advance();
        }

        advance();
        advance();

        return new Token(TokenType.PROCEDURE, result.toString(), param.toString());
    }

    public Token getNextToken() {
        if (Character.isWhitespace(currentChar))
            skipWhiteSpaces();
        if (Character.isDigit(currentChar))
            return getNumberToken();
        if (Character.isAlphabetic(currentChar))
            return id();
        if (currentChar == '[') {
            advance();
            skipComment();
        }
        if (currentChar == '\'') {
            advance();
            return getString();
        }
        if(currentChar == '_') {
            advance();
            return getProcedure();
        }

        Token token = new Token(TokenType.getTokenTypeByAbbreviation(currentChar));
        advance();

        return token;
    }

    /*private char peek() {
        int peekPos = ++pos;
        if(peekPos >= expression.length())
            return NONE;
        else
            return expression.charAt(peekPos);
    }*/
}
