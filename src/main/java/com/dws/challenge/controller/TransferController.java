package com.dws.challenge.controller;

import com.dws.challenge.domain.TransferRequest;
import com.dws.challenge.service.PaymentNotification;
import com.dws.challenge.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * This class represents a Controller layer.
 * It provides methods to perform operations like amount transfer
 * from source to destination account.
 *
 * @author Tiwari Banktesh
 */
@RestController
@RequestMapping("/v1/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;


    /**
     * Adds two numbers and returns the result.
     *
     * @param  transferRequest entity the first argument
     * @return the string as message after successfully amount transfer.
     */
   @PostMapping
    public ResponseEntity<String> transferAmount(@Valid @RequestBody TransferRequest transferRequest) {
        long accountFromId = transferRequest.getFromAccountId();
        long accountToId = transferRequest.getToAccountId();
        BigDecimal amount = transferRequest.getAmount();

        transferService.transferMoney(accountFromId, accountToId, amount);
        return ResponseEntity.ok("Transfer Amount successfully...");

   }
}
