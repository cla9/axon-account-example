package com.cqrs.jeju.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public class AccountCreationEvent {
    private final String accountID;
    private final Long balance;
}
