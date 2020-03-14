package com.interpreter.exceptions;

public class NoSuchProcedureNameException extends RuntimeException {

    private String name;

    public NoSuchProcedureNameException(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "NoSuchProcedureNameException{" +
                "name='" + name + '\'' +
                '}';
    }
}
