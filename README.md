# Analisador Sintático Recursivo Descendente

Um analisador léxico e sintático para expressões matemáticas e atribuições, implementado em Java com Spring Boot. O sistema utiliza a técnica de análise recursiva descendente para validar e processar expressões como `id = id + (id * id)`.

## Funcionalidades

- Análise léxica de tokens (identificadores, operadores, etc.)
- Análise sintática com parser recursivo descendente
- Cálculo de conjuntos FIRST e FOLLOW para não-terminais da gramática
- Geração de árvore sintática abstrata (AST)
- Interface web para entrada e visualização dos resultados

## Tecnologias Utilizadas

- Java
- Spring Boot
- Thymeleaf (templates HTML)
- Materialize CSS (interface gráfica)

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/fesa/ec7/analisadorlexico_ec7/
│   │   ├── controller/
│   │   │   └── HomeController.java
│   │   ├── model/
│   │   │   ├── ASTNode.java
│   │   │   ├── FirstFollowCalculator.java
│   │   │   ├── Grammar.java
│   │   │   ├── Lexer.java
│   │   │   ├── RecursiveDescentParser.java
│   │   │   ├── Symbol.java
│   │   │   └── Token.java
│   │   └── AnalisadorlexicoEc7Application.java
│   └── resources/
│       ├── templates/
│       │   ├── index.html
│       │   └── result.html
│       └── application.properties
└── test/
└── java/com/fesa/ec7/analisadorlexico_ec7/
└── AnalisadorlexicoEc7ApplicationTests.java
```

## Como Executar

1. Clone o repositório
2. Execute `./mvnw spring-boot:run` (Linux/Mac) ou `mvnw.cmd spring-boot:run` (Windows)
3. Acesse `http://localhost:8080/parser/` no navegador

## Gramática Utilizada

O sistema implementa uma gramática para expressões com atribuições e operações aritméticas:
```
S -> id = E
E -> T E'
E' -> + T E' | - T E' | ε
T -> F T'
T' -> * F T' | / F T' | ε
F -> ( E ) | id
```

## Exemplos de Uso

- Expressão válida: `id = id + (id * id)`
- Expressão válida: `id = id + id / id - id`
- Expressão inválida: `id id + id` (falta operador de atribuição)
