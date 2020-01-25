package com.cqrs.event.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class TransferApprovedEvent {
    private String srcAccountID;
    private String dstAccountID;
    private String transferID;
    private Long amount;
}
