package com.cqrs.jeju.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class AccountDTO {
    private String accountID;
    private Long balance;
}
