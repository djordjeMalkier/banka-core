package common.bankarskiSistem.unit.service;


import common.bankarskiSistem.model.*;
import common.bankarskiSistem.repository.ExchangeRatesRepository;
import common.bankarskiSistem.service.BankService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankServiceUnitTests {


    @Mock
    private ExchangeRatesRepository exchangeRatesRepository;


    @InjectMocks
    private BankService bankService;






    @ParameterizedTest
    @MethodSource({"common.bankarskiSistem.unit.service.parametrized.ExchangeRatesParameters#generateExchangeRates"})
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
    @MethodSource({"common.bankarskiSistem.unit.service.parametrized.ExchangeRatesParameters#generateUpdateExchangeRates"})
    void updateExchangeRates_ok(ExchangeRates exchangeRatesOld, ExchangeRates exchangeRatesNew){

        when(exchangeRatesRepository.findByIdExchangeRates(exchangeRatesOld.getIdExchangeRates())).thenReturn(Optional.of(exchangeRatesOld));
        when(exchangeRatesRepository.save(any(ExchangeRates.class))).thenReturn(exchangeRatesNew);

        ExchangeRates result=bankService.updateExchangeRates(exchangeRatesOld);

        verify(exchangeRatesRepository,times(1)).save(any());
        assertThat(result.getIdExchangeRates()).isEqualTo(exchangeRatesNew.getIdExchangeRates());
        assertThat(result.getName()).isEqualTo(exchangeRatesNew.getName());
    }

    @ParameterizedTest
    @NullSource
    void updateExchangeRates_nullExchangeRates_ThrowsNullPointerException(ExchangeRates exchangeRates) {
        assertThatThrownBy(() -> bankService.updateExchangeRates(exchangeRates))
                .isInstanceOf(NullPointerException.class);

    }


    @ParameterizedTest
    @MethodSource({"common.bankarskiSistem.unit.service.parametrized.ExchangeRatesParameters#generateBank"})
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
