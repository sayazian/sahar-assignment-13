package com.coderscampus.assignment13.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class UserService {

    @Autowired//fix this
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;

    public List<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> findByNameAndUsername(String name, String username) {
        return userRepo.findByNameAndUsername(name, username);
    }

    public List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2) {
        return userRepo.findByCreatedDateBetween(date1, date2);
    }

    public User findExactlyOneUserByUsername(String username) {
        List<User> users = userRepo.findExactlyOneUserByUsername(username);
        if (users.size() > 0)
            return users.get(0);
        else
            return new User();
    }

    public Set<User> findAll() {
        return userRepo.findAllUsersWithAccountsAndAddresses();
    }

    public User findById(Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        return userOpt.orElse(new User());
    }

    public User saveUser(User user) {
        boolean isNewUser = (user.getUserId() == null);
        User managedUser;
        Address addressFromForm = user.getAddress();

        if (isNewUser) {
            managedUser = setNewUserAccounts(user);
        } else {
            managedUser = userRepo.findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setAccounts(managedUser.getAccounts());
        }
        updateUserInfoFromForm(user, managedUser);

        if (addressFromForm != null && addressService.isAddressEmpty(addressFromForm)) {
            managedUser.setAddress(null);
        } else if (addressFromForm != null) {
            if (managedUser.getAddress() == null) {
                Address newAddress = new Address();
                newAddress.setUser(managedUser);
                newAddress.setUserId(managedUser.getUserId());
                updateAddressFromForm(newAddress, addressFromForm);
                managedUser.setAddress(newAddress);
            } else {
                updateAddressFromForm(managedUser.getAddress(), addressFromForm);
            }
        }

        return userRepo.save(managedUser);
    }

    private static void updateUserInfoFromForm(User user, User managedUser) {
        managedUser.setUsername(user.getUsername());
        managedUser.setPassword(user.getPassword());
        managedUser.setName(user.getName());
    }

    private User setNewUserAccounts(User user) {
        Account checking = new Account();
        checking.setAccountName("Checking Account");
        checking.getUsers().add(user);
        Account savings = new Account();
        savings.setAccountName("Savings Account");
        savings.getUsers().add(user);
        user.getAccounts().add(checking);
        user.getAccounts().add(savings);
        accountRepo.save(checking);
        accountRepo.save(savings);
        return user;
    }

    private void updateAddressFromForm(Address newAddress, Address addressFromForm) {
        newAddress.setAddressLine1(addressFromForm.getAddressLine1());
        newAddress.setAddressLine2(addressFromForm.getAddressLine2());
        newAddress.setCity(addressFromForm.getCity());
        newAddress.setRegion(addressFromForm.getRegion());
        newAddress.setCountry(addressFromForm.getCountry());
        newAddress.setZipCode(addressFromForm.getZipCode());
    }

    public void delete(Long userId) {
        userRepo.deleteById(userId);
    }
}
