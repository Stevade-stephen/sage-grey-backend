package io.sagegrey.backend.controllers;

import io.sagegrey.backend.dtos.AccountDto;
import io.sagegrey.backend.dtos.UserDto;
import io.sagegrey.backend.models.User;
import io.sagegrey.backend.services.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

   private final UserServiceInterface userService;


    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @PostMapping("/users/{userId}/accounts")
    public ResponseEntity<String> createAccount(@PathVariable Long userId, @Valid @RequestBody AccountDto account) {
        return userService.createAccount(userId, account);
    }

    @PostMapping("/users/transfers")
    public ResponseEntity<String> transferFunds(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam BigDecimal amount) {
       return userService.transferFunds(fromAccountId, toAccountId, amount);
    }

    @PostMapping("/users/withdrawals")
    public ResponseEntity<String> withdrawFunds(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        return userService.withdrawFunds(accountId, amount);
    }

    @PostMapping("/users/fund-account")
    public ResponseEntity<String> fundAccount(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        return userService.fundAccount(accountId, amount);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }
}
