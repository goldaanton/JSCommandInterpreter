package com.interpreter.nodes;

import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import com.interpreter.token.Token;

import java.util.Optional;

public class TypeExpression implements AbstractExpression {

    private Token token;

    public TypeExpression(Token token) {
        this.token = token;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        return Optional.empty();
    }

    public Token getToken() {
        return token;
    }
}
