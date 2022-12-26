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

    public static Conversion generateOneConversion() {
        return new Conversion(
                5,
                Currency.EUR,
                Currency.RSD,
                0.75,
                null);
    }

    public static Stream<Arguments> generateConversion(){
        return Stream.of(Arguments.of(
                generateOneConversion()));
    }

    public static Stream<Arguments> generateBank(){
        return Stream.of(Arguments.of(
                new Bank(
                        1,
                        "Bank Intesa",
                        "Milutina Milankovica 1",
                        null,
                        new ExchangeRates(
                                1,
                                "kurs 1",
                                new ArrayList<>(List.of(generateOneConversion())),
                                null
                        )
                )));
    }
}
