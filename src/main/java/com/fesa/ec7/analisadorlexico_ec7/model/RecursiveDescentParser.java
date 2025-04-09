package com.fesa.ec7.analisadorlexico_ec7.model;

import java.util.Map;
import java.util.Set;

public class RecursiveDescentParser {
    private Lexer lexer;
    private Token currentToken;
    private FirstFollowCalculator ffCalculator;
    private Map<String, Set<String>> followSets;

    public RecursiveDescentParser(Lexer lexer, Grammar grammar) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
        
        this.ffCalculator = new FirstFollowCalculator(grammar);
        ffCalculator.getFirstSets();
        this.followSets = ffCalculator.getFollowSets();
    }

    public ASTNode parse() {
        ASTNode root = S();
        if (!currentToken.getType().equals("$")) {
            throw new RuntimeException("Erro sintático: entrada não consumida totalmente");
        }
        return root;
    }

    private void match(String expected) {
        if (currentToken.getType().equals(expected)) {
            currentToken = lexer.nextToken();
        } else {
            throw new RuntimeException("Erro sintático: esperado '" + expected + "', encontrado '" + currentToken.getType() + "'");
        }
    }

    // S -> id = E
    private ASTNode S() {
        ASTNode node = new ASTNode("S");
        
        if (currentToken.getType().equals("id")) {
            node.addChild(new ASTNode(currentToken.getValue()));
            match("id");
        } else {
            throw new RuntimeException("Erro sintático: esperado 'id', encontrado '" + currentToken.getType() + "'");
        }
        
        if (currentToken.getType().equals("=")) {
            node.addChild(new ASTNode("="));
            match("=");
        } else {
            throw new RuntimeException("Erro sintático: esperado '=', encontrado '" + currentToken.getType() + "'");
        }
        
        node.addChild(E());
        return node;
    }

    // E -> T E'
    private ASTNode E() {
        ASTNode node = new ASTNode("E");
        node.addChild(T());
        node.addChild(EPrime());
        return node;
    }

    // E' -> + T E' | - T E' | ε
    private ASTNode EPrime() {
        ASTNode node = new ASTNode("E'");
        
        if (currentToken.getType().equals("+") || currentToken.getType().equals("-")) {
            node.addChild(new ASTNode(currentToken.getValue()));
            String op = currentToken.getType();
            match(op);
            node.addChild(T());
            node.addChild(EPrime());
        } else if (followSets.get("E'").contains(currentToken.getType())) {
            node.addChild(new ASTNode("ε"));
        } else {
            throw new RuntimeException("Erro sintático em E': esperado '+', '-', ou um dos tokens " + 
                followSets.get("E'") + ", encontrado '" + currentToken.getType() + "'");
        }
        
        return node;
    }

    // T -> F T'
    private ASTNode T() {
        ASTNode node = new ASTNode("T");
        node.addChild(F());
        node.addChild(TPrime());
        return node;
    }

    // T' -> * F T' | / F T' | ε
    private ASTNode TPrime() {
        ASTNode node = new ASTNode("T'");
        
        if (currentToken.getType().equals("*") || currentToken.getType().equals("/")) {
            node.addChild(new ASTNode(currentToken.getValue()));
            String op = currentToken.getType();
            match(op);
            node.addChild(F());
            node.addChild(TPrime());
        } else if (followSets.get("T'").contains(currentToken.getType())) {
            node.addChild(new ASTNode("ε"));
        } else {
            throw new RuntimeException("Erro sintático em T': esperado '*', '/', ou um dos tokens " + 
                followSets.get("T'") + ", encontrado '" + currentToken.getType() + "'");
        }
        
        return node;
    }

    // F -> ( E ) | id
    private ASTNode F() {
        ASTNode node = new ASTNode("F");
        
        if (currentToken.getType().equals("(")) {
            node.addChild(new ASTNode("("));
            match("(");
            node.addChild(E());
            if (currentToken.getType().equals(")")) {
                node.addChild(new ASTNode(")"));
                match(")");
            } else {
                throw new RuntimeException("Erro sintático: esperado ')', encontrado '" + currentToken.getType() + "'");
            }
        } else if (currentToken.getType().equals("id")) {
            node.addChild(new ASTNode(currentToken.getValue()));
            match("id");
        } else {
            throw new RuntimeException("Erro sintático em F: esperado '(' ou 'id', encontrado '" + currentToken.getType() + "'");
        }
        
        return node;
    }
}
