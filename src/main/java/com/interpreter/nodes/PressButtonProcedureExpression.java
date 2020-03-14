package com.interpreter.nodes;

import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import com.interpreter.token.Token;
import org.openqa.selenium.By;

import java.util.Optional;

public class PressButtonProcedureExpression extends ProcedureExpression {

    private Token token;

    public PressButtonProcedureExpression(Token token) {
        this.token = token;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        context.getDriver().findElement(By.xpath(token.getParameters())).click();

        return Optional.empty();
    }
}
