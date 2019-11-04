package com.david.interview.transfer.dao;

import com.david.interview.transfer.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Repository
public class AccountDao {

    public static Map<String, Account> accountMap = new Hashtable<>();

    public Account create(Account account) {
        synchronized (this) {
            if (accountMap.get(account.getAccountNo()) != null) {
                throw new RuntimeException("账户已存在");
            }
            accountMap.put(account.getAccountNo(), account);
            return account;
        }
    }

    public Account update(Account account) {
        accountMap.put(account.getAccountNo(), account);
        return account;
    }

    public Account loadForUpdate(String accountNo) {
        return accountMap.get(accountNo);
    }

    public Account load(String accountNo) {
        return accountMap.get(accountNo);
    }

    public List<Account> listAccount() {
        return new ArrayList<>(accountMap.values());
    }
}
