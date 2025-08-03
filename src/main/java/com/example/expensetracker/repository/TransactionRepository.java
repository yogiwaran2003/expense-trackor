package com.example.expensetracker.repository;
import com.example.expensetracker.model.Transaction;
import com.example.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}
