package com.interpreter.nodes.procedures;

import com.interpreter.nodes.AbstractExpression;

import java.util.List;

public abstract class ProcedureExpression implements AbstractExpression {

    protected List<AbstractExpression> parameters;

}
