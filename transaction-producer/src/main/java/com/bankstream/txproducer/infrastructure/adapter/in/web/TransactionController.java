package com.bankstream.txproducer.infrastructure.adapter.in.web;

import com.bankstream.txproducer.domain.model.AccountId;
import com.bankstream.txproducer.domain.model.Transaction;
import com.bankstream.txproducer.domain.port.in.ListTransactionsUseCase;
import com.bankstream.txproducer.domain.port.in.SubmitTransactionUseCase;
import com.bankstream.txproducer.infrastructure.adapter.in.web.dto.SubmitTransactionRequest;
import com.bankstream.txproducer.infrastructure.adapter.in.web.dto.TransactionResponse;
import com.bankstream.txproducer.infrastructure.adapter.in.web.mapper.TransactionWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Submit and list bank transactions")
public class TransactionController {

    private final SubmitTransactionUseCase submitTransactionUseCase;
    private final ListTransactionsUseCase listTransactionsUseCase;
    private final TransactionWebMapper mapper;

    @PostMapping
    @Operation(summary = "Submit a new transaction (deposit or withdrawal)")
    public ResponseEntity<TransactionResponse> submit(@Valid @RequestBody SubmitTransactionRequest request) {
        Transaction transaction = submitTransactionUseCase.submit(mapper.toCommand(request));
        TransactionResponse body = mapper.toResponse(transaction);
        return ResponseEntity.created(URI.create("/api/v1/transactions/" + body.id())).body(body);
    }

    @GetMapping
    @Operation(summary = "List every transaction submitted so far, optionally filtered by account")
    public List<TransactionResponse> list(@RequestParam(required = false) UUID accountId) {
        List<Transaction> transactions = accountId == null
                ? listTransactionsUseCase.listAll()
                : listTransactionsUseCase.listByAccount(new AccountId(accountId));

        return transactions.stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch a single transaction (convenience lookup over the in-memory store)")
    public ResponseEntity<TransactionResponse> getById(@PathVariable UUID id) {
        return listTransactionsUseCase.listAll().stream()
                .filter(tx -> tx.id().value().equals(id))
                .findFirst()
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
