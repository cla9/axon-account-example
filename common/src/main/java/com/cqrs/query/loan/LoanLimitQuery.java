package com.cqrs.query.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class LoanLimitQuery {
    private String holderID;
    private Long balance;
}
