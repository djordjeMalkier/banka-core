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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConversionServiceUnitTests {
    @InjectMocks
    private ConversionService conversionService;
    @Mock
    private ConversionRepository conversionRepository;


    @ParameterizedTest
    //
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenValidId_thenFindByIdConversionReturnsConversion(Conversion conversion) {
        //Given
        when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.of(conversion));

        //When
        Conversion found = conversionService.findByIdConversion(conversion.getIdConversion());

        //Then
        assertThat(found.getIdConversion()).isEqualTo(conversion.getIdConversion());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    public void whenInvalidId_thenFindByIdConversionThrowNullPointerException(Integer nonValidIdConversion) {
        when(conversionRepository.findByIdConversion(nonValidIdConversion)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> conversionService.findByIdConversion(nonValidIdConversion))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("The conversion dose not exist.");
    }

    @ParameterizedTest
    @NullSource
    public void whenConversionIsNull_thenDeleteConversionThrowsNullPointerException(Conversion conversion) {
        assertThatThrownBy(() -> conversionService.deleteConversion(conversion))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Conversion does not exist");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenValidId_thenConversionShouldBeDeleted(Conversion conversion) {
        //When
        Conversion result = conversionService.deleteConversion(conversion);

        //Then
        verify(conversionRepository, times(1))
                .delete(conversion);
        assertThat(result.getIdConversion()).isEqualTo(conversion.getIdConversion());
    }

    @ParameterizedTest
    @NullSource
    public void whenConversionIsNull_thenAddConversionThrowsNullPointerException(Conversion conversion) {
        assertThatThrownBy(() -> conversionService.addConversion(conversion))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Null conversion");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenAddNewConversion_thenConversionIsSaved(Conversion conversion) {
        //Given
        when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.empty());
        when(conversionRepository.save(any(Conversion.class)))
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
        when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.of(conversion));

        assertThatThrownBy(() -> conversionService.addConversion(conversion))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("This conversion already exists!");
    }

    @ParameterizedTest
    @NullSource
    public void whenConversionIsNull_thenUpdateConversionThrowsNullPointerException(Conversion conversion) {
        assertThatThrownBy(() -> conversionService.updateConversion(conversion))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Null conversion");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenUpdateNonExistingConversion_thenThrowNullPointerException(Conversion conversion) {
        //Given
        when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> conversionService.updateConversion(conversion))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("No such conversion exists!");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenUpdateExistingConversion_thenConversionIsReturned(Conversion conversion) {
        //Given
        when(conversionRepository.findByIdConversion(conversion.getIdConversion()))
                .thenReturn(Optional.of(conversion));
        when(conversionRepository.save(any(Conversion.class)))
                .thenReturn(conversion);

        //When
        Conversion result = conversionService.updateConversion(conversion);

        //Then
        assertThat(result.getIdConversion()).isEqualTo(conversion.getIdConversion());
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateConversion")
    public void whenUpdateConversionInExistingConversion_thenAllFieldsAreUpdated(Conversion conversionOld, Conversion conversionNew) {
        //Given
        when(conversionRepository.findByIdConversion(conversionOld.getIdConversion())).thenReturn(Optional.of(conversionOld));
        when(conversionRepository.save(any(Conversion.class))).thenReturn(conversionNew);

        //When
        Conversion resultConversion = conversionService.updateConversion(conversionOld);

        //Then
        assertThat(resultConversion.getIdConversion()).isEqualTo(conversionNew.getIdConversion());
        assertThat(resultConversion.getValue()).isEqualTo(conversionNew.getValue());
        assertThat(resultConversion.getCurrencyFrom()).isEqualTo(conversionNew.getCurrencyFrom());
        assertThat(resultConversion.getCurrencyTo()).isEqualTo(conversionNew.getCurrencyTo());
    }

    @ParameterizedTest
    @NullSource
    public void whenBankIsNull_thenConvertThrowsNullPointerException(Bank bank) {
        //Given
        Currency currencyFrom = Currency.EUR;
        Currency currencyTo = Currency.RSD;

        assertThatThrownBy(() -> conversionService.convert(currencyFrom, currencyTo, bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Null bank");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateBank")
    public void whenCurrencyFromOrCurrencyToIsNull_thenConvertThrowsNullPointerException(Bank bank, Conversion conversion) {
        //Given
        Currency currencyFrom = conversion.getCurrencyFrom();
        Currency currencyTo = conversion.getCurrencyTo();

        assertThatThrownBy(() -> conversionService.convert(null, currencyTo, bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Null currency");

        assertThatThrownBy(() -> conversionService.convert(currencyFrom, null, bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Null currency");
    }

    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateBank")
    public void whenConvertCallWithNonExistingConversion_thenThrowNullPointerException(Bank bank, Conversion conversion) {
        //Given
        Currency currencyFrom = conversion.getCurrencyFrom();
        Currency currencyTo = conversion.getCurrencyTo();
        when(conversionRepository.findByCurrencyFromAndCurrencyToAndExchangeRates(
                currencyFrom, currencyTo, bank.getExchangeRates())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> conversionService.convert(currencyFrom, currencyTo, bank))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Conversion " + currencyFrom + " to " + currencyTo + " not found");
    }


    @ParameterizedTest
    @MethodSource("common.bankarskiSistem.parametrized.ConversionServiceParameters#generateBank")
    public void whenConvertExists_thenConversionValueIsReturned(Bank bank, Conversion conversion) {
        //Given
        Currency currencyFrom = conversion.getCurrencyFrom();
        Currency currencyTo = conversion.getCurrencyTo();
        double value = conversion.getValue();
        when(conversionRepository.findByCurrencyFromAndCurrencyToAndExchangeRates(
                currencyFrom, currencyTo, bank.getExchangeRates())).thenReturn(Optional.of(conversion));

        //When
        double resultValue = conversionService.convert(currencyFrom, currencyTo, bank);

        //Then
        assertThat(resultValue).isEqualTo(value);
    }
}