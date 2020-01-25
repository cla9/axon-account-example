package com.cqrs.jeju.command;


import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class AccountCreationCommand {
    @TargetAggregateIdentifier
    private String accountID;
    private Long balance;
}
