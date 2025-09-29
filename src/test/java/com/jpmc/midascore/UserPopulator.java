package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserPopulator {
    @Autowired
    private DatabaseConduit databaseConduit;

    public void populate() {
        // Create users - Note: wilbur is now included for Task 4
        User user1 = new User("user1", new BigDecimal("500.00"));
        User user2 = new User("user2", new BigDecimal("750.00"));
        User user3 = new User("waldorf", new BigDecimal("1000.00"));
        User user4 = new User("wilbur", new BigDecimal("800.00")); // wilbur for Task 4
        User user5 = new User("user5", new BigDecimal("300.00"));
        User user6 = new User("user6", new BigDecimal("800.00"));
        User user7 = new User("user7", new BigDecimal("450.00"));
        User user8 = new User("user8", new BigDecimal("350.00"));
        User user9 = new User("user9", new BigDecimal("900.00"));
        User user10 = new User("user10", new BigDecimal("200.00"));

        databaseConduit.save(user1);
        databaseConduit.save(user2);
        databaseConduit.save(user3);
        databaseConduit.save(user4); // wilbur
        databaseConduit.save(user5);
        databaseConduit.save(user6);
        databaseConduit.save(user7);
        databaseConduit.save(user8);
        databaseConduit.save(user9);
        databaseConduit.save(user10);

        System.out.println("Users populated - wilbur should be user ID 4");
    }
}
