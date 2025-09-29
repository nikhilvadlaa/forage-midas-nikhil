package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

@Service
public class IncentiveService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    public BigDecimal getIncentiveAmount(Transaction transaction) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Transaction> request = new HttpEntity<>(transaction, headers);

            Incentive incentive = restTemplate.postForObject(INCENTIVE_API_URL, request, Incentive.class);

            if (incentive != null && incentive.getAmount() != null) {
                System.out.println("Received incentive: " + incentive.getAmount());
                return incentive.getAmount();
            }

            return BigDecimal.ZERO;

        } catch (Exception e) {
            System.err.println("Failed to get incentive: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
