package com.smallworld.controller;

import com.smallworld.data.Transaction;
import com.smallworld.service.TransactionDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionDataFetcher transactionDataFetcher;

    /**
     * Get the sum of the amounts of all transactions.
     */
    @GetMapping("/totalAmount")
    public double getTotalTransactionAmount() {
        return transactionDataFetcher.getTotalTransactionAmount();
    }

    /**
     * Get the sum of the amounts of all transactions sent by the specified client.
     */
    @GetMapping("/totalAmountSentBy")
    public double getTotalTransactionAmountSentBy(@RequestParam String senderName) {
        return transactionDataFetcher.getTotalTransactionAmountSentBy(senderName);
    }

    /**
     * Get the highest transaction amount.
     */
    @GetMapping("/maxAmount")
    public double getMaxTransactionAmount() {
        return transactionDataFetcher.getMaxTransactionAmount();
    }

    /**
     * Counts the number of unique clients that sent or received a transaction.
     */
    @GetMapping("/uniqueClientsCount")
    public long countUniqueClients() {
        return transactionDataFetcher.countUniqueClients();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved.
     */
    @GetMapping("/hasOpenComplianceIssues")
    public boolean hasOpenComplianceIssues(@RequestParam String clientFullName) {
        return transactionDataFetcher.hasOpenComplianceIssues(clientFullName);
    }


    /**
     * Returns all transactions indexed by beneficiary name.
     */
    @GetMapping("/transactionsByBeneficiaryName")
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        return transactionDataFetcher.getTransactionsByBeneficiaryName();
    }

    /**
     * Returns the identifiers of all open compliance issues.
     */
    @GetMapping("/unsolvedIssueIds")
    public Set<Long> getUnsolvedIssueIds() {
        return transactionDataFetcher.getUnsolvedIssueIds();
    }

    /**
     * Returns a list of all solved issue messages.
     */
    @GetMapping("/solvedIssueMessages")
    public List<String> getAllSolvedIssueMessages() {
        return transactionDataFetcher.getAllSolvedIssueMessages();
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending.
     */
    @GetMapping("/top3TransactionsByAmount")
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactionDataFetcher.getTop3TransactionsByAmount();
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount.
     */
    @GetMapping("/topSender")
    public Optional<String> getTopSender() {
        return transactionDataFetcher.getTopSender();
    }
}
