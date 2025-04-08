package com.fesa.ec7.analisadorlexico_ec7.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grammar {
    private Map<String, List<List<String>>> productions;
    private String startSymbol;
    
    public Grammar() {
        this.productions = new HashMap<>();
    }
    
    public void addProduction(String nonTerminal, List<String> production) {
        if (!productions.containsKey(nonTerminal)) {
            productions.put(nonTerminal, new ArrayList<>());
        }
        productions.get(nonTerminal).add(production);
    }
    
    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }
    
    public Map<String, List<List<String>>> getProductions() {
        return productions;
    }
    
    public String getStartSymbol() {
        return startSymbol;
    }
    
    public boolean isTerminal(String symbol) {
        return !productions.containsKey(symbol);
    }
    
    public static Grammar createAssignmentGrammar() {
        Grammar grammar = new Grammar();
        
        // S -> id = E
        grammar.addProduction("S", List.of("id", "=", "E"));
        
        // E -> T E'
        grammar.addProduction("E", List.of("T", "E'"));
        
        // E' -> + T E' | - T E' | ε
        grammar.addProduction("E'", List.of("+", "T", "E'"));
        grammar.addProduction("E'", List.of("-", "T", "E'"));
        grammar.addProduction("E'", List.of()); // ε
        
        // T -> F T'
        grammar.addProduction("T", List.of("F", "T'"));
        
        // T' -> * F T' | / F T' | ε
        grammar.addProduction("T'", List.of("*", "F", "T'"));
        grammar.addProduction("T'", List.of("/", "F", "T'"));
        grammar.addProduction("T'", List.of()); // ε
        
        // F -> ( E ) | id
        grammar.addProduction("F", List.of("(", "E", ")"));
        grammar.addProduction("F", List.of("id"));
        
        grammar.setStartSymbol("S");
        
        return grammar;
    }
}