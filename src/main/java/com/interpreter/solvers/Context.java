package com.interpreter.solvers;

import org.openqa.selenium.WebDriver;

import javax.swing.*;
import java.util.HashMap;
import java.util.Optional;

public class Context {

    private HashMap<String, Optional<?>> globalScope;
    private WebDriver driver;
    private JTextArea output;

    public Context() {
        globalScope = new HashMap<>();
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void setOutput(JTextArea output) {
        this.output = output;
    }

    public HashMap<String, Optional<?>> getGlobalScope() {
        return globalScope;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public JTextArea getOutput() {
        return output;
    }
}
