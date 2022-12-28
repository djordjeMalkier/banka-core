package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.exceptions.EntityAlreadyExistsException;
import common.bankarskiSistem.exceptions.EntityNotFoundException;
import common.bankarskiSistem.model.*;
import common.bankarskiSistem.repository.UserRepository;
import common.bankarskiSistem.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

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
