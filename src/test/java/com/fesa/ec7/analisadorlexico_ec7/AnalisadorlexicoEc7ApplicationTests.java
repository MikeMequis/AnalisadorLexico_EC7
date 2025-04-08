package com.fesa.ec7.analisadorlexico_ec7;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fesa.ec7.analisadorlexico_ec7.model.ASTNode;
import com.fesa.ec7.analisadorlexico_ec7.model.Grammar;
import com.fesa.ec7.analisadorlexico_ec7.model.Lexer;
import com.fesa.ec7.analisadorlexico_ec7.model.RecursiveDescentParser;

@SpringBootTest
class AnalisadorlexicoEc7ApplicationTests {

    private final Grammar grammar = Grammar.createAssignmentGrammar();

    @Test
    void contextLoads() {
    }

    @Test
    void expressoesValidas() {
        List<String> expressoes = Arrays.asList(
            "id = id",
            "id = id + id",
            "id = id * id - id",
            "id = (id + id) * id",
            "id = id / (id - id)",
            "id = (id * (id + id)) / id"
        );

        assertThat(expressoes).allMatch(expr -> {
            try {
                Lexer lexer = new Lexer(expr);
                RecursiveDescentParser parser = new RecursiveDescentParser(lexer, grammar);
                parser.parse();
                return true;
            } catch (Exception e) {
                System.out.println("Erro na expressão válida: " + expr + " | " + e.getMessage());
                return false;
            }
        }, "Todas as expressões deveriam ser válidas");
    }

    @Test
    void expressoesInvalidas() {
        List<String> expressoes = Arrays.asList(
            "id =",                 // Faltando expressão
            "id = + id",             // Operador sem operando
            "id = id *",             // Expressão incompleta
            "id = (id + id",        // Parêntese não fechado
            "id = id ++ id",        // Operador duplicado
            "id = id id",           // Faltando operador
            "id = * id",            // Operador sem operando esquerdo
            "= id + id",             // Faltando identificador
            "id = (id + )"           // Expressão vazia nos parênteses
        );

        assertThat(expressoes).allMatch(expr -> {
            try {
                Lexer lexer = new Lexer(expr);
                RecursiveDescentParser parser = new RecursiveDescentParser(lexer, grammar);
                parser.parse();
                System.out.println("Expressão inválida passou: " + expr);
                return false;
            } catch (Exception e) {
                System.out.println("Erro esperado em: " + expr + " | " + e.getMessage());
                return true;
            }
        }, "Todas as expressões deveriam ser inválidas");
    }

    @Test
    void atribuicoesComplexasValidas() {
        List<String> expressoes = Arrays.asList(
            "id = id + id * id - id",
            "id = (id + id) * (id - id)",
            "id = id / id + (id * id)",
            "id = ((id + id) * id) / id"
        );

        assertThat(expressoes).allMatch(expr -> {
            try {
                Lexer lexer = new Lexer(expr);
                RecursiveDescentParser parser = new RecursiveDescentParser(lexer, grammar);
                ASTNode ast = parser.parse();
                System.out.println("AST gerado para '" + expr + "':\n" + ast);
                return true;
            } catch (Exception e) {
                System.out.println("Erro inesperado em: " + expr + " | " + e.getMessage());
                return false;
            }
        }, "Expressões complexas deveriam ser válidas");
    }
}