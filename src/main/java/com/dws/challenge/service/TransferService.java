package com.dws.challenge.service;

import com.dws.challenge.constant.ErrorCode;
import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.exception.GlobalExceptionHandler;
import com.dws.challenge.exception.InsufficientFundsException;
import com.dws.challenge.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * This class represents a Service layer.
 * It provides methods to perform operations like amount transfer
 * from source to destination account.
 *
 * @author Tiwari Banktesh
 */
@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentNotification paymentNotification;
    private static final Object lock = new Object();

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * Adds two numbers and returns the result.
     *
     * @param  accountFromId as the first argument
     * @param  accountToId as the first argument
     * @param  transferAmount as the first argument
     */
    @Transactional
    public void transferMoney(Long accountFromId, Long accountToId, BigDecimal transferAmount) {

        synchronized (lock) {
            Account accountFrom = accountRepository.findById(accountFromId)
                    .orElseThrow(
                            () -> new AccountNotFoundException("Account id " + accountFromId + " Does not exist", ErrorCode.ACCOUNT_ERROR));

            Account accountTo = accountRepository.findById(accountToId)
                    .orElseThrow(
                            () -> new AccountNotFoundException("Account id " + accountToId + " Does not exist",ErrorCode.ACCOUNT_ERROR));

            if (accountFrom.getBalance().compareTo(transferAmount) < 0) {
                throw new InsufficientFundsException("Account id " + accountFrom.getId() + " does not have sufficient balance to transfer",ErrorCode.ACCOUNT_ERROR);
            }

            accountFrom.setBalance(accountFrom.getBalance().subtract(transferAmount));
            accountTo.setBalance(accountTo.getBalance().add(transferAmount));

            accountRepository.save(accountFrom);
            accountRepository.save(accountTo);

            // Send notifications
            String fromMessage = "Amount " + transferAmount + " transferred to Account " + accountToId;
            String toMessage = "Amount " + transferAmount + " received from Account " + accountFromId;
            log.info(paymentNotification.sendNotification(accountFromId, fromMessage));
            log.info(paymentNotification.sendNotification(accountToId, toMessage));
        }
    }
}
