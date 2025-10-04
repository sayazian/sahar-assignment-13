package com.coderscampus.assignment13.web;

import java.util.Set;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	private final UserService userService;
	private final AccountService accountService;

	public UserController(UserService userService, AccountService accountService) {
		this.userService = userService;
		this.accountService = accountService;
	}
	
	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		model.put("user", new User());
		return "register";
	}
	
	@PostMapping("/register")
	public String postCreateUser (User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		return "users";
	}
	
	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		model.put("user", user);
		return "user";
	}
	
	@PostMapping("/users/{userId}")
	public String postOneUser (User user) {
		userService.saveUser(user);
		return "redirect:/users/" + user.getUserId();
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getOneAccount (ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
		Account account = accountService.findById(accountId);
		model.put("account", account);
		return "account";
	}

	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String postOneAccount (@PathVariable Long userId, Account account) {
		accountService.saveAccount(account);
		return "redirect:/users/" + userId + "/accounts/" + account.getAccountId();
	}

	@PostMapping("/users/{userId}/newAccount")
	public String createOneAccount (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		Account account = new Account();
		user.getAccounts().add(account);
		account.getUsers().add(user);
		accountService.saveAccount(account);
		model.put("account", account);
		return "redirect:/users/" + user.getUserId() + "/accounts/" + account.getAccountId();
	}

	@GetMapping("/users/{userId}/delete/")
	public String deleteUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "users";
	}

}
