package org.financk.financk_backend.budget.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @GetMapping
    public ResponseEntity<String> budget() {
        return ResponseEntity.ok("Budget");
    }
}
