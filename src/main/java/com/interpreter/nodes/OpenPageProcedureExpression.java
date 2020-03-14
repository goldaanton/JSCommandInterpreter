package com.interpreter.nodes;

import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import com.interpreter.token.Token;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class OpenPageProcedureExpression extends ProcedureExpression {

    private Token token;

    public OpenPageProcedureExpression(Token token) {
        this.token = token;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {
    }

    @Override
    public Optional<?> solve(Context context) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Anton\\IdeaProjects\\TestSelenium\\drivers\\chromedriver.exe");

        context.setDriver(new ChromeDriver());

        WebDriver driver = context.getDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.navigate().to(token.getParameters());

        return Optional.empty();
    }
}
