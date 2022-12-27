package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.exceptions.NameOfTheBankAlreadyExistException;
import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.ExchangeRates;
import common.bankarskiSistem.repository.BankRepository;
import common.bankarskiSistem.repository.ExchangeRatesRepository;
import common.bankarskiSistem.service.BankService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {
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
    }

    @Test
    void createBank_nullBank_throwsNullPointerException() {
        assertThatThrownBy(() -> bankService.createBank(null))
                .isInstanceOf(NullPointerException.class);
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
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.BankServiceParameters#bank_params")
    void updateBank_sameName_ThrowsNameOfTheBankAlreadyExistException(Bank bank) {
        Mockito.when(bankRepository.findByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));
        given(bankRepository.findByName(bank.getName())).willReturn(Optional.of(bank));

        //when and then
        assertThatThrownBy(() -> bankService.updateBank(bank))
                .isInstanceOf(NameOfTheBankAlreadyExistException.class)
                .hasMessageContaining("Name already exist."); //ove poruke ne moraju da se stavljaju
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
    void addExchangeRates_invalidBankId_ThrowsNullPointerException(Bank bank, Integer idExchangeRates) {
        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.addExchangeRates(idExchangeRates, bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank does not exist.");
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
    }
}