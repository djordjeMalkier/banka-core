package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.Conversion;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.model.ExchangeRates;
import common.bankarskiSistem.repository.ConversionRepository;
import common.bankarskiSistem.service.ConversionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class ConversionServiceUnitTests {
    @InjectMocks
    private ConversionService conversionService;
    @Mock
    private ConversionRepository conversionRepository;


    @Test
    public void whenValidId_thenConversionShouldBeFound() {
        //Given
        Integer idConversion = 5;
        Conversion conversion = new Conversion();
        conversion.setIdConversion(idConversion);
        Mockito.when(conversionRepository.findByIdConversion(idConversion)).thenReturn(Optional.of(conversion));

        //When
        Conversion found = conversionService.findByIdConversion(idConversion);

        //Then
        assertThat(found.getIdConversion()).isEqualTo(idConversion);
    }

    @Test
    public void whenInvalidId_thenThrowNullPointerException() {
        //Given
        Integer nonValidIdConversion = 1;
        Mockito.when(conversionRepository.findByIdConversion(nonValidIdConversion)).thenReturn(Optional.empty());

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.findByIdConversion(nonValidIdConversion));

        //Then
        String expectedMessage = "The conversion dose not exist.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenConversionIsNull_thenDeleteConversionThrowsNullPointerException() {
        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.deleteConversion(null));

        //Then
        String expectedMessage = "Conversion does not exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void whenValidId_thenConversionShouldBeDeleted() {
        //Given
        Integer idConversion = 5;
        Conversion conversion = new Conversion();
        conversion.setIdConversion(idConversion);
        Mockito.when(conversionRepository.findByIdConversion(idConversion)).thenReturn(Optional.of(conversion));

        //When
        Conversion result = conversionService.deleteConversion(conversion);

        //Then
        Mockito.verify(conversionRepository, times(1))
                .delete(conversion);
        assertThat(result.getIdConversion()).isEqualTo(conversion.getIdConversion());
    }

    @Test
    public void whenConversionIsNull_thenAddConversionThrowsNullPointerException() {
        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.addConversion(null));

        //Then
        String expectedMessage = "Null conversion";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void whenAddNewConversion_thenConversionIsSaved() {
        //Given
        Integer idConversion = 5;
        Conversion conversion = new Conversion();
        conversion.setIdConversion(idConversion);
        Mockito.when(conversionRepository.findByIdConversion(idConversion)).thenReturn(Optional.empty());
        Mockito.when(conversionRepository.save(conversion)).thenReturn(conversion);

        //When
        Conversion result = conversionService.addConversion(conversion);

        //Then
        assertThat(result.getIdConversion()).isEqualTo(idConversion);
    }

    @Test
    public void whenAddExistingConversion_thenThrowNullPointerException() {
        //Given
        Integer idConversion = 5;
        Conversion conversion = new Conversion();
        conversion.setIdConversion(idConversion);
        Mockito.when(conversionRepository.findByIdConversion(idConversion)).thenReturn(Optional.of(conversion));

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.addConversion(conversion));

        //Then
        String expectedMessage = "This conversion already exists!";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void whenConversionIsNull_thenUpdateConversionThrowsNullPointerException() {
        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.updateConversion(null));

        //Then
        String expectedMessage = "Null conversion";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void whenUpdateNonExistingConversion_thenThrowNullPointerException() {
        //Given
        Integer nonValidIdConversion = 1;
        Conversion conversion = new Conversion();
        conversion.setIdConversion(nonValidIdConversion);
        Mockito.when(conversionRepository.findByIdConversion(nonValidIdConversion)).thenReturn(Optional.empty());

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.updateConversion(conversion));

        //Then
        String expectedMessage = "No such conversion exists!";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void whenUpdateExistingConversion_thenConversionIsReturned() {
        //Given
        Integer idConversion = 1;
        Conversion conversion = new Conversion();
        conversion.setIdConversion(idConversion);
        Mockito.when(conversionRepository.findByIdConversion(idConversion)).thenReturn(Optional.of(conversion));
        Mockito.when(conversionRepository.save(conversion)).thenReturn(conversion);

        //When
        Conversion result = conversionService.updateConversion(conversion);

        //Then
        assertThat(result.getIdConversion()).isEqualTo(idConversion);
    }

    @Test
    public void whenUpdateConversionInExistingConversion_thenAllFieldsAreUpdated() {
        //Given
        Integer idConversion = 1;
        Conversion conversionOld = new Conversion();
        conversionOld.setIdConversion(idConversion);
        double oldValue = 100;
        Currency oldCurrencyFrom = Currency.RSD;
        Currency oldCurrencyTo = Currency.EUR;
        conversionOld.setValue(oldValue);
        conversionOld.setCurrencyFrom(oldCurrencyFrom);
        conversionOld.setCurrencyTo(oldCurrencyTo);

        Conversion conversionNew = new Conversion();
        conversionNew.setIdConversion(idConversion);
        double newValue = 200;
        Currency newCurrencyFrom = Currency.EUR;
        Currency newCurrencyTo = Currency.RSD;
        conversionNew.setValue(newValue);
        conversionNew.setCurrencyFrom(newCurrencyFrom);
        conversionNew.setCurrencyTo(newCurrencyTo);
        Mockito.when(conversionRepository.findByIdConversion(idConversion)).thenReturn(Optional.of(conversionOld));
        Mockito.when(conversionRepository.save(conversionOld)).thenReturn(conversionNew);

        //When
        Conversion resultConversion = conversionService.updateConversion(conversionOld);

        //Then
        assertThat(resultConversion.getIdConversion()).isEqualTo(idConversion);
        assertThat(resultConversion.getValue()).isEqualTo(newValue);
        assertThat(resultConversion.getCurrencyFrom()).isEqualTo(newCurrencyFrom);
        assertThat(resultConversion.getCurrencyTo()).isEqualTo(newCurrencyTo);
    }

    @Test
    public void whenBankIsNull_thenConvertThrowsNullPointerException() {
        //Given
        Currency currencyFrom = Currency.EUR;
        Currency currencyTo = Currency.RSD;

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.convert(currencyFrom, currencyTo, null));

        //Then
        String expectedMessage = "Null bank";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void whenCurrencyFromOrCurrencyToIsNull_thenConvertThrowsNullPointerException() {
        //Given
        Currency currencyFrom = Currency.EUR;
        Currency currencyTo = Currency.RSD;
        Bank bank = new Bank();

        //When
        Exception exceptionCurrencyFrom = assertThrows(NullPointerException.class,
                () -> conversionService.convert(null, currencyTo, bank));

        Exception exceptionCurrencyTo = assertThrows(NullPointerException.class,
                () -> conversionService.convert(currencyFrom, null, bank));

        //Then
        String expectedMessage = "Null currency";
        String actualMessageFrom = exceptionCurrencyFrom.getMessage();
        String actualMessageTo = exceptionCurrencyTo.getMessage();
        assertEquals(actualMessageFrom, expectedMessage);
        assertEquals(actualMessageTo, expectedMessage);
    }

    @Test
    public void whenConvertCallWithNonExistingConversion_thenThrowNullPointerException() {
        //Given
        Currency currencyFrom = Currency.EUR;
        Currency currencyTo = Currency.RSD;
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setIdExchangeRates(1);
        Bank bank = new Bank();
        bank.setExchangeRates(exchangeRates);
        Mockito.when(conversionRepository.findByCurrencyFromAndCurrencyToAndExchangeRates(
                currencyFrom, currencyTo, exchangeRates)).thenReturn(Optional.empty());

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.convert(currencyFrom, currencyTo, bank));

        //Then
        String expectedMessage = "Conversion " + currencyFrom + " to " + currencyTo + " not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }


    @Test
    public void whenConvertExists_thenConversionValueIsReturned() {
        //Given
        Currency currencyFrom = Currency.EUR;
        Currency currencyTo = Currency.RSD;
        double value = 0.75;

        List<Conversion> conversions = new ArrayList<>();
        Conversion conversion = new Conversion();
        conversion.setIdConversion(1);
        conversion.setCurrencyFrom(currencyFrom);
        conversion.setCurrencyTo(currencyTo);
        conversion.setValue(value);
        conversions.add(conversion);

        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setIdExchangeRates(1);
        exchangeRates.setConversions(conversions);

        Bank bank = new Bank();
        bank.setExchangeRates(exchangeRates);
        Mockito.when(conversionRepository.findByCurrencyFromAndCurrencyToAndExchangeRates(
                currencyFrom, currencyTo, exchangeRates)).thenReturn(Optional.of(conversion));

        //When
        double resultValue = conversionService.convert(currencyFrom, currencyTo, bank);

        //Then
        assertThat(resultValue).isEqualTo(value);
    }
}