package com.bankstream.fraudwatcher.infrastructure.adapter.in.web;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import com.bankstream.fraudwatcher.application.port.in.EvaluateTransactionUseCase;
import com.bankstream.fraudwatcher.application.port.in.ListAlertsUseCase;
import com.bankstream.fraudwatcher.infrastructure.adapter.in.web.dto.EvaluateTransactionRequest;
import com.bankstream.fraudwatcher.infrastructure.adapter.in.web.dto.FraudAlertResponse;
import com.bankstream.fraudwatcher.infrastructure.adapter.in.web.mapper.FraudWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fraud")
@RequiredArgsConstructor
@Tag(name = "Fraud", description = "Evaluate transactions against fraud rules and list raised alerts")
public class FraudController {

    private final EvaluateTransactionUseCase evaluateTransactionUseCase;
    private final ListAlertsUseCase listAlertsUseCase;
    private final FraudWebMapper mapper;

    @PostMapping("/evaluate")
    @Operation(summary = "TEMPORARY: manually evaluate a transaction against every fraud rule "
            + "(stands in for the Kafka listener we'll add together)")
    public List<FraudAlertResponse> evaluate(@Valid @RequestBody EvaluateTransactionRequest request) {
        List<FraudAlert> alerts = evaluateTransactionUseCase.evaluate(mapper.toCommand(request));
        return alerts.stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/alerts")
    @Operation(summary = "List every fraud alert raised so far, optionally filtered by account")
    public List<FraudAlertResponse> alerts(@RequestParam(required = false) UUID accountId) {
        List<FraudAlert> alerts = accountId == null
                ? listAlertsUseCase.listAll()
                : listAlertsUseCase.listByAccount(new AccountId(accountId));

        return alerts.stream().map(mapper::toResponse).toList();
    }
}
