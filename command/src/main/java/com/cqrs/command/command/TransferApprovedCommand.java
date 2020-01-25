package com.cqrs.command.command;


import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Getter
@Builder
public class TransferApprovedCommand {
    @TargetAggregateIdentifier
    private String accountID;
    private Long amount;
    private String transferID;
}
