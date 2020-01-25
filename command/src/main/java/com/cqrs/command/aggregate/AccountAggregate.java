package com.cqrs.command.aggregate;


import com.cqrs.command.command.*;
import com.cqrs.command.event.DepositCompletedEvent;
import com.cqrs.event.AccountCreationEvent;
import com.cqrs.event.DepositMoneyEvent;
import com.cqrs.event.WithdrawMoneyEvent;
import com.cqrs.event.transfer.MoneyTransferEvent;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;


import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Aggregate
@EqualsAndHashCode
public class AccountAggregate {
    @AggregateIdentifier
    private String accountID;
    private String holderID;
    private Long balance;

    @CommandHandler
    public AccountAggregate(AccountCreationCommand command) {
        log.debug("handling {}", command);
        apply(new AccountCreationEvent(command.getHolderID(), command.getAccountID()));
    }

    @EventSourcingHandler
    protected void createAccount(AccountCreationEvent event) {
        log.debug("applying {}", event);
        this.accountID = event.getAccountID();
        this.holderID = event.getHolderID();
        this.balance = 0L;
    }

    @CommandHandler
    protected void depositMoney(DepositMoneyCommand command) {
        log.debug("handling {}", command);
        if (command.getAmount() <= 0) throw new IllegalStateException("amount >= 0");
        apply(new DepositMoneyEvent(command.getHolderID(), command.getAccountID(), command.getAmount()));
    }

    @EventSourcingHandler
    protected void depositMoney(DepositMoneyEvent event) {
        log.debug("applying {}", event);
        this.balance += event.getAmount();
        log.debug("balance {}", this.balance);
    }

    @CommandHandler
    protected void withdrawMoney(WithdrawMoneyCommand command) {
        log.debug("handling {}", command);
        if (this.balance - command.getAmount() < 0) throw new IllegalStateException("잔고가 부족합니다.");
        else if (command.getAmount() <= 0) throw new IllegalStateException("amount >= 0");
        apply(new WithdrawMoneyEvent(command.getHolderID(), command.getAccountID(), command.getAmount()));
    }

    @EventSourcingHandler
    protected void withdrawMoney(WithdrawMoneyEvent event) {
        log.debug("applying {}", event);
        this.balance -= event.getAmount();
        log.debug("balance {}", this.balance);
    }

    @CommandHandler
    protected void transferMoney(MoneyTransferCommand command) {
        log.debug("handling {}", command);
        apply(MoneyTransferEvent.builder()
                .srcAccountID(command.getSrcAccountID())
                .dstAccountID(command.getDstAccountID())
                .amount(command.getAmount())
                .comamndFactory(command.getBankType().getCommandFactory(command))
                .transferID(command.getTransferID())
                .build());
    }

    @CommandHandler
    protected void transferMoney(TransferApprovedCommand command) {
        log.debug("handling {}", command);
        apply(new DepositMoneyEvent(this.holderID, command.getAccountID(), command.getAmount()));
        apply(new DepositCompletedEvent(command.getAccountID(), command.getTransferID()));
    }
}
