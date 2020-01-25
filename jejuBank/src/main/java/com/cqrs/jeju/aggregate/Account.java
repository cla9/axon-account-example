package com.cqrs.jeju.aggregate;

import com.cqrs.command.transfer.JejuBankCancelTransferCommand;
import com.cqrs.command.transfer.JejuBankCompensationCancelCommand;
import com.cqrs.command.transfer.JejuBankTransferCommand;
import com.cqrs.event.transfer.CompletedCancelTransferEvent;
import com.cqrs.event.transfer.CompletedCompensationCancelEvent;
import com.cqrs.event.transfer.TransferApprovedEvent;
import com.cqrs.event.transfer.TransferDeniedEvent;
import com.cqrs.jeju.command.AccountCreationCommand;
import com.cqrs.jeju.event.AccountCreationEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Entity
@Aggregate
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Account {
    @AggregateIdentifier
    @Id
    private String accountID;
    private Long balance;
    private final transient Random random = new Random();

    @CommandHandler
    public Account(AccountCreationCommand command) throws IllegalAccessException {
        log.debug("handling {}", command);
        if (command.getBalance() <= 0)
            throw new IllegalAccessException("유효하지 않은 입력입니다.");
        apply(new AccountCreationEvent(command.getAccountID(), command.getBalance()));
    }

    @EventSourcingHandler
    protected void on(AccountCreationEvent event) {
        log.debug("event {}", event);
        this.accountID = event.getAccountID();
        this.balance = event.getBalance();
    }

    @CommandHandler
    protected void on(JejuBankTransferCommand command) throws InterruptedException {
        if (random.nextBoolean())
            TimeUnit.SECONDS.sleep(15);

        log.debug("handling {}", command);
        if (this.balance < command.getAmount()) {
            apply(TransferDeniedEvent.builder()
                                        .srcAccountID(command.getSrcAccountID())
                                        .dstAccountID(command.getDstAccountID())
                                        .amount(command.getAmount())
                                        .description("잔고가 부족합니다.")
                                        .transferID(command.getTransferID())
                                     .build());
        } else {
            apply(TransferApprovedEvent.builder()
                                            .srcAccountID(command.getSrcAccountID())
                                            .dstAccountID(command.getDstAccountID())
                                            .transferID(command.getTransferID())
                                            .amount(command.getAmount())
                                        .build());
        }
    }

    @EventSourcingHandler
    protected void on(TransferApprovedEvent event) {
        log.debug("event {}", event);
        this.balance -= event.getAmount();
    }

    @CommandHandler
    protected void on(JejuBankCancelTransferCommand command) {
        log.debug("handling {}", command);
        apply(CompletedCancelTransferEvent.builder()
                                            .srcAccountID(command.getSrcAccountID())
                                            .dstAccountID(command.getDstAccountID())
                                            .transferID(command.getTransferID())
                                            .amount(command.getAmount())
                                          .build());
    }

    @EventSourcingHandler
    protected void on(CompletedCancelTransferEvent event) {
        log.debug("event {}", event);
        this.balance += event.getAmount();
    }

    @CommandHandler
    protected void on(JejuBankCompensationCancelCommand command) {
        log.debug("handling {}", command);
        apply(CompletedCompensationCancelEvent.builder()
                                                .srcAccountID(command.getSrcAccountID())
                                                .dstAccountID(command.getDstAccountID())
                                                .transferID(command.getTransferID())
                                                .amount(command.getAmount())
                                              .build());
    }

    @EventSourcingHandler
    protected void on(CompletedCompensationCancelEvent event) {
        log.debug("event {}", event);
        this.balance -= event.getAmount();
    }
}
