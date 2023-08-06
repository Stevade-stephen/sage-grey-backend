package io.sagegrey.backend.services;

import io.sagegrey.backend.dtos.AccountDto;
import io.sagegrey.backend.dtos.UserDto;
import io.sagegrey.backend.models.Account;
import io.sagegrey.backend.models.User;
import io.sagegrey.backend.repositories.AccountRepository;
import io.sagegrey.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public ResponseEntity<String> createUser(UserDto userDto) {
        User user = User.builder().username(userDto.getUsername())
                .userAccounts(new HashSet<>())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("User created successfully");
    }

    @Override
    public ResponseEntity<String> createAccount(Long userId, AccountDto accountDto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Account account = Account.builder()
                    .user(user)
                    .accountType(accountDto.getAccountType())
                    .accountBalance(BigDecimal.ZERO)
                    .build();

            account.setUser(user);
            accountRepository.save(account);

            user.getUserAccounts().add(account);
            userRepository.save(user);
            return ResponseEntity.ok("Account created successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Optional<Account> fromAccountOptional = accountRepository.findById(fromAccountId);
        Optional<Account> toAccountOptional = accountRepository.findById(toAccountId);

        if (fromAccountOptional.isPresent() && toAccountOptional.isPresent()) {
            Account fromAccount = fromAccountOptional.get();
            Account toAccount = toAccountOptional.get();

            BigDecimal fromBalance = fromAccount.getAccountBalance();
            BigDecimal toBalance = toAccount.getAccountBalance();

            if (fromBalance.compareTo(amount) >= 0) {
                fromBalance = fromBalance.subtract(amount);
                toBalance = toBalance.add(amount);

                fromAccount.setAccountBalance(fromBalance);
                toAccount.setAccountBalance(toBalance);

                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);

                return ResponseEntity.ok("Funds transferred successfully");
            }
            return ResponseEntity.badRequest().body("Insufficient funds");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> withdrawFunds(Long accountId, BigDecimal amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            BigDecimal accountBalance = account.getAccountBalance();

            if (accountBalance.compareTo(amount) >= 0) {
                accountBalance = accountBalance.subtract(amount);
                account.setAccountBalance(accountBalance);
                accountRepository.save(account);
                return ResponseEntity.ok("Funds withdrawn successfully");
            }
            return ResponseEntity.badRequest().body("Insufficient funds");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<String> fundAccount(Long accountId, BigDecimal amount) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            BigDecimal accountBalance = account.getAccountBalance();

            accountBalance = accountBalance.add(amount);
            account.setAccountBalance(accountBalance);
            accountRepository.save(account);

            return ResponseEntity.ok("Account funded successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
