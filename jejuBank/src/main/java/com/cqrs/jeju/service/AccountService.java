package com.cqrs.jeju.service;

import com.cqrs.jeju.dto.AccountDTO;

public interface AccountService {
    String createAccount(AccountDTO accountDTO);
}
