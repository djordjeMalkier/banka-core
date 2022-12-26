package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.model.*;
import common.bankarskiSistem.repository.BankAccountRepository;
import common.bankarskiSistem.repository.UserRepository;
import common.bankarskiSistem.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.control.MappingControl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {
	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BankAccountRepository bankAccountRepository;

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void whenValidPersonalId_thenUserShouldBeFound(User user) {

		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		User found = userService.getUserByPersonalID(user.getPersonalId());

		assertThat(found.getPersonalId()).isEqualTo(user.getPersonalId());
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void whenNonValidPersonalId_thenNullPointerExceptionIsThrown(User user) {

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.getUserByPersonalID(null);
		});

		String expectedMessage = "Null personal id";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void whenNonExistingPersonalId_thenUserShouldNotBeFound(User user) {

		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.empty());


		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.getUserByPersonalID(user.getPersonalId());
		});

		String expectedMessage = "User [" + user.getPersonalId() + "]" + " not found";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void whenValidBankAccountId_thenBankAccountShouldBeFound(User user, BankAccount bankAccount) {


		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		BankAccount found = userService.getBankAccountByID(user.getPersonalId(),bankAccount.getIdAccount());

		assertThat(found.getIdAccount()).isEqualTo(bankAccount.getIdAccount()
		);
		assertThat(found).isNotNull();
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void whenNonValidBankAccountId_thenNullShouldBeFound(User user) {
		Integer account_id = 1;

		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		BankAccount found = userService.getBankAccountByID(user.getPersonalId(),account_id);
//verify samo :D
		assertThat(found).isNull();
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void whenNonValidBankAccountId_thenNullPointerExceptionIsThrownDeletingBankAccount(User user) {

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.deleteBankAccountById(user.getPersonalId(),null);
		});

		String expectedMessage = "Null account id";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void whenNonValidPersonalID_thenNullPointerExceptionIsThrownDeletingBankAccount(User user, BankAccount bankAccount) {

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.deleteBankAccountById(null,bankAccount.getIdAccount());
		});

		String expectedMessage = "Null personal id";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@Test
	public void whenNonValidPersonalID_thenNullPointerExceptionIsThrownGettingBalance() {
		Integer accountID = 1;
		Currency currency = Currency.EUR;

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.getBalance(null,accountID,Optional.of(currency));
		});

		String expectedMessage = "Null personal id";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void whenNonAccountID_thenNullPointerExceptionIsThrownGettingBalance(User user, BankAccount bankAccount) {

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.getBalance(user.getPersonalId(),null,Optional.of(bankAccount.getCurrency()));
		});

		String expectedMessage = "No account";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void whenNonValidCurrency_thenNullPointerExceptionIsThrownGettingBalance(User user, BankAccount bankAccount) {

		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.getBalance(user.getPersonalId(),bankAccount.getIdAccount()
					,null);
		});

		String expectedMessage = "Cannot invoke \"java.util.Optional.isPresent()\" because \"currencyTo\" is null";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}


}
