package com.cqrs.command.command;


import com.cqrs.command.dto.TransferDTO;
import com.cqrs.command.transfer.*;
import com.cqrs.command.transfer.factory.TransferComamndFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;
import java.util.function.Function;

@Builder
@ToString
@Getter
public class MoneyTransferCommand {
    private String srcAccountID;
    @TargetAggregateIdentifier
    private String dstAccountID;
    private Long amount;
    private String transferID;
    private BankType bankType;

    public enum BankType{
        JEJU(command -> new TransferComamndFactory(new JejuBankTransferCommand(),new JejuBankCancelTransferCommand(), new JejuBankCompensationCancelCommand())),
        SEOUL(command -> new TransferComamndFactory(new SeoulBankTransferCommand(), new SeoulBankCancelTransferCommand(), new SeoulBankCompensationCancelCommand()));

        private Function<MoneyTransferCommand, TransferComamndFactory> expression;
        BankType(Function<MoneyTransferCommand, TransferComamndFactory> expression){ this.expression = expression;}
        public TransferComamndFactory getCommandFactory(MoneyTransferCommand command){
            TransferComamndFactory factory = this.expression.apply(command);
            factory.create(command.getSrcAccountID(), command.getDstAccountID(), command.amount, command.getTransferID());
            return factory;
        }

    }

    public static MoneyTransferCommand of(TransferDTO dto){
        return MoneyTransferCommand.builder()
                .srcAccountID(dto.getSrcAccountID())
                .dstAccountID(dto.getDstAccountID())
                .amount(dto.getAmount())
                .bankType(dto.getBankType())
                .transferID(UUID.randomUUID().toString())
                .build();
    }
}
