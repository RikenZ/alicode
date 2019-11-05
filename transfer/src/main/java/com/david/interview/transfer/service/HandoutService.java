package com.david.interview.transfer.service;


import com.david.interview.transfer.dto.ReceiveHandoutDTO;
import com.david.interview.transfer.dto.SendHandoutDTO;
import com.david.interview.transfer.model.Handout;

import java.math.BigDecimal;

public interface HandoutService {
    //发红包
    Handout sendHandout(SendHandoutDTO dto);

    //抢红包
    BigDecimal receiveHandout(ReceiveHandoutDTO dto);

    //超时退款
    void refund(String id);
}
