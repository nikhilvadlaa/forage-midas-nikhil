package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.context.annotation.Import;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
@Import(TestKafkaProducerConfig.class)
class TaskTwoTests {
    static final Logger logger = LoggerFactory.getLogger(TaskTwoTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_two_verifier() throws InterruptedException {
        Thread.sleep(3000);

        String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");

        // Split the big string into individual lines
        String allData = String.join("", transactionLines);
        String[] individualLines = allData.split("\\r?\\n");

        // Send first 4 transactions
        for (int i = 0; i < Math.min(4, individualLines.length); i++) {
            String line = individualLines[i].trim();
            if (!line.isEmpty()) {
                kafkaProducer.send(line);
                Thread.sleep(1000); // Wait between sends
            }
        }

        Thread.sleep(5000);

        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("use your debugger to watch for incoming transactions");
        logger.info("kill this test once you find the answer");

        // Instead of infinite loop, let's run for a reasonable time
        for (int i = 0; i < 10; i++) {
            Thread.sleep(5000);
            logger.info("waiting for transactions... " + i);
        }
    }
}
