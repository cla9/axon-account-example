package com.cqrs.command.service;

import com.cqrs.command.command.*;
import com.cqrs.command.dto.*;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CommandGateway commandGateway;

    @Override
    public CompletableFuture<String> createHolder(HolderDTO holderDTO) {
        return commandGateway.send(new HolderCreationCommand(UUID.randomUUID().toString()
                , holderDTO.getHolderName()
                , holderDTO.getTel()
                , holderDTO.getAddress()
                , holderDTO.getCompany())
                );
    }

    @Override
    public CompletableFuture<String> createAccount(AccountDTO accountDTO) {
        return commandGateway.send(new AccountCreationCommand(UUID.randomUUID().toString(),accountDTO.getHolderID()));
    }

    @Override
    public CompletableFuture<String> depositMoney(DepositDTO transactionDTO) {
        return commandGateway.send(new DepositMoneyCommand(transactionDTO.getAccountID(), transactionDTO.getHolderID(), transactionDTO.getAmount()));
    }

    @Override
    public CompletableFuture<String> withdrawMoney(WithdrawalDTO transactionDTO) {
        return commandGateway.send(new WithdrawMoneyCommand(transactionDTO.getAccountID(), transactionDTO.getHolderID(), transactionDTO.getAmount()));
    }

    @Override
    public String transferMoney(TransferDTO transferDTO) {
        return commandGateway.sendAndWait(MoneyTransferCommand.of(transferDTO));
    }
}
