package com.interpreter.nodes;

import com.interpreter.semanticanalyzer.SymbolTable;
import com.interpreter.solvers.Context;
import com.interpreter.token.Token;
import com.interpreter.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConditionalExpression implements AbstractExpression {

    private AbstractExpression ifCompound;
    private AbstractExpression elseCompound;
    private List<AbstractExpression> elements;
    private List<Token> signs;
    private List<Token> separators;

    public ConditionalExpression(List<AbstractExpression> elements, List<Token> signs, List<Token> separators, AbstractExpression compound) {
        this.ifCompound = compound;
        this.elements = elements;
        this.signs = signs;
        this.separators = separators;
    }

    public ConditionalExpression(List<AbstractExpression> elements, List<Token> signs, List<Token> separators,
                                 AbstractExpression ifCompound, AbstractExpression elseCompound) {
        this.ifCompound = ifCompound;
        this.elseCompound = elseCompound;
        this.elements = elements;
        this.signs = signs;
        this.separators = separators;
    }

    @Override
    public void analyzeNode(SymbolTable symbolTable) {

    }

    @Override
    public Optional<?> solve(Context context) {

        if (separators.size() == 1) {
            int left = ((Optional<Integer>) elements.get(0).solve(context))
                    .orElseThrow(RuntimeException::new);

            int right = ((Optional<Integer>) elements.get(1).solve(context))
                    .orElseThrow(RuntimeException::new);

            if (separators.get(0).getType() == TokenType.OR) {
                if ((signs.get(0).getType() == TokenType.LESS && left < right) ||
                        (signs.get(0).getType() == TokenType.MORE && left > right)) {
                    ifCompound.solve(context);
                    return Optional.empty();
                }
            } else {
                if ((signs.get(0).getType() == TokenType.LESS && left > right) ||
                        (signs.get(0).getType() == TokenType.MORE && left < right)) {
                    if (elseCompound != null) {
                        elseCompound.solve(context);
                    }
                    return Optional.empty();
                }
            }
        }

        List<Boolean> results = new ArrayList<>();

        int signN = 0;
        for (int i = 0; i < elements.size() - 1; i += 2) {
            int left = ((Optional<Integer>) elements.get(i).solve(context))
                    .orElseThrow(RuntimeException::new);

            int right = ((Optional<Integer>) elements.get(i + 1).solve(context))
                    .orElseThrow(RuntimeException::new);

            if ((signs.get(signN).getType() == TokenType.LESS && left < right) ||
                    (signs.get(signN).getType() == TokenType.MORE && left > right))
                results.add(true);
            else
                results.add(false);
            signN++;
        }

        boolean prev = results.get(0);
        boolean result = prev;

        for (int i = 1; i < results.size(); i++) {
            result = (separators.get(i - 1).getType() == TokenType.AND && prev && results.get(i) ||
                    (separators.get(i - 1).getType() == TokenType.OR && (prev || results.get(i))));

            prev = result;
        }

        if (result)
            ifCompound.solve(context);
        else if (elseCompound != null)
            elseCompound.solve(context);

        return Optional.empty();
    }
}

