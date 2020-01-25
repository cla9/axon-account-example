package com.cqrs.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HolderDTO {
    private String holderName;
    private String tel;
    private String address;
    private String company;
}
