package com.fesa.ec7.analisadorlexico_ec7.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private int currentPos = 0;
    private final List<Token> tokens;

    public Lexer(String input) {
        this.tokens = tokenize(input);
    }

    private List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("([()+\\-*/=])|([a-zA-Z]\\w*)|\\s+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group();
            if (match.matches("\\s+")) continue;

            String type = switch (match) {
                case "(", ")", "+", "-", "*", "/", "=" -> match;
                default -> "id";
            };
            tokens.add(new Token(type, match));
        }
        tokens.add(new Token("$", "$"));
        return tokens;
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
