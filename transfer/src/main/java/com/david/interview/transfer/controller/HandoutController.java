package com.david.interview.transfer.controller;

import com.david.interview.transfer.dto.ReceiveHandoutDTO;
import com.david.interview.transfer.dto.SendHandoutDTO;
import com.david.interview.transfer.log.SysLog;
import com.david.interview.transfer.model.Account;
import com.david.interview.transfer.model.Handout;
import com.david.interview.transfer.response.Result;
import com.david.interview.transfer.service.AccountService;
import com.david.interview.transfer.service.HandoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

//红包接口
@RestController
@RequestMapping(value = "/service")
public class HandoutController {

    @Autowired
    private HandoutService handoutService;

    @SysLog("发红包")
    @PutMapping(value = "/v2/send-handout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Result<Handout>> listAccount(@RequestBody SendHandoutDTO dto) {
        Handout handout = handoutService.sendHandout(dto);
        return ResponseEntity.ok(new Result<>(handout));
    }

    @SysLog("抢红包")
    @PutMapping(value = "/v2/receive-handout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Result<String>> receiveHandout(@RequestBody ReceiveHandoutDTO dto) {
        BigDecimal b = handoutService.receiveHandout(dto);
        return ResponseEntity.ok(new Result<>(String.format("抢到红包:%s", b)));
    }
}
