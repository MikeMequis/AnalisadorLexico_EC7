package com.fesa.ec7.analisadorlexico_ec7.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FirstFollowCalculator {
    private Grammar grammar;
    private Map<String, Set<String>> firstSets;
    private Map<String, Set<String>> followSets;

    public FirstFollowCalculator(Grammar grammar) {
        this.grammar = grammar;
        this.firstSets = new HashMap<>();
        this.followSets = new HashMap<>();
        
        // Inicializa os conjuntos First e Follow
        initializeSets();
        
        // Computa os conjuntos
        computeFirstSets();
        computeFollowSets();
    }

    private void initializeSets() {
        for (String nonTerminal : grammar.getProductions().keySet()) {
            firstSets.put(nonTerminal, new HashSet<>());
            followSets.put(nonTerminal, new HashSet<>());
        }
        // Adiciona $ ao Follow do símbolo inicial
        followSets.get(grammar.getStartSymbol()).add("$");
    }

    private void computeFirstSets() {
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, List<List<String>>> entry : grammar.getProductions().entrySet()) {
                String nonTerminal = entry.getKey();
                for (List<String> production : entry.getValue()) {
                    // Produção vazia
                    if (production.isEmpty()) {
                        if (firstSets.get(nonTerminal).add("ε")) {
                            changed = true;
                        }
                        continue;
                    }
                    
                    // Calcula First para a sequência de símbolos
                    Set<String> firstOfProduction = computeFirstOfSequence(production);
                    if (firstSets.get(nonTerminal).addAll(firstOfProduction)) {
                        changed = true;
                    }
                }
            }
        } while (changed);
    }

    private Set<String> computeFirstOfSequence(List<String> sequence) {
        if (sequence.isEmpty()) return Set.of("ε");
        
        String firstSymbol = sequence.get(0);
        
        if (grammar.isTerminal(firstSymbol)) {
            return Set.of(firstSymbol);
        }
        
        Set<String> result = new HashSet<>(firstSets.get(firstSymbol));
        result.remove("ε");
        
        if (firstSets.get(firstSymbol).contains("ε")) {
            if (sequence.size() > 1) {
                result.addAll(computeFirstOfSequence(sequence.subList(1, sequence.size())));
            } else {
                result.add("ε");
            }
        }
        
        return result;
    }

    private void computeFollowSets() {
        boolean changed;
        do {
            changed = false;
            for (Map.Entry<String, List<List<String>>> entry : grammar.getProductions().entrySet()) {
                String A = entry.getKey();
                for (List<String> production : entry.getValue()) {
                    for (int i = 0; i < production.size(); i++) {
                        String B = production.get(i);
                        // Só trabalha com não terminais
                        if (grammar.isTerminal(B)) continue;
                        
                        // Seja beta o que vem após B na produção
                        if (i < production.size() - 1) {
                            List<String> beta = production.subList(i+1, production.size());
                            Set<String> firstOfBeta = computeFirstOfSequence(beta);
                            
                            // Adiciona First(beta) - {ε} a Follow(B)
                            Set<String> firstMinusEpsilon = new HashSet<>(firstOfBeta);
                            firstMinusEpsilon.remove("ε");
                            if (followSets.get(B).addAll(firstMinusEpsilon)) {
                                changed = true;
                            }
                            
                            // Se First(beta) contém ε, adiciona Follow(A) a Follow(B)
                            if (firstOfBeta.contains("ε")) {
                                if (followSets.get(B).addAll(followSets.get(A))) {
                                    changed = true;
                                }
                            }
                        } else {
                            // Se B é o último símbolo, adiciona Follow(A) a Follow(B)
                            if (followSets.get(B).addAll(followSets.get(A))) {
                                changed = true;
                            }
                        }
                    }
                }
            }
        } while (changed);
    }

    public Map<String, Set<String>> getFirstSets() {
        return firstSets;
    }

    public Map<String, Set<String>> getFollowSets() {
        return followSets;
    }
    
    // Método de formatação para uso na web, em vez de System.out.println
    public String formatSets() {
        StringBuilder result = new StringBuilder();
        
        result.append("Conjuntos FIRST:\n");
        for (String nonTerminal : firstSets.keySet()) {
            result.append(nonTerminal).append(" = ").append(firstSets.get(nonTerminal)).append("\n");
        }
        
        result.append("\nConjuntos FOLLOW:\n");
        for (String nonTerminal : followSets.keySet()) {
            result.append(nonTerminal).append(" = ").append(followSets.get(nonTerminal)).append("\n");
        }
        
        return result.toString();
    }
}
