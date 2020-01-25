package com.cqrs.jeju.controller;

import com.cqrs.jeju.dto.AccountDTO;
import com.cqrs.jeju.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/account")
    public ResponseEntity<String> createAccount(@RequestBody AccountDTO accountDTO){
        return ResponseEntity.ok().body(accountService.createAccount(accountDTO));
    }
}
