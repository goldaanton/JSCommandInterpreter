package com.interpreter.nodes;

import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import com.interpreter.token.Token;
import org.openqa.selenium.By;

import java.util.Optional;

public class SetTextProcedureExpression extends ProcedureExpression {

    private Token token;

    public SetTextProcedureExpression(Token token) {
        this.token = token;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        String[] args = token.getParameters().split(" ");

        context.getDriver().findElement(By.xpath(args[0])).sendKeys(args[1]);


        return Optional.empty();
    }
}
