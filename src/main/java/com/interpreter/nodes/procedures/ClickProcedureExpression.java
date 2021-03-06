package com.interpreter.nodes.procedures;

import com.interpreter.nodes.AbstractExpression;
import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import org.openqa.selenium.By;

import java.util.List;
import java.util.Optional;

public class ClickProcedureExpression extends ProcedureExpression {

    public ClickProcedureExpression(List<AbstractExpression> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        context.getDriver().findElement(By.xpath(((Optional<String>) parameters.get(0).solve(context))
                .orElseThrow(RuntimeException::new))).click();

        return Optional.empty();
    }
}
