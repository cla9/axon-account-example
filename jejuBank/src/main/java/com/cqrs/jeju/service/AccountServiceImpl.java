package com.cqrs.jeju.service;

import com.cqrs.jeju.command.AccountCreationCommand;
import com.cqrs.jeju.dto.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final CommandGateway commandGateway;

    @Override
    public String createAccount(AccountDTO accountDTO) {
        return commandGateway.sendAndWait(new AccountCreationCommand(accountDTO.getAccountID(), accountDTO.getBalance()));
    }
}
