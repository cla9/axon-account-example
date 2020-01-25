package com.cqrs.query.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class AccountQuery {
    private String holderId;
}
