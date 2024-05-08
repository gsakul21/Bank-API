package dev.codescreen.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dev.codescreen.domain.TransactionEvent;
import dev.codescreen.domain.TransactionStatus;
import dev.codescreen.schemas.DebitCredit;


@DataJpaTest(
    properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
    }
)
public class TransactionEventRepositoryTest {

    @Autowired
    private TransactionEventRepository transactionEventRepository;

    private TransactionEvent transactionEvent;

    @BeforeEach
    public void setUp()
    {
        transactionEvent = new TransactionEvent(
            "a",
            "b",
            TransactionStatus.AUTH_SUCCESS,
            DebitCredit.DEBIT,
            "USD",
            "500",
            Instant.now().toString()
        );

        transactionEventRepository.saveAndFlush(transactionEvent);
    }

    @AfterEach
    public void clear()
    {
        transactionEventRepository.delete(transactionEvent);
    }

    @Test
    void findTransactionByUniqueId()
    {
        TransactionEvent loaded = transactionEventRepository.findById(transactionEvent.getMessageId()).orElse(null);

        assertNotNull(loaded);
        assertEquals(transactionEvent.getUserId(), loaded.getUserId());
        assertEquals(transactionEvent.getMessageId(), loaded.getMessageId());
        assertEquals(transactionEvent.getTransactionStatus(), loaded.getTransactionStatus());
        assertEquals(transactionEvent.getDebitOrCredit(), loaded.getDebitOrCredit());
        assertEquals(transactionEvent.getCurrency(), loaded.getCurrency());
        assertEquals(transactionEvent.getAmount(), loaded.getAmount());
        assertEquals(transactionEvent.getTimeOfEvent(), loaded.getTimeOfEvent());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void addTransactionToDatabase()
    {
        TransactionEvent transactionEventNumTwo = new TransactionEvent(
            "a",
            "c",
            TransactionStatus.AUTH_SUCCESS,
            DebitCredit.DEBIT,
            "USD",
            "500",
            Instant.now().toString()
        );

        transactionEventRepository.saveAndFlush(transactionEventNumTwo);
    }

    @Test
    void allTransactionsForUser()
    {
        String targetUserId = transactionEvent.getUserId();
        List<TransactionEvent> list = transactionEventRepository.findByUserId(targetUserId);

        for (TransactionEvent event : list)
        {
            assertEquals(event.getUserId(), targetUserId);
        }
    }

}
