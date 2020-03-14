package com.interpreter.nodes;

import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import com.interpreter.token.Token;

import java.util.Optional;

public class PrintProcedureExpression extends ProcedureExpression {

    private Token procedure;

    public PrintProcedureExpression(Token procedure) {
        this.procedure = procedure;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        System.out.println(procedure.getParameters());
        return Optional.empty();
    }
}
