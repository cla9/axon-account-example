package com.cqrs.event.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class CompletedCancelTransferEvent {
    private String srcAccountID;
    private String dstAccountID;
    private Long amount;
    private String transferID;
}
