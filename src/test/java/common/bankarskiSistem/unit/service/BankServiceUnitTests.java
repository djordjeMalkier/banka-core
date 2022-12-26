package common.bankarskiSistem.unit.service;


import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.Conversion;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.model.ExchangeRates;
import common.bankarskiSistem.repository.BankRepository;
import common.bankarskiSistem.repository.ConversionRepository;
import common.bankarskiSistem.repository.ExchangeRatesRepository;
import common.bankarskiSistem.service.BankService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BankServiceUnitTests {

    @MockBean
    private BankRepository bankRepository;

    @MockBean
    private ExchangeRatesRepository exchangeRatesRepository;

    @MockBean
    private ConversionRepository conversionRepository;

    @Autowired
    private BankService bankService;


    @BeforeEach
    void setUp(){

    }


    @Test
    void CanCreateExchangeRatesWithOutId() {

        ExchangeRates exchangeRates=new ExchangeRates(
                null,
                "KursnaLista",
                null,
                null

        );

        bankService.createExchangeRates(exchangeRates);

        ArgumentCaptor<ExchangeRates> exchangeRatesArgumentCaptor = ArgumentCaptor.forClass(ExchangeRates.class);
        verify(exchangeRatesRepository).save(exchangeRatesArgumentCaptor.capture());

        ExchangeRates capturedExchangeRates = exchangeRatesArgumentCaptor.getValue();

        assertThat(capturedExchangeRates).isEqualTo(exchangeRates);



    }


    @Test
    void CanCreateExchangeRatesWithName() {

        ExchangeRates exchangeRates=new ExchangeRates(
                1,
                "KursnaLista 1",
                null,
                null

        );

        bankService.createExchangeRates(exchangeRates);

        ArgumentCaptor<ExchangeRates> exchangeRatesArgumentCaptor = ArgumentCaptor.forClass(ExchangeRates.class);
        verify(exchangeRatesRepository).save(exchangeRatesArgumentCaptor.capture());

        ExchangeRates capturedExchangeRates = exchangeRatesArgumentCaptor.getValue();

        assertThat(capturedExchangeRates).isEqualTo(exchangeRates);



    }

    @Test
    void CanCreateExchangeRatesWithOutName() {

        ExchangeRates exchangeRates=new ExchangeRates(
                1,
                null,
                null,
                null

        );

        bankService.createExchangeRates(exchangeRates);

        ArgumentCaptor<ExchangeRates> exchangeRatesArgumentCaptor = ArgumentCaptor.forClass(ExchangeRates.class);
        verify(exchangeRatesRepository).save(exchangeRatesArgumentCaptor.capture());

        ExchangeRates capturedExchangeRates = exchangeRatesArgumentCaptor.getValue();

        assertThat(capturedExchangeRates).isEqualTo(exchangeRates);



    }

    @Test
    void CanCreateExchangeRatesWithConversions() {

        List<Conversion> conversions=new ArrayList<>();
        Conversion conversion1=new Conversion();

        conversion1.setIdConversion(1);
        conversion1.setCurrencyFrom(Currency.EUR);
        conversion1.setCurrencyTo(Currency.RSD);
        conversion1.setValue(117);


        Conversion conversion2=new Conversion();

        conversion2.setIdConversion(2);
        conversion2.setCurrencyFrom(Currency.RSD);
        conversion2.setCurrencyTo(Currency.EUR);
        conversion2.setValue(0.0085);

        conversions.add(conversion1);
        conversions.add(conversion2);



        ExchangeRates exchangeRates=new ExchangeRates(
                1,
                null,
                conversions,
                null

        );

        bankService.createExchangeRates(exchangeRates);

        ArgumentCaptor<ExchangeRates> exchangeRatesArgumentCaptor = ArgumentCaptor.forClass(ExchangeRates.class);
        verify(exchangeRatesRepository).save(exchangeRatesArgumentCaptor.capture());

        ExchangeRates capturedExchangeRates = exchangeRatesArgumentCaptor.getValue();

        assertThat(capturedExchangeRates).isEqualTo(exchangeRates);



    }

    @Test
    void CanCreateExchangeRatesWithBanks() {

        List<Bank> banks=new ArrayList<>();

        Bank bank1=new Bank(
                1,
                "Banka 1",
                "Adresa 1"
        );

        Bank bank2=new Bank(
                2,
                "Banka 2",
                "Adresa 2"
        );

        banks.add(bank1);
        banks.add(bank2);


        ExchangeRates exchangeRates=new ExchangeRates(
                1,
                null,
                null,
                banks

        );

        bankService.createExchangeRates(exchangeRates);

        ArgumentCaptor<ExchangeRates> exchangeRatesArgumentCaptor = ArgumentCaptor.forClass(ExchangeRates.class);
        verify(exchangeRatesRepository).save(exchangeRatesArgumentCaptor.capture());

        ExchangeRates capturedExchangeRates = exchangeRatesArgumentCaptor.getValue();

        assertThat(capturedExchangeRates).isEqualTo(exchangeRates);



    }

    @Test
    void CanCreateExchangeRatesWithEmpty() {




        ExchangeRates exchangeRates=null;

        bankService.createExchangeRates(exchangeRates);

        ArgumentCaptor<ExchangeRates> exchangeRatesArgumentCaptor = ArgumentCaptor.forClass(ExchangeRates.class);
        verify(exchangeRatesRepository).save(exchangeRatesArgumentCaptor.capture());

        ExchangeRates capturedExchangeRates = exchangeRatesArgumentCaptor.getValue();

        assertThat(capturedExchangeRates).isEqualTo(exchangeRates);



    }

    @Test
    void canFindByIdExchangeRates() {
        Integer idExchangeRates = 1;
        ExchangeRates exchangeRates=new ExchangeRates();
        exchangeRates.setIdExchangeRates(idExchangeRates);
        when(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates)).thenReturn(Optional.of(exchangeRates));
        
        assertThat(bankService.findByIdExchangeRates(idExchangeRates)).isEqualTo(exchangeRates);
    }


    @Test
    void canFindByIdExchangeRatesNotExist() {
        Integer idExchangeRates = 100000;
        ExchangeRates exchangeRates=new ExchangeRates();
        exchangeRates.setIdExchangeRates(idExchangeRates);
        when(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates)).thenReturn(Optional.empty());

        assertThat(bankService.findByIdExchangeRates(idExchangeRates)).isEqualTo(exchangeRates);
    }






}
