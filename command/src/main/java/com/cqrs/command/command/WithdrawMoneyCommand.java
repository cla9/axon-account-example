package com.cqrs.command.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@ToString
@Getter
public class WithdrawMoneyCommand {
    @TargetAggregateIdentifier
    private String accountID;
    private String holderID;
    private Long amount;
}
