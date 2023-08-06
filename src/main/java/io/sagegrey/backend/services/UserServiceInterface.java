package io.sagegrey.backend.services;

import io.sagegrey.backend.dtos.AccountDto;
import io.sagegrey.backend.dtos.UserDto;
import io.sagegrey.backend.models.User;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface UserServiceInterface {
    ResponseEntity<String> createUser (UserDto userDto);
    ResponseEntity<String> createAccount (Long id, AccountDto accountDto);
    ResponseEntity<String> transferFunds (Long fromAccountId, Long toAccountId, BigDecimal amount);
    ResponseEntity<String> withdrawFunds (Long accountId, BigDecimal amount);
    ResponseEntity<String> fundAccount (Long accountId, BigDecimal amount);
    ResponseEntity<List<User>> getAllUsers();
}
