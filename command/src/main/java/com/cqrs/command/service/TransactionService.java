package com.cqrs.command.service;

import com.cqrs.command.dto.*;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    CompletableFuture<String> createHolder(HolderDTO holderDTO);
    CompletableFuture<String> createAccount(AccountDTO accountDTO);
    CompletableFuture<String> depositMoney(DepositDTO transactionDTO);
    CompletableFuture<String> withdrawMoney(WithdrawalDTO transactionDTO);
    String transferMoney(TransferDTO transferDTO);
}
