package com.interpreter.nodes.procedures;

import com.interpreter.nodes.AbstractExpression;
import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;

import java.util.List;
import java.util.Optional;

public class PrintProcedureExpression extends ProcedureExpression {

    private List<AbstractExpression> parameters;

    public PrintProcedureExpression(List<AbstractExpression> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        for (AbstractExpression i : parameters) {
            System.out.println(i.solve(context).orElseThrow(RuntimeException::new));
        }
        return Optional.empty();
    }
}
