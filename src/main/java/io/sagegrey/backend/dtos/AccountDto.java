package io.sagegrey.backend.dtos;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AccountDto {
    private String accountType;
    private BigDecimal accountBalance;
}
