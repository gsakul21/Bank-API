package dev.codescreen.service;

import org.springframework.stereotype.Service;
import dev.codescreen.repository.*;

@Service
public class BankLedgerService {
    private final TransactionEventRepository transactionEventRepository;

    public BankLedgerService(TransactionEventRepository transactionEventRepository)
    {
        this.transactionEventRepository = transactionEventRepository;
    }
}