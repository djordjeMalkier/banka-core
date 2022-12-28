package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.exceptions.EntityAlreadyExistsException;
import common.bankarskiSistem.exceptions.EntityNotFoundException;
import common.bankarskiSistem.model.*;
import common.bankarskiSistem.repository.UserRepository;
import common.bankarskiSistem.service.ConversionService;
import common.bankarskiSistem.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
	public void getBankAccountByID_nonValidBankAccountId_nullReturned(User user) {
		int nonValidBankAccountId = 1;

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

		Exception exception = assertThrows(NullPointerException.class, () -> {
			userService.getBalance(user.getPersonalId(), bankAccount.getIdAccount()
					, null);
		});

		String expectedMessage = "Cannot invoke \"java.util.Optional.isPresent()\" because \"currencyTo\" is null";
		String actualMessage = exception.getMessage();
	}


	@ParameterizedTest
	@MethodSource({"common.bankarskiSistem.parametrized.UserParameters#generateUpdateUser"})
	void updateUser_ok(User userOld, User userNew) throws EntityNotFoundException {
		when(userRepository.getReferenceById(userOld.getPersonalId())).thenReturn(userOld);
		when(userRepository.save(any(User.class))).thenReturn(userNew);

		User result= userService.updateUser(userOld);

		verify(userRepository,times(1)).save(any());
		assertThat(result.getPersonalId()).isEqualTo(userNew.getPersonalId());

	}

	@ParameterizedTest
	@MethodSource({"common.bankarskiSistem.parametrized.UserParameters#generateUpdateUserWithNoPersonalId"})
	void updateUser_userNotExists_throwsEntityNotFoundException(User userOld) {

		when(userRepository.getReferenceById(userOld.getPersonalId())).thenReturn(userOld);

		assertThatThrownBy(() -> userService.updateUser(userOld))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("User not found!");

		verify(userRepository,never()).save(any());

	}
	@ParameterizedTest
	@NullSource
	void updateUser_nullUser_throwsNullPointerException(User user) {
		assertThatThrownBy(() -> userService.updateUser(user))
				.isInstanceOf(NullPointerException.class);
  }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateUser")
    public void saveUser_whenSaveUser_thenReturnUserObject(User user) throws EntityAlreadyExistsException {

        when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User savedEmployee = userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
        assertThat(savedEmployee).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateUser")
    public void saveUser_whenPassedExistingUser_thenThrowsEntityAlreadyExistsException(User user) {
        when(userRepository.findByPersonalId(user.getPersonalId()))
                .thenReturn(Optional.of(user));

        assertThrows(EntityAlreadyExistsException.class, () ->
                userService.saveUser(user));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void saveUser_whenPassedNullUser_thenThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                userService.saveUser(null));
        assertEquals("Null user", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateUser")
    public void deleteUserById_whenDeleteUser_thenReturnUserObject(User user) throws EntityNotFoundException {

        when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

        User returnedUser = userService.deleteUserById(user.getPersonalId());

        verify(userRepository, times(1)).deleteByPersonalId(user.getPersonalId());
        assertEquals(returnedUser.getPersonalId(), user.getPersonalId());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateUser")
    public void deleteUserById_whenPassedExistingUser_thenThrowsEntityNotFoundException(User user) {

        when(userRepository.findByPersonalId(user.getPersonalId()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                userService.deleteUserById(user.getPersonalId()));

        verify(userRepository, never()).deleteByPersonalId(user.getPersonalId());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateUser")
    public void deleteUserById_whenPassedNullId_thenThrowsNullPointerException(User user) {
        assertThrows(NullPointerException.class, () ->
                userService.deleteUserById(null));

        verify(userRepository, never()).deleteByPersonalId(user.getPersonalId());
    }

    @ParameterizedTest
    @MethodSource({"common.bankarskiSistem.parametrized.Parameters#generateBankAccountForPayIn"})
    public void payIn_whenPayInBankAccount_thenReturnBankBalance(User user, BankAccount bankAccount, double payment,
                                                                 double newBalance) throws EntityNotFoundException {

        when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));
        double balance = userService.payIn(user.getPersonalId(), bankAccount.getIdAccount(), payment);

        assertEquals(balance, newBalance);
    }

    @ParameterizedTest
    @MethodSource({"common.bankarskiSistem.parametrized.Parameters#generateBankAccount"})
    public void payIn_whenPassedNullAccountId_thenThrowsNullPointerException(User user) {
        double payment = 500;
        Exception exception = assertThrows(NullPointerException.class, () ->
                userService.payIn(user.getPersonalId(), null, payment));
        assertEquals(exception.getMessage(), "No account");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateBankAccount")
    public void payIn_whenPaymentLessThenOrEqualsZero_thenThrowsIllegalArgumentException(User user, BankAccount bankAccount) {
        double payment = 0;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.payIn(user.getPersonalId(), bankAccount.getIdAccount(), payment));

        assertEquals(exception.getMessage(), "Payment must be positive");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateBankAccountForPayOut")
    public void payOut_whenPayOutBankAccount_thenReturnBankBalance(User user, BankAccount bankAccount, double payment,
                                                                   double newBalance) throws EntityNotFoundException {

        when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));

        double balance = userService.payOut(user.getPersonalId(), bankAccount.getIdAccount(), payment);

        assertEquals(balance, newBalance);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateBankAccount")
    public void payOut_whenPassedNullAccountId_thenThrowsNullPointerException(User user) {
        double payment = 500;
        Exception exception = assertThrows(NullPointerException.class, () ->
                userService.payOut(user.getPersonalId(), null, payment));
        assertEquals(exception.getMessage(), "No account");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateBankAccount")
    public void payOut_whenPaymentLessThenOrEqualsZero_thenThrowsIllegalArgumentException(User user, BankAccount bankAccount) {
        double payment = 0;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.payOut(user.getPersonalId(), bankAccount.getIdAccount(), payment));

        assertEquals(exception.getMessage(), "Payout must be positive");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generatePayOutWhenPaymentGraterThenBalance")
    public void payOut_whenPaymentGraterThenBalance_thenThrowsArithmeticException(User user, BankAccount bankAccount,
                                                                                  double payment) {

        when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));
        Exception exception = assertThrows(ArithmeticException.class, () ->
                userService.payOut(user.getPersonalId(), bankAccount.getIdAccount(), payment));
        assertEquals(exception.getMessage(), "Payout is greater than balance");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.Parameters#generateBankAccount")
    public void createBankAccount_whenBankAccountIsCreated_thenReturnBankAccount(User user, BankAccount bankAccount) throws EntityAlreadyExistsException {
        when(userRepository.findByPersonalId(user.getPersonalId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        BankAccount savedBankAccount = userService.createBankAccount(bankAccount);

        verify(userRepository, times(1)).save(user);
        assertThat(savedBankAccount).isNotNull();
    }

    @Test
    public void createBankAccount_whenBankAccountIsNull_thenThrowsNullPointerException() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                userService.createBankAccount(null));
        assertEquals("Null bank account", exception.getMessage());
    }
}
