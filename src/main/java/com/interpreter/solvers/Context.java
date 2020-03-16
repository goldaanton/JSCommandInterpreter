package com.interpreter.solvers;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Optional;

public class Context {

    private HashMap<String, Optional<?>> globalScope;
    private WebDriver driver;

    public Context() {
        globalScope = new HashMap<>();
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public HashMap<String, Optional<?>> getGlobalScope() {
        return globalScope;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
