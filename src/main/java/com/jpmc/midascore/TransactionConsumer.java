package com.jpmc.midascore;

import com.jpmc.midascore.component.TransactionService;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.User;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class TransactionConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);
    private int transactionCount = 0;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-group")
    public void consume(Transaction transaction) {
        transactionCount++;
        logger.info("=== TRANSACTION #{} ===", transactionCount);
        logger.info("From: {} To: {} Amount: {}",
                transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount());

        // Process the transaction through the service
        boolean processed = transactionService.processTransaction(transaction);
        logger.info("Transaction processed: {}", processed);

        // Print wilbur's balance after each transaction
        printWilburBalance();
    }

    private void printWilburBalance() {
        Optional<User> wilbur = userRepository.findByUsername("wilbur");
        if (wilbur.isPresent()) {
            BigDecimal balance = wilbur.get().getBalance();
            logger.info("🎯 WILBUR CURRENT BALANCE: {}", balance);
            logger.info("🎯 WILBUR BALANCE (ROUNDED DOWN): {}", balance.intValue());
        } else {
            logger.info("*** WILBUR USER NOT FOUND ***");
        }
    }
}
