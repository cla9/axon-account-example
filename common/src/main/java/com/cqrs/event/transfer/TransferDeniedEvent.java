package com.cqrs.event.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TransferDeniedEvent {
    private String srcAccountID;
    private String dstAccountID;
    private String transferID;
    private Long amount;
    private String description;
}
