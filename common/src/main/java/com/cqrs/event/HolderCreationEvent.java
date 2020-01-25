package com.cqrs.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.serialization.Revision;

@AllArgsConstructor
@ToString
@Getter
@Revision("1.0")
public class HolderCreationEvent {
    private String holderID;
    private String holderName;
    private String tel;
    private String address;
    private String company;
}
