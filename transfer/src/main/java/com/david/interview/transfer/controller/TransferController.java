package com.david.interview.transfer.controller;


import com.david.interview.transfer.log.SysLog;
import com.david.interview.transfer.response.Result;
import com.david.interview.transfer.service.TransferService;
import com.david.interview.transfer.dto.TransferRequestParamDTO;
import com.david.interview.transfer.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/service")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @SysLog("转账")
    @ResponseBody
    @PutMapping(value = "/v2/transfer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Result> transfer(@Valid @RequestBody TransferRequestParamDTO transferParamDTO) {
        Transfer transfer = transferService.transfer(transferParamDTO);
        return ResponseEntity.ok(new Result<>(transfer));
    }

}
