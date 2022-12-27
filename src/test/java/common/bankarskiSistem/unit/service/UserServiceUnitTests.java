package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.exceptions.EntityNotFoundException;
import common.bankarskiSistem.model.*;
import common.bankarskiSistem.repository.BankAccountRepository;
import common.bankarskiSistem.repository.ConversionRepository;
import common.bankarskiSistem.repository.UserRepository;
import common.bankarskiSistem.service.ConversionService;
import common.bankarskiSistem.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {
	@InjectMocks
	private UserService userService;
	@InjectMocks
	private ConversionService conversionService;
	@Mock
	private UserRepository userRepository;




	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void getUserByPersonalID_found(User user) {

		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		User found = userService.getUserByPersonalID(user.getPersonalId());

		assertThat(found.getPersonalId()).isEqualTo(user.getPersonalId());
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void getUserByPersonalID_nullPersonalId_NullPointerExceptionIsThrown(User user) {

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.getUserByPersonalID(null);
		});

		String expectedMessage = "Null personal id";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void getUserByPersonalID_nonExistingUser_userNotFound(User user) {

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
	public void getBankAccountByID_found(User user, BankAccount bankAccount) {

		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		BankAccount found = userService.getBankAccountByID(user.getPersonalId(),bankAccount.getIdAccount());

		assertThat(found.getIdAccount()).isEqualTo(bankAccount.getIdAccount()
		);
		assertThat(found).isNotNull();
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	@ValueSource(ints = {1,2})
	public void getBankAccountByID_nonValidBankAccountId_nullReturned(User user, Integer nonValidBankAccountId) {

		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		BankAccount found = userService.getBankAccountByID(user.getPersonalId(),nonValidBankAccountId);

		assertThat(found).isNull();
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void deleteBankAccountById_deleted(User user, BankAccount bankAccount) throws EntityNotFoundException {

		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		BankAccount found = userService.getBankAccountByID(user.getPersonalId(),bankAccount.getIdAccount());

		assertThat(userService.deleteBankAccountById(user.getPersonalId(),found.getIdAccount())).isNotNull();
		assertThat(found).isNotNull();

		Mockito.verify(userRepository, Mockito.times(1)).save(user);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateUser")
	public void deleteBankAccountById_nonValidAccountId_NullPointerExceptionIsThrown(User user) {

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.deleteBankAccountById(user.getPersonalId(),null);
		});

		String expectedMessage = "Null account id";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void deleteBankAccountById_nonValidPersonalId_NullPointerExceptionIsThrown(User user, BankAccount bankAccount) {

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.deleteBankAccountById(null,bankAccount.getIdAccount());
		});

		String expectedMessage = "Null personal id";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccountWithoutUser")
	public void deleteBankAccountById_nonValidPersonalId_EntityExceptionIsThrown(User user, BankAccount bankAccount) {
		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		Exception exception = assertThrows(EntityNotFoundException.class, () ->{
			userService.deleteBankAccountById(user.getPersonalId(), bankAccount.getIdAccount());
		});

		String expectedMessage = "User " + user.getPersonalId() + " doesn't have account " + bankAccount.getIdAccount();
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

/*	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void getBalance_okWithToCurrency(User user, BankAccount bankAccount) throws EntityNotFoundException {
		Currency currency = Currency.RSD;
		Conversion conversion = new Conversion(
				1,
				Currency.EUR,
				Currency.RSD,
				117,
				null
		);

		Mockito.when(
				conversionRepository.findByCurrencyFromAndCurrencyToAndExchangeRates(
				bankAccount.getCurrency(), currency, bankAccount.getBank().getExchangeRates())
		).thenReturn(Optional.of(conversion));
		Mockito.when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

		assertThat(conversionService.convert(bankAccount.getCurrency(),currency,bankAccount.getBank())).isNotNull();
		assertThat(userService.getBalance(user.getPersonalId(), bankAccount.getIdAccount(),Optional.of(currency))).isNotNull();
	}*/

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void getBalance_nullPersonalID_NullPointerException(User user, BankAccount bankAccount) {
		Currency currency = Currency.EUR;

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.getBalance(null,bankAccount.getIdAccount(),Optional.of(currency));
		});

		String expectedMessage = "Null personal id";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void getBalance_nullAccountId_NullPointerException(User user, BankAccount bankAccount) {

		Exception exception = assertThrows(NullPointerException.class, () ->{
			userService.getBalance(user.getPersonalId(),null,Optional.of(bankAccount.getCurrency()));
		});

		String expectedMessage = "No account";
		String actualMessage = exception.getMessage();

		assertEquals(expectedMessage,actualMessage);
	}

	@ParameterizedTest
	@MethodSource("common.bankarskiSistem.parametrised.UserServiceParameters#generateBankAccount")
	public void getBalance_nullCurrency_NullPointerException(User user, BankAccount bankAccount) {

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
