package com.example.expensetracker.controller;

import com.example.expensetracker.model.Transaction;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.TransactionRepository;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
@Controller
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Transaction> transactions = transactionRepository.findByUser(user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("username", username);
        return "dashboard";
    }

    @GetMapping("/transaction/add")
    public String showAddTransactionForm() {
        return "add-transaction";
    }

    @PostMapping("/transaction/add")
    public String addTransaction(
            @RequestParam double amount,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam String type,
            @RequestParam String date,
            Model model) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username).orElseThrow();

            Transaction transaction = new Transaction();
            transaction.setAmount(BigDecimal.valueOf(amount));
            transaction.setDescription(description);
            transaction.setCategory(category);
            transaction.setType(type);
            transaction.setDate(LocalDate.parse(date));
            transaction.setUser(user);

            transactionRepository.save(transaction);
            return "redirect:/dashboard";
        } catch (DateTimeParseException e) {
            model.addAttribute("error", "Invalid date format. Please use YYYY-MM-DD format.");
            return "add-transaction";
        }
    }
}
