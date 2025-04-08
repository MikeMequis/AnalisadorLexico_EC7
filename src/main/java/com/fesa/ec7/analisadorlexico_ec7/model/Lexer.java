package com.fesa.ec7.analisadorlexico_ec7.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private int currentPos = 0;
    private List<Token> tokens;
    
    public Lexer(String input) {
        tokens = tokenize(input);
    }
    
    private List<Token> tokenize(String input) {
        List<Token> tokenList = new ArrayList<>();
        
        // Definição de padrões para tokens
        Pattern tokenPattern = Pattern.compile("\\(|\\)|\\+|\\-|\\*|\\/|\\=|[a-zA-Z][a-zA-Z0-9]*|\\s+");
        Matcher matcher = tokenPattern.matcher(input);
        
        while (matcher.find()) {
            String match = matcher.group().trim();
            if (!match.isEmpty()) {
                String type;
                switch (match) {
                    case "(": type = "("; break;
                    case ")": type = ")"; break;
                    case "+": type = "+"; break;
                    case "-": type = "-"; break;
                    case "*": type = "*"; break;
                    case "/": type = "/"; break;
                    case "=": type = "="; break;
                    default: type = "id"; break;
                }
                tokenList.add(new Token(type, match));
            }
        }
        
        // Adiciona token de fim de entrada
        tokenList.add(new Token("$", "$"));
        return tokenList;
    }
    
    public Token nextToken() {
        if (currentPos < tokens.size()) {
            return tokens.get(currentPos++);
        }
        return tokens.get(tokens.size() - 1); // Retorna o token de fim ($)
    }
    
    public Token peekToken() {
        if (currentPos < tokens.size()) {
            return tokens.get(currentPos);
        }
        return tokens.get(tokens.size() - 1);
    }
}
