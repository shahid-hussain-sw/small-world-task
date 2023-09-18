package com.smallworld.service;

import com.smallworld.data.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionDataFetcher {


    private final List<Transaction> transactions;

    @Autowired
    public TransactionDataFetcher(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return transactions.stream()
                .filter(transaction -> transaction.getSenderFullName().equals(senderFullName))
                .mapToDouble(Transaction::getAmount).sum();

    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        return transactions.stream().mapToDouble(Transaction::getAmount).max().orElse(0.0);
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        return transactions.stream().map(Transaction::getSenderFullName).distinct().count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
                .filter(transaction -> transaction.getSenderFullName().equals(clientFullName)
                        || transaction.getBeneficiaryFullName().equals(clientFullName))
                .anyMatch(transaction -> !ObjectUtils.isEmpty(transaction.getIssueId()) && !transaction.getIssueSolved());

    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        return transactions.stream()
                .collect(Collectors.toMap(Transaction::getBeneficiaryFullName, transaction -> transaction,
                        (existingTransaction, newTransaction) -> existingTransaction));

    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Long> getUnsolvedIssueIds() {
        return transactions.stream()
                .filter(transaction -> !ObjectUtils.isEmpty(transaction.getIssueId()) && !transaction.getIssueSolved())
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return transactions.stream()
                .filter(Transaction::getIssueSolved)
                .map(Transaction::getIssueMessage).collect(Collectors.toList());
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3).collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        Map<String, Double> senderTotalAmounts = getSenderTotalAmounts();

        Optional<Map.Entry<String, Double>> topSenderEntry = senderTotalAmounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        return topSenderEntry.map(Map.Entry::getKey);
    }

    private Map<String, Double> getSenderTotalAmounts() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(Transaction::getAmount)));
    }
}
