package com.fesa.ec7.analisadorlexico_ec7.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fesa.ec7.analisadorlexico_ec7.model.Grammar;
import com.fesa.ec7.analisadorlexico_ec7.model.Lexer;
import com.fesa.ec7.analisadorlexico_ec7.model.ASTNode;
import com.fesa.ec7.analisadorlexico_ec7.model.RecursiveDescentParser;
import com.fesa.ec7.analisadorlexico_ec7.model.FirstFollowCalculator;

@Controller
@RequestMapping("/parser")
public class HomeController {

    private final Grammar grammar;

    public HomeController() {
        this.grammar = Grammar.createAssignmentGrammar();
    }

    @GetMapping("/")
    public String home(Model model) {
        FirstFollowCalculator calculator = new FirstFollowCalculator(grammar);
        model.addAttribute("firstSets", calculator.getFirstSets());
        model.addAttribute("followSets", calculator.getFollowSets());
        return "index";
    }

    @PostMapping("/parse")
    public String parseExpression(@RequestParam String expression, Model model) {
        try {
            Lexer lexer = new Lexer(expression);
            RecursiveDescentParser parser = new RecursiveDescentParser(lexer, grammar);
            ASTNode ast = parser.parse();
            
            FirstFollowCalculator calculator = new FirstFollowCalculator(grammar);
            Map<String, Set<String>> firstSets = calculator.getFirstSets();
            Map<String, Set<String>> followSets = calculator.getFollowSets();
            
            model.addAttribute("expression", expression);
            model.addAttribute("ast", ast.toString());
            model.addAttribute("isValid", true);
            model.addAttribute("firstSets", firstSets);
            model.addAttribute("followSets", followSets);
        } catch (Exception e) {
            model.addAttribute("expression", expression);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("isValid", false);
        }
        return "result";
    }
}
