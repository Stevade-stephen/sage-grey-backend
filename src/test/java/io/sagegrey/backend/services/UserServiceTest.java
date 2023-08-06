package io.sagegrey.backend.services;
import io.sagegrey.backend.dtos.AccountDto;
import io.sagegrey.backend.dtos.UserDto;
import io.sagegrey.backend.models.Account;
import io.sagegrey.backend.models.User;
import io.sagegrey.backend.repositories.AccountRepository;
import io.sagegrey.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setPassword("testpassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        ResponseEntity<String> response = userService.createUser(userDto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User created successfully", response.getBody());
    }

    @Test
    void testCreateAccount() {
        Long userId = 1L;
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountType("Savings");

        User user = new User();
        user.setId(userId);
        user.setUserAccounts(new HashSet<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved entity

        ResponseEntity<String> response = userService.createAccount(userId, accountDto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Account created successfully", response.getBody());

        // Additional verification
        assertEquals(1, user.getUserAccounts().size());
        assertEquals("Savings", user.getUserAccounts().iterator().next().getAccountType());
    }

    @Test
    void testTransferFunds() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        Account fromAccount = new Account();
        fromAccount.setAccountBalance(new BigDecimal("200.00"));

        Account toAccount = new Account();
        toAccount.setAccountBalance(new BigDecimal("150.00"));

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        ResponseEntity<String> response = userService.transferFunds(fromAccountId, toAccountId, amount);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Funds transferred successfully", response.getBody());
    }

    @Test
    void testWithdrawFunds() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("50.00");

        Account account = new Account();
        account.setAccountBalance(new BigDecimal("100.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        ResponseEntity<String> response = userService.withdrawFunds(accountId, amount);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Funds withdrawn successfully", response.getBody());
    }

    @Test
    void testFundAccount() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("50.00");

        Account account = new Account();
        account.setAccountBalance(new BigDecimal("100.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        ResponseEntity<String> response = userService.fundAccount(accountId, amount);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Account funded successfully", response.getBody());
    }
}
