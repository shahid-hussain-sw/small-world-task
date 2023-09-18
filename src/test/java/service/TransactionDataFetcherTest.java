package service;

import com.smallworld.data.Transaction;
import com.smallworld.service.TransactionDataFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDataFetcherTest {

    @InjectMocks
    TransactionDataFetcher transactionDataFetcher;
    @Mock
    private List<Transaction> transactions;

    @Test
    void testGetTotalTransactionAmount() {
        // given
        when(transactions.stream()).thenReturn(Stream.of(getTransaction(), getTransaction(), getTransaction()));
        // when
        double totalAmount = transactionDataFetcher.getTotalTransactionAmount();
        // then
        assertEquals(1290.6, totalAmount);
    }

    @Test
    void testGetTotalTransactionAmountWithEmptyList() {
        // given
        when(transactions.stream()).thenReturn(Stream.of(getTransaction(), getTransaction()));
        // when
        double totalAmount = transactionDataFetcher.getTotalTransactionAmount();
        // then
        assertEquals(860.4, totalAmount);
    }

    @Test
    void testGetMaxTransactionAmount() {
        // given
        Transaction transaction = getTransaction();
        transaction.setAmount(100.0);
        when(transactions.stream()).thenReturn(Stream.of(getTransaction(), transaction));
        // when
        double totalAmount = transactionDataFetcher.getMaxTransactionAmount();
        // then
        assertEquals(430.2, totalAmount);
    }

    @Test
    void testCountUniqueClients() {
        // given
        Transaction transaction = getTransaction();
        transaction.setSenderFullName("Test");
        when(transactions.stream()).thenReturn(Stream.of(getTransaction(), transaction));
        // when
        long uniqueClients = transactionDataFetcher.countUniqueClients();
        // then
        assertEquals(2, uniqueClients);
    }

    @Test
    void testHasOpenComplianceIssues() {
        // given
        when(transactions.stream()).thenReturn(Stream.of(getTransaction()));
        // when
        boolean openComplianceIssues = transactionDataFetcher.hasOpenComplianceIssues("Tom Shelby");
        // then
        assertTrue(openComplianceIssues);
    }

    @Test
    void testGetTransactionsByBeneficiaryName() {
        // given
        when(transactions.stream()).thenReturn(Stream.of(getTransaction()));
        // when
        Map<String, Transaction> transactions = transactionDataFetcher.getTransactionsByBeneficiaryName();
        // then
        Transaction transaction = transactions.get("Alfie Solomons");
        assertEquals(430.2, transaction.getAmount());
        assertEquals(33, transaction.getBeneficiaryAge());
        assertEquals(1L, transaction.getIssueId());
        assertEquals("Looks like money laundering", transaction.getIssueMessage());
        assertEquals(663458L, transaction.getMtn());
        assertEquals(22, transaction.getSenderAge());
        assertEquals("Tom Shelby", transaction.getSenderFullName());
        assertEquals(false, transaction.getIssueSolved());
    }

    @Test
    void testGetUnsolvedIssueIds() {
        // given
        Transaction transaction = getTransaction();
        transaction.setIssueSolved(true);
        transaction.setIssueId(2454L);
        when(transactions.stream()).thenReturn(Stream.of(getTransaction(), transaction));
        // when
        Set<Long> unsolvedIssueIds = transactionDataFetcher.getUnsolvedIssueIds();
        // then
        assertEquals(1, unsolvedIssueIds.size());

    }

    @Test
    void testGetAllSolvedIssueMessages() {
        // given
        Transaction transaction = getTransaction();
        transaction.setIssueSolved(true);
        transaction.setIssueId(2454L);
        when(transactions.stream()).thenReturn(Stream.of(getTransaction(), transaction));
        // when
        List<String> allSolvedIssueMessages = transactionDataFetcher.getAllSolvedIssueMessages();
        // then
        assertEquals(1, allSolvedIssueMessages.size());
        assertEquals("Looks like money laundering", allSolvedIssueMessages.get(0));

    }

    @Test
    void testGetTop3TransactionsByAmount() {
        // given
        Transaction transaction = getTransaction();
        transaction.setAmount(100.0);
        Transaction transaction1 = getTransaction();
        transaction.setAmount(200.0);
        Transaction transaction2 = getTransaction();
        transaction.setAmount(300.0);
        when(transactions.stream()).thenReturn(Stream.of(getTransaction(), transaction, transaction1, transaction2));
        // when
        List<Transaction> transactions = transactionDataFetcher.getTop3TransactionsByAmount();
        // then
        assertEquals(3, transactions.size());
        Transaction output = transactions.get(0);
        assertEquals(430.2, output.getAmount());
        assertEquals(33, output.getBeneficiaryAge());
        assertEquals(1L, output.getIssueId());
        assertEquals("Looks like money laundering", output.getIssueMessage());
        assertEquals(663458L, output.getMtn());
        assertEquals(22, output.getSenderAge());
        assertEquals("Tom Shelby", output.getSenderFullName());
        assertEquals(false, output.getIssueSolved());

    }


    @Test
    void testGetTopSender() {
        // given
        Transaction transaction = getTransaction();
        transaction.setAmount(100.0);
        Transaction transaction1 = getTransaction();
        transaction.setAmount(200.0);
        Transaction transaction2 = getTransaction();
        transaction.setAmount(300.0);
        when(transactions.stream()).thenReturn(Stream.of(getTransaction(), transaction, transaction1, transaction2));
        // when
        Optional<String> topSender = transactionDataFetcher.getTopSender();
        // then
        assertNotNull(topSender.get());
        assertEquals("Tom Shelby", topSender.get());

    }

    @Test
    void testGetTotalTransactionAmountSentBy() {
        //given
        when(transactions.stream()).thenReturn(Stream.of(getTransaction()));
        // when
        double totalAmount = transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        // then
        assertEquals(430.2, totalAmount); // Expecting total amount sent by Sender1
    }

    @Test
    void testGetTotalTransactionAmountSentByWithNoMatchingSender() {
        // given
        when(transactions.stream()).thenReturn(Stream.of(getTransaction()));
        // when
        double totalAmount = transactionDataFetcher.getTotalTransactionAmountSentBy("Sender");
        // then
        assertEquals(0.0, totalAmount);
    }


    private Transaction getTransaction() {
        return Transaction.builder()
                .amount(430.2)
                .beneficiaryAge(33)
                .beneficiaryFullName("Alfie Solomons")
                .issueId(1L)
                .issueMessage("Looks like money laundering")
                .issueSolved(false)
                .mtn(663458L)
                .senderAge(22)
                .senderFullName("Tom Shelby")
                .build();

    }

}
