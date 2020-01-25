package com.cqrs.query.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Builder
public class LoanLimitResult {
    private String holderID;
    private String bankName;
    private Long balance;
    private Long loanLimit;
}
