package com.interpreter.solvers;

import com.interpreter.exceptions.InvalidSyntaxException;
import com.interpreter.exceptions.NoSuchProcedureNameException;
import com.interpreter.nodes.*;
import com.interpreter.nodes.procedures.*;
import com.interpreter.token.Token;
import com.interpreter.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
    }

    private void eat(TokenType type) {
        if (currentToken.getType() == type)
            currentToken = lexer.getNextToken();
        else
            throw new InvalidSyntaxException(currentToken.getType().name());
    }

    private AbstractExpression factor() {
        /*
         *       factor : PLUS  factor | MINUS factor | INTEGER
         *              | L_PARENTHESIS expr R_PARENTHESIS | variable
         */

        Token token = currentToken;

        switch (token.getType()) {
            case ADDITION:
                eat(TokenType.ADDITION);
                return new UnaryOpExpression(token, factor());
            case SUBTRACTION:
                eat(TokenType.SUBTRACTION);
                return new UnaryOpExpression(token, factor());
            case INTEGER:
                eat(TokenType.INTEGER);
                return new NumExpression(token);
            case DOUBLE:
                eat(TokenType.DOUBLE);
                return new NumExpression(token);
            case STRING:
                eat(TokenType.STRING);
                return new StringExpression(token);
            case L_PARENTHESIS:
                eat(TokenType.L_PARENTHESIS);
                AbstractExpression node = expr();
                eat(TokenType.R_PARENTHESIS);
                return node;
            default:
                return variable();
        }
    }

    private AbstractExpression term() {
        /*
         *       term : factor ((MUL | DIV) factor)*
         */

        AbstractExpression node = factor();

        while (currentToken.getType() == TokenType.DIVISION || currentToken.getType() == TokenType.MULTIPLICATION) {
            Token token = currentToken;
            if (token.getType() == TokenType.MULTIPLICATION)
                eat(TokenType.MULTIPLICATION);
            else if (token.getType() == TokenType.DIVISION)
                eat(TokenType.DIVISION);

            node = new BinOpExpression(node, token, factor());
        }

        return node;
    }

    private AbstractExpression expr() {
        /*
         *       expr : term ((PLUS | MINUS) term)*
         */

        AbstractExpression node = term();

        while (currentToken.getType() == TokenType.ADDITION || currentToken.getType() == TokenType.SUBTRACTION) {
            Token token = currentToken;
            if (token.getType() == TokenType.ADDITION)
                eat(TokenType.ADDITION);
            else if (token.getType() == TokenType.SUBTRACTION)
                eat(TokenType.SUBTRACTION);

            node = new BinOpExpression(node, token, term());
        }

        return node;
    }

    private AbstractExpression empty() {
        /*
         *      An empty production
         */

        return new NoOpExpression();
    }

    private AbstractExpression assignmentStatement() {
        /*
         *      assignment_statement : variable ASSIGN expr
         */

        AbstractExpression left = variable();
        eat(TokenType.ASSIGN);
        AbstractExpression right = expr();

        return new AssignExpression((VarExpression) left, right);
    }

    private List<AbstractExpression> getParameters() {
        /*
         *      parameters : (factor (COMMA factor)*)*
         */

        List<AbstractExpression> parameters = new ArrayList<>();
        if (currentToken.getType() != TokenType.R_PARENTHESIS) {
            parameters.add(factor());
        }

        while (currentToken.getType() != TokenType.R_PARENTHESIS) {
            eat(TokenType.COMMA);
            parameters.add(factor());
        }

        return parameters;
    }

    private AbstractExpression procedureStatement() {
        /*
         *      procedure_statement : PROCEDURE L_PARENTHESIS parameters R_PARENTHESIS
         */

        Token procedure = currentToken;
        eat(TokenType.PROCEDURE);
        eat(TokenType.L_PARENTHESIS);
        List<AbstractExpression> parameters = getParameters();
        eat(TokenType.R_PARENTHESIS);

        switch (procedure.getValue(String.class).orElseThrow(RuntimeException::new)) {
            case "print":
                return new PrintProcedureExpression(parameters);
            case "openPage":
                return new OpenPageProcedureExpression(parameters);
            case "click":
                return new ClickProcedureExpression(parameters);
            case "setText":
                return new SetTextProcedureExpression(parameters);
            case "closePage":
                return new ClosePageProcedureExpression();
            default:
                throw new NoSuchProcedureNameException(procedure.getValue(String.class).orElseThrow(RuntimeException::new));
        }
    }

    private AbstractExpression conditionalStatement() {
        /*
         *      conditionalStatement : IF L_PARENTHESIS conditional R_PARENTHESIS compound (ELSE compound)?
         *      conditional : factor (LESS | MORE) factor ((AND | OR) conditional)*
         */

        List<AbstractExpression> elements = new ArrayList<>();
        List<Token> signs = new ArrayList<>();
        List<Token> separators = new ArrayList<>();

        eat(TokenType.IF);
        eat(TokenType.L_PARENTHESIS);

        while (currentToken.getType() != TokenType.R_PARENTHESIS) {
            elements.add(factor());

            Token sign = currentToken;
            if (sign.getType() == TokenType.MORE) {
                eat(TokenType.MORE);
            } else {
                eat(TokenType.LESS);
            }
            signs.add(sign);

            elements.add(factor());

            if (currentToken.getType() == TokenType.AND || currentToken.getType() == TokenType.OR) {
                sign = currentToken;
                if (currentToken.getType() == TokenType.AND) {
                    eat(TokenType.AND);
                } else {
                    eat(TokenType.OR);
                }
                separators.add(sign);
            }
        }

        eat(TokenType.R_PARENTHESIS);

        AbstractExpression ifCompound = compoundStatement();

        if (currentToken.getType() == TokenType.ELSE) {
            eat(TokenType.ELSE);
            AbstractExpression elseCompound = compoundStatement();
            return new ConditionalExpression(elements, signs, separators, ifCompound, elseCompound);
        }

        return new ConditionalExpression(elements, signs, separators, ifCompound);
    }

    private AbstractExpression statement() {
        /*
         *      statement : compound_statement | assignment_statement | procedure | conditional | empty
         */

        switch (currentToken.getType()) {
            case BEGIN:
                return compoundStatement();
            case ID:
                return assignmentStatement();
            case PROCEDURE:
                return procedureStatement();
            case IF:
                return conditionalStatement();
            default:
                return empty();
        }
    }

    private ArrayList<AbstractExpression> statementList() {
        /*
         *      statement_list : statement | statement SEMI statement_list
         */

        AbstractExpression node = statement();

        ArrayList<AbstractExpression> result = new ArrayList<>();
        result.add(node);

        while (currentToken.getType() == TokenType.SEMI) {
            eat(TokenType.SEMI);
            result.add(statement());
        }

        if (currentToken.getType() == TokenType.ID)
            throw new InvalidSyntaxException("statementList");

        return result;
    }

    private AbstractExpression compoundStatement() {
        /*
         *      compoundStatement : BEGIN statementList END
         */

        eat(TokenType.BEGIN);
        ArrayList<AbstractExpression> nodes = statementList();
        eat(TokenType.END);

        CompoundExpression root = new CompoundExpression();
        for (AbstractExpression node : nodes) {
            root.getChildren().add(node);
        }

        return root;
    }

    private TypeExpression typeSpecification() {
        /*
         * type_spec : INTEGER | REAL
         */

        Token token = currentToken;
        if (currentToken.getType() == TokenType.INTEGER)
            eat(TokenType.INTEGER);
        else if (currentToken.getType() == TokenType.DOUBLE)
            eat(TokenType.DOUBLE);
        else if (currentToken.getType() == TokenType.STRING)
            eat(TokenType.STRING);
        return new TypeExpression(token);
    }

    private List<DeclarationExpression> variableDeclaration() {
        /*
         *      variable_declaration : ID (COMMA ID)* COLON type_spec
         */

        List<VarExpression> varNodes = new ArrayList<>();
        varNodes.add(new VarExpression(currentToken));
        eat(TokenType.ID);

        while (currentToken.getType() == TokenType.COMMA) {
            eat(TokenType.COMMA);
            varNodes.add(new VarExpression(currentToken));
            eat(TokenType.ID);
        }

        eat(TokenType.COLON);

        TypeExpression typeExpression = typeSpecification();

        List<DeclarationExpression> variableDeclaration = new ArrayList<>();

        for (VarExpression varExpression : varNodes) {
            variableDeclaration.add(new VarDeclarationExpression(varExpression, typeExpression.getToken().getType()));
        }

        return variableDeclaration;
    }

    private List<DeclarationExpression> declaration() {
        /*
         *      declarations : VAR (variable_declaration SEMI)+ | empty
         */

        List<DeclarationExpression> declaration = new ArrayList<>();
        if (currentToken.getType() == TokenType.VAR) {
            eat(TokenType.VAR);

            while (currentToken.getType() == TokenType.ID) {
                List<DeclarationExpression> varDel = variableDeclaration();
                declaration.addAll(varDel);
                eat(TokenType.SEMI);
            }
        }

        return declaration;
    }

    private BlockExpression block() {
        /*
         *      block : declaration compoundStatement
         */

        List<DeclarationExpression> declarationNodes = declaration();
        CompoundExpression compoundExpression = (CompoundExpression) compoundStatement();

        return new BlockExpression(declarationNodes, compoundExpression);
    }

    private AbstractExpression variable() {
        /*
         *      variable : ID
         */

        AbstractExpression node = new VarExpression(currentToken);
        eat(TokenType.ID);

        return node;
    }

    private AbstractExpression program() {
        /*
         *      program : PROGRAM variable SEMI block DOT
         */

        eat(TokenType.PROGRAM);
        VarExpression varNode = (VarExpression) variable();
        String programName = varNode.getVarToken().getValue(String.class)
                .orElseThrow(RuntimeException::new);
        eat(TokenType.SEMI);

        BlockExpression blockExpression = block();

        ProgramExpression programExpression = new ProgramExpression(programName, blockExpression);
        eat(TokenType.DOT);

        return programExpression;
    }

    public AbstractExpression parse() {
        AbstractExpression root = program();
        if (currentToken.getType() != TokenType.EOF)
            throw new InvalidSyntaxException("parse");

        return root;
    }
}
