package com.cqrs.command.dto;

import com.cqrs.command.command.MoneyTransferCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private String srcAccountID;
    private String dstAccountID;
    private Long amount;
    private MoneyTransferCommand.BankType bankType;
}
