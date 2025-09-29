package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.User;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private IncentiveService incentiveService;

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        try {
            // Find sender and recipient by ID
            Optional<User> senderOpt = userRepository.findById(transaction.getSenderId());
            Optional<User> recipientOpt = userRepository.findById(transaction.getRecipientId());

            // Validate both users exist
            if (!senderOpt.isPresent() || !recipientOpt.isPresent()) {
                System.out.println("Invalid transaction: User not found");
                return false;
            }

            User sender = senderOpt.get();
            User recipient = recipientOpt.get();
            BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());

            // Validate sender has sufficient balance
            if (sender.getBalance().compareTo(amount) < 0) {
                System.out.println("Invalid transaction: Insufficient balance");
                return false;
            }

            // Get incentive amount from API
            BigDecimal incentiveAmount = incentiveService.getIncentiveAmount(transaction);

            // Process transaction: update balances
            sender.setBalance(sender.getBalance().subtract(amount));
            // Add both transaction amount AND incentive to recipient
            recipient.setBalance(recipient.getBalance().add(amount).add(incentiveAmount));

            // Save updated users
            userRepository.save(sender);
            userRepository.save(recipient);

            // Record transaction with incentive
            TransactionRecord record = new TransactionRecord(sender, recipient, amount, incentiveAmount);
            transactionRecordRepository.save(record);

            System.out.println("Transaction processed: " + sender.getUsername() + " -> " + recipient.getUsername() +
                    " Amount: " + amount + " Incentive: " + incentiveAmount);
            return true;

        } catch (Exception e) {
            System.err.println("Transaction processing failed: " + e.getMessage());
            return false;
        }
    }
}
