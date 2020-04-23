package com.interpreter.nodes.procedures;

import com.interpreter.nodes.AbstractExpression;
import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class OpenPageProcedureExpression extends ProcedureExpression {

    public OpenPageProcedureExpression(List<AbstractExpression> parameters) {
        this.parameters = parameters;
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

        driver.navigate().to(((Optional<String>) parameters.get(0).solve(context))
                .orElseThrow(RuntimeException::new));

        return Optional.empty();
    }
}
