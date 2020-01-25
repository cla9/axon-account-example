package com.cqrs.command.aggregate;

import com.cqrs.command.command.HolderCreationCommand;
import com.cqrs.event.HolderCreationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@AllArgsConstructor
@NoArgsConstructor
@Aggregate
@Slf4j
public class HolderAggregate {
    @AggregateIdentifier
    @Getter
    private String holderID;
    private String holderName;
    private String tel;
    private String address;


    @CommandHandler
    public HolderAggregate(HolderCreationCommand command) {
        log.debug("handling {}", command);
        apply(new HolderCreationEvent(command.getHolderID(), command.getHolderName(), command.getTel(), command.getAddress(), command.getCompany()));
    }

    @EventSourcingHandler
    protected void createHolder(HolderCreationEvent event){
        log.debug("applying {}", event);
        this.holderID = event.getHolderID();
        this.holderName = event.getHolderName();
        this.tel = event.getTel();
        this.address = event.getAddress();
    }
}
