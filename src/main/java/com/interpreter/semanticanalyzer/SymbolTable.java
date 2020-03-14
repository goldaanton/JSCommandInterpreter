package com.interpreter.semanticanalyzer;

import com.interpreter.exceptions.NoSuchSymbolException;
import com.interpreter.symbols.BuiltinTypeSymbol;
import com.interpreter.symbols.Symbol;
import com.interpreter.token.TokenType;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, Symbol> symbols;

    public SymbolTable() {
        symbols = new LinkedHashMap<>();
        initializeBuiltIn();
    }

    public void defineSymbol(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    public boolean containSymbol(String symbolName) {
        return symbols.containsKey(symbolName);
    }

    public void initializeBuiltIn() {
        defineSymbol(new BuiltinTypeSymbol(TokenType.INTEGER.name()));
        defineSymbol(new BuiltinTypeSymbol(TokenType.DOUBLE.name()));
        defineSymbol(new BuiltinTypeSymbol(TokenType.STRING.name()));
    }

    public Symbol fetchSymbol(String symbolName) {
        if (!symbols.containsKey(symbolName))
            throw new NoSuchSymbolException(symbolName);

        return symbols.get(symbolName);
    }
}
