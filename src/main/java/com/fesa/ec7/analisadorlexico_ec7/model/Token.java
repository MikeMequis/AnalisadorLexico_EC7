package com.fesa.ec7.analisadorlexico_ec7.model;

public class Token {
    private final String type;
    private final String value;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
    
    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{type='" + type + "', value='" + value + "'}";
    }
}
