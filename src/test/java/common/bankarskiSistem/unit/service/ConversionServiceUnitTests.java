package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.Conversion;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.repository.ConversionRepository;
import common.bankarskiSistem.service.ConversionService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ConversionServiceUnitTests {
    @InjectMocks
    private ConversionService conversionService;
    @Mock
    private ConversionRepository conversionRepository;


    @ParameterizedTest
    //
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenValidId_thenConversionShouldBeFound(Conversion conversion) {
        //Given
        Mockito.when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.of(conversion));

        //When
        Conversion found = conversionService.findByIdConversion(conversion.getIdConversion());

        //Then
        assertThat(found.getIdConversion()).isEqualTo(conversion.getIdConversion());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    public void whenInvalidId_thenThrowNullPointerException(Integer nonValidIdConversion) {
        //Given
        Mockito.when(conversionRepository.findByIdConversion(nonValidIdConversion)).thenReturn(Optional.empty());

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.findByIdConversion(nonValidIdConversion));

        //Then
        String expectedMessage = "The conversion dose not exist.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @NullSource
    public void whenConversionIsNull_thenDeleteConversionThrowsNullPointerException(Conversion conversion) {
        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.deleteConversion(conversion));

        //Then
        String expectedMessage = "Conversion does not exist";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenValidId_thenConversionShouldBeDeleted(Conversion conversion) {
        //When
        Conversion result = conversionService.deleteConversion(conversion);

        //Then
        Mockito.verify(conversionRepository, times(1))
                .delete(conversion);
        assertThat(result.getIdConversion()).isEqualTo(conversion.getIdConversion());
    }

    @ParameterizedTest
    @NullSource
    public void whenConversionIsNull_thenAddConversionThrowsNullPointerException(Conversion conversion) {
        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.addConversion(conversion));

        //Then
        String expectedMessage = "Null conversion";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenAddNewConversion_thenConversionIsSaved(Conversion conversion) {
        //Given
        Mockito.when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.empty());
        Mockito.when(conversionRepository.save(conversion))
                .thenReturn(conversion);

        //When
        Conversion result = conversionService.addConversion(conversion);

        //Then
        assertThat(result.getIdConversion()).isEqualTo(conversion.getIdConversion());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenAddExistingConversion_thenThrowNullPointerException(Conversion conversion) {
        //Given
        Mockito.when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.of(conversion));

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.addConversion(conversion));

        //Then
        String expectedMessage = "This conversion already exists!";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @ParameterizedTest
    @NullSource
    public void whenConversionIsNull_thenUpdateConversionThrowsNullPointerException(Conversion conversion) {
        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.updateConversion(conversion));

        //Then
        String expectedMessage = "Null conversion";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenUpdateNonExistingConversion_thenThrowNullPointerException(Conversion conversion) {
        //Given
        Mockito.when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.empty());

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.updateConversion(conversion));

        //Then
        String expectedMessage = "No such conversion exists!";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenUpdateExistingConversion_thenConversionIsReturned(Conversion conversion) {
        //Given
        Mockito.when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.of(conversion));
        Mockito.when(conversionRepository.save(conversion)).thenReturn(conversion);

        //When
        Conversion result = conversionService.updateConversion(conversion);

        //Then
        assertThat(result.getIdConversion()).isEqualTo(conversion.getIdConversion());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenUpdateConversionInExistingConversion_thenAllFieldsAreUpdated(Conversion conversionOld) {
        //Given
        int idConversion = conversionOld.getIdConversion();
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

    @ParameterizedTest
    @NullSource
    public void whenBankIsNull_thenConvertThrowsNullPointerException(Bank bank) {
        //Given
        Currency currencyFrom = Currency.EUR;
        Currency currencyTo = Currency.RSD;

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.convert(currencyFrom, currencyTo, bank));

        //Then
        String expectedMessage = "Null bank";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateBank")
    public void whenCurrencyFromOrCurrencyToIsNull_thenConvertThrowsNullPointerException(Bank bank) {
        //Given
        Conversion conversion = bank.getExchangeRates().getConversions().get(0);
        Currency currencyFrom = conversion.getCurrencyFrom();
        Currency currencyTo = conversion.getCurrencyTo();

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

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateBank")
    public void whenConvertCallWithNonExistingConversion_thenThrowNullPointerException(Bank bank) {
        //Given
        Conversion conversion = bank.getExchangeRates().getConversions().get(0);
        Currency currencyFrom = conversion.getCurrencyFrom();
        Currency currencyTo = conversion.getCurrencyTo();
        Mockito.when(conversionRepository.findByCurrencyFromAndCurrencyToAndExchangeRates(
                currencyFrom, currencyTo, bank.getExchangeRates())).thenReturn(Optional.empty());

        //When
        Exception exception = assertThrows(NullPointerException.class,
                () -> conversionService.convert(currencyFrom, currencyTo, bank));

        //Then
        String expectedMessage = "Conversion " + currencyFrom + " to " + currencyTo + " not found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }


    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateBank")
    public void whenConvertExists_thenConversionValueIsReturned(Bank bank) {
        //Given
        Conversion conversion = bank.getExchangeRates().getConversions().get(0);
        Currency currencyFrom = conversion.getCurrencyFrom();
        Currency currencyTo = conversion.getCurrencyTo();
        double value = conversion.getValue();


        Mockito.when(conversionRepository.findByCurrencyFromAndCurrencyToAndExchangeRates(
                currencyFrom, currencyTo, bank.getExchangeRates())).thenReturn(Optional.of(conversion));

        //When
        double resultValue = conversionService.convert(currencyFrom, currencyTo, bank);

        //Then
        assertThat(resultValue).isEqualTo(value);
    }
}