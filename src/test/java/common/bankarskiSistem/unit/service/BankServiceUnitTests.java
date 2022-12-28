package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.exceptions.EntityNotFoundException;
import common.bankarskiSistem.exceptions.NameOfTheBankAlreadyExistException;
import common.bankarskiSistem.model.*;
import common.bankarskiSistem.repository.BankRepository;
import common.bankarskiSistem.repository.ExchangeRatesRepository;
import common.bankarskiSistem.service.BankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceUnitTests {
    @Mock
    private BankRepository bankRepository;
    @Mock
    private ExchangeRatesRepository exchangeRatesRepository;
    @InjectMocks
    private BankService bankService;

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void createBank_ok(Bank bank, Bank saved) throws NameOfTheBankAlreadyExistException {
        //when
        when(bankRepository.save(any(Bank.class))).thenReturn(saved);

        Bank actual = bankService.createBank(bank);

        //then
    /*ArgumentCaptor<Bank> bankArgumentCaptor = ArgumentCaptor.forClass(Bank.class);
    verify(bankRepository).save(bankArgumentCaptor.capture());

    Bank capturedBank = bankArgumentCaptor.getValue();*/

        verify(bankRepository, times(1)).findByName(anyString());
        verify(bankRepository, times(1)).save(any()); //tacno jednom moze da se desi save
        assertThat(actual).isEqualTo(saved);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void createBank_sameName_throwsNameOfTheBankAlreadyExistException(Bank bank) {
        given(bankRepository.findByName(bank.getName())).willReturn(Optional.of(bank));

        //when and then
        assertThatThrownBy(() -> bankService.createBank(bank))
                .isInstanceOf(NameOfTheBankAlreadyExistException.class)
                .hasMessageContaining("Name of the bank already exists.");

        verify(bankRepository, never()).save(any());
    }

    @Test
    void createBank_nullBank_throwsNullPointerException() {
        assertThatThrownBy(() -> bankService.createBank(null))
                .isInstanceOf(NullPointerException.class);

        verify(bankRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void deleteBank_validId_ok(Bank bank) {
        Mockito.when(bankRepository.findByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));
        Mockito.when(bankRepository.deleteByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));

        //when
        Bank result = bankService.deleteBank(bank);

        //then
        verify(bankRepository, times(1)).deleteByIdBank(any());
        assertThat(result.getIdBank()).isEqualTo(bank.getIdBank());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void deleteBank_invalidId_throwsNullPointerException(Bank bank) {
        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.deleteBank(bank))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void findById_validId_ok(Bank bank) {
        Mockito.when(bankRepository.findByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));

        //when
        Bank result = bankService.findById(bank.getIdBank());

        //then
        assertThat(result.getIdBank()).isEqualTo(bank.getIdBank());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void findById_invalidId_throwsNullPointerException(Bank bank) {
        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.findById(bank.getIdBank()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank does not exist.");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#updateBank_params")
    void updateBank_ok(Bank bankOld, Bank bankNew) throws NameOfTheBankAlreadyExistException {
        Mockito.when(bankRepository.findByIdBank(bankOld.getIdBank())).thenReturn(Optional.of(bankOld));
        Mockito.when(bankRepository.save(bankOld)).thenReturn(bankNew);

        //when
        Bank updateBank = bankService.updateBank(bankOld);

        //then
        verify(bankRepository, times(1)).findByName(anyString());
        verify(bankRepository, times(1)).save(any());
        assertThat(updateBank.getAddress()).isEqualTo(bankNew.getAddress());
        assertThat(updateBank.getName()).isEqualTo(bankNew.getName());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void updateBank_invalidId_throwsNullPointerException(Bank bank) {
        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.updateBank(bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank does not exist.");

        verify(bankRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void updateBank_sameName_throwsNameOfTheBankAlreadyExistException(Bank bank) {

        Mockito.when(bankRepository.findByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));
        given(bankRepository.findByName(bank.getName())).willReturn(Optional.of(bank));

        //when and then
        assertThatThrownBy(() -> bankService.updateBank(bank))
                .isInstanceOf(NameOfTheBankAlreadyExistException.class)
                .hasMessageContaining("Name already exist."); //ove poruke ne moraju da se stavljaju

        verify(bankRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#exchangeRates_params")
    void addExchangeRates_ok(Bank bank, ExchangeRates exchangeRates) {
        Mockito.when(bankRepository.findByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));
        Mockito.when(exchangeRatesRepository.findByIdExchangeRates(exchangeRates.getIdExchangeRates())).thenReturn(Optional.of(exchangeRates));

        //when
        bankService.addExchangeRates(exchangeRates.getIdExchangeRates(), bank);

        //then
        ArgumentCaptor<Bank> bankRatesArgumentCaptor = ArgumentCaptor.forClass(Bank.class);
        verify(bankRepository).save(bankRatesArgumentCaptor.capture());

        Bank capturedBank = bankRatesArgumentCaptor.getValue();

        assertThat(capturedBank).isEqualTo(bank);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bankObjectAndIdExchangeRates_param")
    void addExchangeRates_invalidBankId_throwsNullPointerException(Bank bank, Integer idExchangeRates) {
        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.addExchangeRates(idExchangeRates, bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank does not exist.");

        verify(bankRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bankObjectAndIdExchangeRates_param")
    void addExchangeRates_invalidExchangeRateId_ThrowsNullPointerException(Bank bank, Integer idExchangeRates) {
        Mockito.when(bankRepository.findByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));
        given(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates)).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.addExchangeRates(idExchangeRates, bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The exchange rate does not exist.");

        verify(bankRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource({"common.bankarskiSistem.parametrized.ExchangeRatesParameters#generateExchangeRates"})
    void addExchangeRates_ok(ExchangeRates exchangeRates) {

        bankService.createExchangeRates(exchangeRates);

        ArgumentCaptor<ExchangeRates> exchangeRatesArgumentCaptor = ArgumentCaptor.forClass(ExchangeRates.class);
        verify(exchangeRatesRepository).save(exchangeRatesArgumentCaptor.capture());

        ExchangeRates capturedExchangeRates = exchangeRatesArgumentCaptor.getValue();
        assertThat(capturedExchangeRates).isEqualTo(exchangeRates);
    }

    @ParameterizedTest
    @NullSource
    void addExchangeRates_nullExchangeRates_ThrowsNullPointerException(ExchangeRates exchangeRates) {
        assertThatThrownBy(() -> bankService.createExchangeRates(exchangeRates))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The exchangeRates is null");
    }

    @ParameterizedTest
    @ValueSource(ints = {1})
    void findExchangeRates_validId_ok(Integer idExchangeRates) {
        ExchangeRates exchangeRates=new ExchangeRates();
        exchangeRates.setIdExchangeRates(idExchangeRates);

        when(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates)).thenReturn(Optional.of(exchangeRates));
        assertThat(bankService.findByIdExchangeRates(idExchangeRates)).isEqualTo(exchangeRates);
    }


    @ParameterizedTest
    @ValueSource(ints = {10000})
    void findExchangeRates_invalidId_ThrowsNullPointerException(Integer idExchangeRates) {
        when(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankService.findByIdExchangeRates(idExchangeRates))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The exchange rates do not exist.");
    }

    @ParameterizedTest
    @MethodSource({"common.bankarskiSistem.parametrized.ExchangeRatesParameters#generateUpdateExchangeRates"})
    void updateExchangeRates_ok(ExchangeRates exchangeRatesOld, ExchangeRates exchangeRatesNew){

        when(exchangeRatesRepository.findByIdExchangeRates(exchangeRatesOld.getIdExchangeRates())).thenReturn(Optional.of(exchangeRatesOld));
        when(exchangeRatesRepository.save(any(ExchangeRates.class))).thenReturn(exchangeRatesNew);

        ExchangeRates result=bankService.updateExchangeRates(exchangeRatesOld);

        verify(exchangeRatesRepository,times(1)).save(any());
        assertThat(result.getIdExchangeRates()).isEqualTo(exchangeRatesNew.getIdExchangeRates());
        assertThat(result.getName()).isEqualTo(exchangeRatesNew.getName());
    }

    @ParameterizedTest
    @MethodSource({"common.bankarskiSistem.parametrized.ExchangeRatesParameters#generateUpdateExchangeRatesWithEmpty"})
    void updateExchangeRates_nullExchangeRates_ThrowsNullPointerException(ExchangeRates exchangeRates) {
        when(exchangeRatesRepository.findByIdExchangeRates(exchangeRates.getIdExchangeRates())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankService.updateExchangeRates(exchangeRates))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The exchanges rates do not exist.");

        verify(exchangeRatesRepository,never()).save(any());
    }

    @ParameterizedTest
    @MethodSource({"common.bankarskiSistem.parametrized.ExchangeRatesParameters#generateBank"})
    void getAllUsers_ok(Bank bank){
        Set<User> users=bankService.getAllUsers(bank);

        Set<User> users1=new HashSet<>();
        for(int i=0; i<bank.getBankAccounts().size(); i++){
           users1.add( bank.getBankAccounts().get(i).getUser());
        }

        assertThat(users).isEqualTo(users1);
    }

    @ParameterizedTest
    @NullSource
    void getAllUsersOfEmptyBank_nullBank_throwsNullPointerException(Bank bank) {
        assertThatThrownBy(() -> bankService.getAllUsers(bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank is null.");
    }

}
