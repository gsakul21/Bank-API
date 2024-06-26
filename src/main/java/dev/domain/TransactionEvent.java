package dev.domain;

import java.math.BigDecimal;

import dev.schemas.DebitCredit;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

/*
 * This is the class that defines an event for the service, as per the
 * event sourcing pattern. The class will store relevant information for
 * what is being denoted a "Transaction", this is essentially any load request
 * or authorization request to an account. It stores information relevant to the
 * event such as timestamp, involved User, as well as actually amount details (
 * currency, amount, type).
 */

@Entity
public class TransactionEvent {



    /*
     * Message ID passed in with request, since it is unique to the message and
     * every event is essentialy associated with one message, this will be the
     * unique identifier of an event.
     */
    @Id
    private String messageId;

    // The involved user for the transaction event
    private String userId;

    /*
     * Custom Enum to denote and keep track of what actually happened with the
     * load or authorization call. See {@link TransactionStatus} for more
     * information.
     * Stored as string in database.
     */
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    /*
     * As specified in schema, representing if this was a credit or debit
     * transaction. See {@link DebitCredit} for more information.
     * Stored as string in database.
     */
    @Enumerated(EnumType.STRING)
    private DebitCredit debitOrCredit;

    /*
     * Specific details to the transaction, retrieved from Amount object. See
     * {@link Amount} for more information.
     */

    private String currency;

    private BigDecimal amount;

    private String timeOfEvent;

    /*
     * Default constructor required for Entity objects and actual constructor
     * used in {@link BankLedgerService}.
     */

    public TransactionEvent() {}

    public TransactionEvent(String userId, String messageId, TransactionStatus transactionStatus, DebitCredit debitOrCredit, String currency, String amount, String timeOfEvent)
    {
        this.userId = userId;
        this.messageId = messageId;
        this.transactionStatus = transactionStatus;
        this.debitOrCredit = debitOrCredit;
        this.currency = currency;
        this.amount = new BigDecimal(amount);
        this.timeOfEvent = timeOfEvent;
    }

    // Getters and setters for various fields of the event object.

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTimeOfEvent() {
        return timeOfEvent;
    }

    public void setTimeOfEvent(String timeOfEvent) {
        this.timeOfEvent = timeOfEvent;
    }

    public DebitCredit getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(DebitCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    

    

}
