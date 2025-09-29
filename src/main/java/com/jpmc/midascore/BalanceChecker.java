package com.jpmc.midascore;

import com.jpmc.midascore.foundation.User;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

// @Component
public class BalanceChecker implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Wait a bit for transactions to process
        Thread.sleep(10000);

        // Check wilbur's balance
        Optional<User> wilbur = userRepository.findByUsername("wilbur");
        if (wilbur.isPresent()) {
            System.out.println("*** FINAL WILBUR BALANCE: " + wilbur.get().getBalance() + " ***");
            System.out.println("*** WILBUR BALANCE (ROUNDED DOWN): " + wilbur.get().getBalance().intValue() + " ***");
        } else {
            System.out.println("*** WILBUR NOT FOUND ***");
        }

        // Print all users for debugging
        System.out.println("=== ALL USERS ===");
        userRepository.findAll().forEach(user ->
                System.out.println("ID: " + user.getId() + ", Username: " + user.getUsername() + ", Balance: " + user.getBalance())
        );
    }
}
