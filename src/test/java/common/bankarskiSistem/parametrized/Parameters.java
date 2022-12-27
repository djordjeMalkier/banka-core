package common.bankarskiSistem.parametrized;

import common.bankarskiSistem.model.*;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class Parameters {
    public static Stream<Arguments> generateUser() {
        return Stream.of(
                Arguments.of(
                        new User("2011445745511", "Pera", "Petrovic", "Adresa")

                )
        );
    }

    public static Stream<Arguments> generateBankAccount() {
        User user = new User("0102999555666", "Milica", "Milic", "Zemun");
        BankAccount bankAccount = new BankAccount(AccountType.FOREIGN, Currency.EUR,
                new Bank(1, "Intesa", "Glavna"),
                1);
        user.addAccount(bankAccount);
        return Stream.of(
                Arguments.of(user,
                        bankAccount
                )

        );
    }
}
