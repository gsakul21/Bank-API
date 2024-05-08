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

/*
 * This test suite focuses on integration and functionality with the 
 * JPARepository, which in this cases utilizes Hibernate and SQLite.
 */

/*
 * Configuring the properties of the testing environment, Spring Boot tests in
 * an embedded, in-memory database so need to use that for this option to work.
 * Thus, configuring H2 for testing.
 */
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

    /*
     * Before each database transaction test, runs this and inserts an event entry
     * into the database
     */

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

    /*
     * After the specific test, deletes the entry that was added before the
     * test from the database. Essentially cleaning up the database for the
     * next test.
     */

    @AfterEach
    public void clear()
    {
        transactionEventRepository.delete(transactionEvent);
    }

    /*
     * Checking the specific transaction event lookup functionality. Given
     * the messageId pertaining to a specific message, can we get the event related
     * to that image.
     */
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

    /*
     * Checking to make sure that we are able to add to the database without
     * any issues.
     */

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

    /*
     * Testing feature that retrieves all TransactionEvents associated with a
     * specific userId. Essentially the entire transaction history of a particular
     * user.
     */

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
