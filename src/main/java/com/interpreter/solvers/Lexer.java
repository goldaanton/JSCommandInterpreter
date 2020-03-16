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
        Token printProcedureToken = new Token(TokenType.PROCEDURE, "print");
        Token clickProcedureToken = new Token(TokenType.PROCEDURE, "click");
        Token closePageProcedureToken = new Token(TokenType.PROCEDURE, "closePage");
        Token openPageProcedureToken = new Token(TokenType.PROCEDURE, "openPage");
        Token setTextProcedureToken = new Token(TokenType.PROCEDURE, "setText");
        Token ifToken = new Token(TokenType.IF, "if");
        Token elseToken = new Token(TokenType.ELSE, "else");


        temporaryMap.put(programToken.getValue(String.class).orElseThrow(RuntimeException::new), programToken);
        temporaryMap.put(varToken.getValue(String.class).orElseThrow(RuntimeException::new), varToken);
        temporaryMap.put(intToken.getValue(String.class).orElseThrow(RuntimeException::new), intToken);
        temporaryMap.put(doubleToken.getValue(String.class).orElseThrow(RuntimeException::new), doubleToken);
        temporaryMap.put(strToken.getValue(String.class).orElseThrow(RuntimeException::new), strToken);
        temporaryMap.put(printProcedureToken.getValue(String.class).orElseThrow(RuntimeException::new), printProcedureToken);
        temporaryMap.put(clickProcedureToken.getValue(String.class).orElseThrow(RuntimeException::new), clickProcedureToken);
        temporaryMap.put(closePageProcedureToken.getValue(String.class).orElseThrow(RuntimeException::new), closePageProcedureToken);
        temporaryMap.put(openPageProcedureToken.getValue(String.class).orElseThrow(RuntimeException::new), openPageProcedureToken);
        temporaryMap.put(setTextProcedureToken.getValue(String.class).orElseThrow(RuntimeException::new), setTextProcedureToken);
        temporaryMap.put(ifToken.getValue(String.class).orElseThrow(RuntimeException::new), ifToken);
        temporaryMap.put(elseToken.getValue(String.class).orElseThrow(RuntimeException::new), elseToken);

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

        Token token = new Token(TokenType.getTokenTypeByAbbreviation(currentChar));
        advance();

        return token;
    }

    private char peek() {
        int peekPos = ++pos;
        if (peekPos >= expression.length())
            return NONE;
        else
            return expression.charAt(peekPos);
    }
}
