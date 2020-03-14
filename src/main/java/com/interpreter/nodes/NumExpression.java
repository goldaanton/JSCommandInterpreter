package com.interpreter.nodes;

import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import com.interpreter.token.Token;

import java.util.Optional;

public class NumExpression implements AbstractExpression {

    private Token token;

    public NumExpression(Token token) {
        this.token = token;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        return token.getValue(Integer.class);
    }
}
