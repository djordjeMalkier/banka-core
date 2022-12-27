package common.bankarskiSistem.parametrized;

import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.Conversion;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.model.ExchangeRates;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConversionServiceParameters {

    public static Stream<Arguments> generateConversion(){
        Conversion conversion1 = Conversion.builder()
                .idConversion(1)
                .currencyFrom(Currency.EUR)
                .currencyTo(Currency.RSD)
                .value(117.1)
                .exchangeRates(null)
                .build();

        Conversion conversion2 = Conversion.builder()
                .idConversion(1)
                .currencyFrom(Currency.RSD)
                .currencyTo(Currency.EUR)
                .value(0.75)
                .exchangeRates(null)
                .build();

        return Stream.of(Arguments.of(conversion1, conversion2));
    }

    public static Stream<Arguments> generateBank(){
         Conversion conversion = Conversion.builder()
                 .idConversion(5)
                 .currencyFrom(Currency.RSD)
                 .currencyTo(Currency.EUR)
                 .value(0.75)
                 .exchangeRates(null)
                 .build();

        ExchangeRates exchangeRates = ExchangeRates.builder()
                .idExchangeRates(1)
                .name("kurs 1")
                .conversions(new ArrayList<>(List.of(conversion)))
                .banks(null)
                .build();

        Bank bank = Bank.builder()
                .idBank(1)
                .name("Bank Intesa")
                .address("Milutina Milankovica 1")
                .bankAccounts(null)
                .exchangeRates(exchangeRates)
                .build();

        return Stream.of(
                Arguments.of(bank, conversion)
        );
    }
}
