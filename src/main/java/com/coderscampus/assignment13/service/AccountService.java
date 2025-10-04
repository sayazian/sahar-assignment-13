package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElse(new Account());
    }

    public Account saveAccount(Account account) {
        return  accountRepository.save(account);
    }
}
