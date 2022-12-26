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
        Conversion conversion1 = new Conversion(
                1,
                Currency.EUR,
                Currency.RSD,
                117.1,
                null);

        Conversion conversion2 = new Conversion(
                1,
                Currency.RSD,
                Currency.EUR,
                0.75,
                null);

        return Stream.of(Arguments.of(conversion1, conversion2));
    }

    public static Stream<Arguments> generateBank(){
         Conversion conversion = new Conversion(
                5,
                Currency.EUR,
                Currency.RSD,
                0.75,
                null);

        ExchangeRates exchangeRates = new ExchangeRates(
                1,
                "kurs 1",
                new ArrayList<>(List.of(conversion)),
                null);

        Bank bank = new Bank(
                1,
                "Bank Intesa",
                "Milutina Milankovica 1",
                null,
                exchangeRates);

        return Stream.of(
                Arguments.of(bank, conversion)
        );
    }
}
