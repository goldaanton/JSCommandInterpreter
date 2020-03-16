package com.interpreter.token;

import com.interpreter.exceptions.BadDefaultValueException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum TokenType {

    ADDITION('+'),
    SUBTRACTION('-'),
    DIVISION('/'),
    MULTIPLICATION('*'),

    L_PARENTHESIS('('),
    R_PARENTHESIS(')'),

    INTEGER('i'),
    DOUBLE('f'),
    STRING('s'),

    PROGRAM('p'),
    VAR('v'),
    BEGIN('{'),
    END('}'),

    IF('c'),
    ELSE('1'),
    MORE('>'),
    LESS('<'),
    AND('&'),
    OR('|'),

    ID('d'),
    PROCEDURE('_'),
    ASSIGN('='),

    SEMI(';'),
    DOT('.'),
    COLON(':'),
    COMMA(','),

    EOF('\0');

    private char tokenTypeAbbreviation;

    TokenType(char tokenTypeAbbreviation) {
        this.tokenTypeAbbreviation = tokenTypeAbbreviation;
    }

    private static final Map<Character, TokenType> TOKEN_ABBREVIATION = Collections.unmodifiableMap(Arrays.stream(values())
            .collect(Collectors.toMap(TokenType::getTokenTypeAbbreviation, tokenType -> tokenType)));

    public static TokenType getTokenTypeByAbbreviation(char abbreviation) {
        return TOKEN_ABBREVIATION.getOrDefault(abbreviation, EOF);
    }

    public static Optional<?> getDefaultValue(TokenType type) {
        switch (type) {
            case INTEGER:
                return Optional.of(0);
            case DOUBLE:
                return Optional.of(0.0);
            case STRING:
                return Optional.of("");
            default:
                throw new BadDefaultValueException(type.name());
        }
    }

    public char getTokenTypeAbbreviation() {
        return tokenTypeAbbreviation;
    }
}
