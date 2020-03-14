package com.interpreter.nodes;

import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;

import java.util.Optional;

public class ClosePageProcedureExpression extends ProcedureExpression {
    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        context.getDriver().quit();
        return Optional.empty();
    }
}
