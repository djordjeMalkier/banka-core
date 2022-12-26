package common.bankarskiSistem.unit.parametrized;

import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.ExchangeRates;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class BankServiceParameters {
    public static Stream<Arguments> bank_params() {
        String name = "Intesa";
        String address = "Vojvode Vlahovica 1";
        Bank bank = new Bank();
        bank.setName(name);
        bank.setAddress(address);

        Bank saved = new Bank(1, name, address);

        return Stream.of(Arguments.of(bank, saved));
    }

    public static Stream<Arguments> bankObjectAndIdExchangeRates_param() {
        Bank bank = new Bank();
        Integer idExchangeRates = 1;

        return Stream.of(Arguments.of(bank, idExchangeRates));
    }

    public static Stream<Arguments> updateBank_params() {
        Integer idBank = 1;

        Bank bankOld;
        String nameOld = "Erste";
        String addressOld = "Vojvode Vlahovica 1";

        Bank bankNew;
        String nameNew = "Intesa";
        String addressNew = "Stepe Stepanovica 1";

        return Stream.of(
                Arguments.of(
                    bankOld = new Bank(idBank, nameOld, addressOld),
                    bankNew = new Bank(idBank, nameNew, addressOld)
                ),
                Arguments.of(
                    bankOld = new Bank(idBank, nameOld, addressOld),
                    bankNew = new Bank(idBank, nameOld, addressNew)
                ),
                Arguments.of(
                    bankOld = new Bank(idBank, nameOld, addressOld),
                    bankNew = new Bank(idBank, nameNew, addressNew)
                )
        );
    }

    public static Stream<Arguments> exchangeRates_params() {
        Integer idExchangeRates = 1;
        String nameExchangeRates = "Kurs 1";
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setIdExchangeRates(idExchangeRates);
        exchangeRates.setName(nameExchangeRates);

        Integer idBank = 1;
        String nameBank = "Intesa";
        String address = "Vojvode Vlahovica 1";
        Bank bank = new Bank();
        bank.setIdBank(idBank);
        bank.setName(nameBank);
        bank.setAddress(address);

        return Stream.of(Arguments.of(bank, exchangeRates));
    }
}
