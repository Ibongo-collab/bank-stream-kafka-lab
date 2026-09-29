package com.bankstream.balanceconsumer.infrastructure.adapter.in.web;

import com.bankstream.balanceconsumer.domain.model.AccountId;
import com.bankstream.balanceconsumer.domain.model.Balance;
import com.bankstream.balanceconsumer.domain.port.in.ApplyTransactionUseCase;
import com.bankstream.balanceconsumer.domain.port.in.GetBalanceUseCase;
import com.bankstream.balanceconsumer.infrastructure.adapter.in.web.dto.ApplyTransactionRequest;
import com.bankstream.balanceconsumer.infrastructure.adapter.in.web.dto.BalanceResponse;
import com.bankstream.balanceconsumer.infrastructure.adapter.in.web.mapper.BalanceWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/balances")
@RequiredArgsConstructor
@Tag(name = "Balances", description = "Query account balances and (temporarily, until Kafka is wired) apply transactions manually")
public class BalanceController {

    private final ApplyTransactionUseCase applyTransactionUseCase;
    private final GetBalanceUseCase getBalanceUseCase;
    private final BalanceWebMapper mapper;


    @GetMapping("/{accountId}")
    @Operation(summary = "Get the current balance for an account")
    public BalanceResponse getBalance(@PathVariable UUID accountId) {
        Balance balance = getBalanceUseCase.getBalance(new AccountId(accountId));
        return mapper.toResponse(balance);
    }

    @PostMapping("/apply")
    @Operation(summary = "TEMPORARY: manually apply a transaction to a balance "
            + "(stands in for the Kafka listener we'll add together)")
    public BalanceResponse apply(@Valid @RequestBody ApplyTransactionRequest request) {
        Balance balance = applyTransactionUseCase.apply(mapper.toCommand(request));
        return mapper.toResponse(balance);
    }
}
