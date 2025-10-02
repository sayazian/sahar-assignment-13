package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElse(new Account());
    }

    public Account saveAccount(Account account) {
        return  accountRepository.save(account);
    }
}
