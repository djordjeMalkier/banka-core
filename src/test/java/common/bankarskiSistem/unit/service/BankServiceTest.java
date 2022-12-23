package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.exceptions.NameOfTheBankAlreadyExistException;
import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.ExchangeRates;
import common.bankarskiSistem.repository.BankRepository;
import common.bankarskiSistem.repository.ExchangeRatesRepository;
import common.bankarskiSistem.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {
    @Mock
    private BankRepository bankRepository;
    @Mock
    private ExchangeRatesRepository exchangeRatesRepository;
    private BankService bankService;

    @BeforeEach
    void setUp() {
        bankService = new BankService(bankRepository, exchangeRatesRepository);
    }

    @Test
    void canCreateBank() throws NameOfTheBankAlreadyExistException {
        //given
        String name = "Intesa";
        String address = "Vojvode Vlahovica 1";

        Bank bank = new Bank();
        bank.setName(name);
        bank.setAddress(address);

        //when
        bankService.createBank(bank);

        //then
        ArgumentCaptor<Bank> bankArgumentCaptor = ArgumentCaptor.forClass(Bank.class);
        verify(bankRepository).save(bankArgumentCaptor.capture());

        Bank capturedBank = bankArgumentCaptor.getValue();

        assertThat(capturedBank).isEqualTo(bank);
    }

    @Test
    void whenNameAlreadyExists_ThenCreateBankThrowsNameOfTheBankAlreadyExistException() {
        //given
        String name = "Intesa";
        Bank bank = new Bank();
        bank.setName(name);

        given(bankRepository.findByName(bank.getName())).willReturn(Optional.of(bank));

        //when and then
        assertThatThrownBy(() -> bankService.createBank(bank))
                .isInstanceOf(NameOfTheBankAlreadyExistException.class)
                .hasMessageContaining("Name of the bank already exists.");

    }

    @Test
    void whenBankIsNull_ThenCreateBankThrowsNullPointerException() {
        assertThatThrownBy(() -> bankService.createBank(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank is null.");
    }

    @Test
    void canDeleteBank() {
        //given
        String name = "Intesa";
        String address = "Vojvode Vlahovica 1";
        Integer idBank = 5;

        Bank bank = new Bank();
        bank.setIdBank(idBank);
        bank.setName(name);
        bank.setAddress(address);

        Mockito.when(bankRepository.findByIdBank(idBank)).thenReturn(Optional.of(bank));
        Mockito.when(bankRepository.deleteByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));

        //when
        Bank result = bankService.deleteBank(bank);

        //then
        assertThat(result.getIdBank()).isEqualTo(bank.getIdBank());
    }

    @Test
    void whenBankDoesNotExists_ThenDeleteBankThrowsNullPointerException() {
        //given
        Bank bank = new Bank();

        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.deleteBank(bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank does not exist.");
    }

    @Test
    void canFindBankById() {
        //given
        Integer idBank = 1;
        Bank bank = new Bank();
        bank.setIdBank(idBank);

        Mockito.when(bankRepository.findByIdBank(idBank)).thenReturn(Optional.of(bank));

        //when
        Bank result = bankService.findById(idBank);

        //then
        assertThat(result.getIdBank()).isEqualTo(bank.getIdBank());
    }

    @Test
    void whenBankDoesNotExists_ThenFindByIdThrowsNullPointerException() {
        //given
        Bank bank = new Bank();

        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.findById(bank.getIdBank()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank does not exist.");
    }

    @Test
    void updateBankName() throws NameOfTheBankAlreadyExistException {
        //given
        Integer idBank = 1;

        Bank bankOld = new Bank();
        bankOld.setIdBank(idBank);
        String nameOld = "Erste";
        bankOld.setName(nameOld);

        Bank bankNew = new Bank();
        bankNew.setIdBank(idBank);
        String nameNew = "Intesa";
        bankNew.setName(nameNew);

        Mockito.when(bankRepository.findByIdBank(idBank)).thenReturn(Optional.of(bankOld));
        Mockito.when(bankRepository.save(bankOld)).thenReturn(bankNew);
        //when
        Bank updateBank = bankService.updateBank(bankOld);

        //then
        assertThat(updateBank.getName()).isEqualTo(bankNew.getName());
    }

    @Test
    void updateBankAddress() throws NameOfTheBankAlreadyExistException {
        //given
        Integer idBank = 1;

        Bank bankOld = new Bank();
        bankOld.setIdBank(idBank);
        String addressOld = "Vojvode Vlahovica 1";
        bankOld.setAddress(addressOld);

        Bank bankNew = new Bank();
        bankNew.setIdBank(idBank);
        String addressNew = "Stepe Stepanovica 2";
        bankNew.setAddress(addressNew);

        Mockito.when(bankRepository.findByIdBank(idBank)).thenReturn(Optional.of(bankOld));
        Mockito.when(bankRepository.save(bankOld)).thenReturn(bankNew);
        //when
        Bank updateBank = bankService.updateBank(bankOld);

        //then
        assertThat(updateBank.getAddress()).isEqualTo(bankNew.getAddress());
    }

    @Test
    void whenBankDoesNotExists_ThenUpdateBankThrowsNullPointerException() {
        //given
        Bank bank = new Bank();

        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.updateBank(bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank does not exist.");
    }

    @Test
    void whenNameAlreadyExists_ThenUpdateBankThrowsNameOfTheBankAlreadyExistException() {
        //given
        Bank bank = new Bank();

        Mockito.when(bankRepository.findByIdBank(bank.getIdBank())).thenReturn(Optional.of(bank));
        given(bankRepository.findByName(bank.getName())).willReturn(Optional.of(bank));

        //when and then
        assertThatThrownBy(() -> bankService.updateBank(bank))
                .isInstanceOf(NameOfTheBankAlreadyExistException.class)
                .hasMessageContaining("Name already exist.");
    }

    @Test
    void addExchangeRates() {
        //given
        Integer idExchangeRates = 1;
        String name = "Kurs 1";

        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setIdExchangeRates(idExchangeRates);
        exchangeRates.setName(name);

        Integer idBank = 1;
        String nameBank = "Intesa";
        String address = "Vojvode Vlahovica 1";
        Bank bank = new Bank();
        bank.setIdBank(idBank);
        bank.setName(nameBank);
        bank.setAddress(address);

        Mockito.when(bankRepository.findByIdBank(idBank)).thenReturn(Optional.of(bank));
        Mockito.when(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates)).thenReturn(Optional.of(exchangeRates));

        //when
        Bank newBank = bankService.addExchangeRates(idExchangeRates, bank);

        //then
        ArgumentCaptor<Bank> bankRatesArgumentCaptor = ArgumentCaptor.forClass(Bank.class);
        verify(bankRepository).save(bankRatesArgumentCaptor.capture());

        Bank capturedBank = bankRatesArgumentCaptor.getValue();

        assertThat(capturedBank).isEqualTo(bank);
    }

    @Test
    void whenBankDoesNotExists_ThenAddExchangeRatesThrowsNullPointerException() {
        //given
        Bank bank = new Bank();
        Integer idExchangeRates = 1;

        given(bankRepository.findByIdBank(bank.getIdBank())).willReturn(Optional.empty());

        //when and then
        assertThatThrownBy(() -> bankService.addExchangeRates(idExchangeRates, bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The bank does not exist.");
    }
}