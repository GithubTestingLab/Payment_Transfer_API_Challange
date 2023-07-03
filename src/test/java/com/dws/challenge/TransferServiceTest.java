package com.dws.challenge;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.GlobalExceptionHandler;
import com.dws.challenge.repository.AccountRepository;
import com.dws.challenge.service.NotificationService;
import com.dws.challenge.service.PaymentNotification;
import com.dws.challenge.service.TransferService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class TransferServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private PaymentNotification paymentNotification;


	@InjectMocks
	private TransferService transferService;

	private static final Logger log = LoggerFactory.getLogger(TransferServiceTest.class);

	@Test
	public void testAfterSuccessfulTransfer_AndSendNotifications() {
		// Create test data
		Long fromAccountId = 1L;
		Long toAccountId = 2L;
		BigDecimal transferAmount = BigDecimal.valueOf(100);

		// Create mock accounts
		Account accountFrom = new Account();
		accountFrom.setId(fromAccountId);
		accountFrom.setBalance(BigDecimal.valueOf(500));

		Account accountTo = new Account();
		accountTo.setId(toAccountId);
		accountTo.setBalance(BigDecimal.valueOf(200));

		// Configure account repository mock
		Mockito.when(accountRepository
						.findById(fromAccountId))
				.thenReturn(Optional.of(accountFrom));
		Mockito.when(accountRepository
						.findById(toAccountId))
				.thenReturn(Optional.of(accountTo));

		// Perform the transfer
		transferService.transferMoney(fromAccountId, toAccountId, transferAmount);

		log.info("Amount {} transferred from account {} and received in account {}",transferAmount,fromAccountId,toAccountId);

		// Verify account balances were updated
		Assert.assertEquals(BigDecimal.valueOf(400), accountFrom.getBalance());
		Assert.assertEquals(BigDecimal.valueOf(300), accountTo.getBalance());

		// Verify notifications were sent
		String fromMessage = "Amount 100 transferred to Account 2";
		String toMessage = "Amount 100 received from Account 1";
		log.info(fromMessage);
		log.info(toMessage);
		Mockito.verify(paymentNotification, Mockito.times(1))
				.sendNotification(fromAccountId, fromMessage);
		Mockito.verify(paymentNotification, Mockito.times(1))
				.sendNotification(toAccountId, toMessage);
	}

}
