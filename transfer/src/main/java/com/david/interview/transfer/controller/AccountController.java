package com.david.interview.transfer.controller;

import com.david.interview.transfer.response.Result;
import com.david.interview.transfer.service.AccountService;
import com.david.interview.transfer.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//账户接口
@RestController
@RequestMapping(value = "/service")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/v2/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Result<List<Account>>> listAccount() {
        List<Account> accounts = accountService.listAccount();
        return ResponseEntity.ok(new Result<>(accounts));
    }
}
