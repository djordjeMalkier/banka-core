package common.bankarskiSistem.parametrized;

import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.ExchangeRates;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class BankServiceParameters {
    public static Stream<Arguments> bank_params() {
        String name = "Intesa";
        String address = "Vojvode Vlahovica 1";
        Bank bank = Bank.builder()
                .name(name)
                .address(address)
                .build();

        Bank saved = Bank.builder()
                .idBank(1)
                .name(name)
                .address(address)
                .build();

        return Stream.of(Arguments.of(bank, saved));
    }

    public static Stream<Arguments> bankObjectAndIdExchangeRates_param() {
        Bank bank = new Bank();
        Integer idExchangeRates = 1;

        return Stream.of(Arguments.of(bank, idExchangeRates));
    }

    public static Stream<Arguments> updateBank_params() {
        Integer idBank = 1;

        String nameOld = "Erste";
        String addressOld = "Vojvode Vlahovica 1";

        String nameNew = "Intesa";
        String addressNew = "Stepe Stepanovica 1";

        return Stream.of(
                Arguments.of(
                    new Bank(idBank, nameOld, addressOld),
                    new Bank(idBank, nameNew, addressOld)
                ),
                Arguments.of(
                    new Bank(idBank, nameOld, addressOld),
                    new Bank(idBank, nameOld, addressNew)
                ),
                Arguments.of(
                    new Bank(idBank, nameOld, addressOld),
                    new Bank(idBank, nameNew, addressNew)
                )
        );
    }

    public static Stream<Arguments> exchangeRates_params() {
        Integer idExchangeRates = 1;
        String nameExchangeRates = "Kurs 1";
        ExchangeRates exchangeRates = ExchangeRates.builder()
                .idExchangeRates(idExchangeRates)
                .name(nameExchangeRates)
                .build();

        Integer idBank = 1;
        String nameBank = "Intesa";
        String address = "Vojvode Vlahovica 1";
        Bank bank = Bank.builder()
                .idBank(idBank)
                .name(nameBank)
                .address(address)
                .build();

        return Stream.of(Arguments.of(bank, exchangeRates));
    }
}
