package com.david.interview.transfer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.david.interview.transfer.dao.AccountDao;
import com.david.interview.transfer.model.Account;
import com.david.interview.transfer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @PostConstruct
    public void init() {
        // 初始化从账户文件中获取账户信息
        BufferedReader bufferedReader;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            File file = new File(this.getClass().getResource("/").getPath() + "accounts.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp);
            }
            List<Account> accountList = JSON.parseObject(stringBuffer.toString(), new TypeReference<List<Account>>() {
            });
            for (Account account : accountList) {
                accountDao.create(account);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Account> listAccount() {
        return accountDao.listAccount();
    }
}
