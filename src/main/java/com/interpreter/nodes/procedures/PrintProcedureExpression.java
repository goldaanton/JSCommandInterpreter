package com.interpreter.nodes.procedures;

import com.interpreter.nodes.AbstractExpression;
import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import org.apache.commons.lang3.text.StrBuilder;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class PrintProcedureExpression extends ProcedureExpression {

    public PrintProcedureExpression(List<AbstractExpression> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {

        JTextArea output = context.getOutput();
        StringBuilder stringBuilder = new StringBuilder();

        for (AbstractExpression i : parameters) {
            stringBuilder.append(i.solve(context).orElseThrow(RuntimeException::new));
            stringBuilder.append('\n');
        }
        output.append(stringBuilder.toString());
        return Optional.empty();
    }
}
