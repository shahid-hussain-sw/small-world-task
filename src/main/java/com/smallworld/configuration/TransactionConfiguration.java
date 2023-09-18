package com.smallworld.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class TransactionConfiguration {

    @Bean
    public List<Transaction> transactions(){
       return loadTransactionsFromJson();
    }

    private List<Transaction> loadTransactionsFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = new String(Files.readAllBytes(Paths.get("transactions.json")));
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load transactions from JSON file", e);
        }
    }
}
