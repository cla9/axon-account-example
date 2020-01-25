package com.cqrs.command.saga;

import com.cqrs.command.command.TransferApprovedCommand;
import com.cqrs.command.event.DepositCompletedEvent;
import com.cqrs.command.transfer.factory.TransferComamndFactory;
import com.cqrs.event.transfer.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
public class TransferManager {
    @Autowired
    private transient CommandGateway commandGateway;
    private boolean isExecutingCompensation = false;
    private boolean isAbortingCompensation = false;
    private TransferComamndFactory comamndFactory;

    @StartSaga
    @SagaEventHandler(associationProperty = "transferID")
    protected void on(MoneyTransferEvent event) {

        log.debug("Created saga instance");
        log.debug("event : {}", event);
        comamndFactory = event.getComamndFactory();
        SagaLifecycle.associateWith("srcAccountID", event.getSrcAccountID());

        try {
            log.info("계좌 이체 시작 : {} ", event);
            commandGateway.sendAndWait(comamndFactory.getTransferCommand(), 10, TimeUnit.SECONDS);
        } catch (CommandExecutionException e) {
            log.error("Failed transfer process. Start cancel transaction");
            cancelTransfer();
        }
    }

    private void cancelTransfer() {
        isExecutingCompensation = true;
        log.info("보상 트랜잭션 요청");
        commandGateway.send(comamndFactory.getAbortTransferCommand());
    }

    @SagaEventHandler(associationProperty = "srcAccountID")
    protected void on(CompletedCancelTransferEvent event) {
        isExecutingCompensation = false;
        if (!isAbortingCompensation) {
            log.info("계좌 이체 취소 완료 : {} ", event);
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "srcAccountID")
    protected void on(TransferDeniedEvent event) {
        log.info("계좌 이체 실패 : {}", event);
        log.info("실패 사유 : {}", event.getDescription());
        if(isExecutingCompensation){
            isAbortingCompensation = true;
            log.info("보상 트랜잭션 취소 요청 : {}", event);
            commandGateway.send(comamndFactory.getCompensationAbortCommand());
        }
        else {
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "srcAccountID")
    @EndSaga
    protected void on(CompletedCompensationCancelEvent event){
        isAbortingCompensation = false;
        log.info("보상 트랜잭션 취소 완료 : {}",event);
    }

    @SagaEventHandler(associationProperty = "srcAccountID")
    protected void on(TransferApprovedEvent event) {
        if (!isExecutingCompensation && !isAbortingCompensation) {
            log.info("이체 금액 {} 계좌 반영 요청 : {}",event.getAmount(), event);
            SagaLifecycle.associateWith("accountID", event.getDstAccountID());
            commandGateway.send(TransferApprovedCommand.builder()
                                                            .accountID(event.getDstAccountID())
                                                            .amount(event.getAmount())
                                                            .transferID(event.getTransferID())
                                                       .build());
        }
    }

    @SagaEventHandler(associationProperty = "accountID")
    @EndSaga
    protected void on(DepositCompletedEvent event){
        log.info("계좌 이체 성공 : {}", event);
    }
}
