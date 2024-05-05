package dev.codescreen.domain;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private BigDecimal amount;

    private String timeOfEvent;

    public TransactionEvent() {}

    public TransactionEvent(String userId, TransactionStatus transactionStatus, String amount, String timeOfEvent)
    {
        this.userId = userId;
        this.transactionStatus = transactionStatus;
        this.amount = new BigDecimal(amount);
        this.timeOfEvent = timeOfEvent;
    }

    


}
