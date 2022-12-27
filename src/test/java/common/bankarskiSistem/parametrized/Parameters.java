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
    public static Stream<Arguments> generateBankAccountForPayIn() {
        User user = new User("0102999555888", "Nikola", "Nikolic", "Zemun");
        BankAccount bankAccount = new BankAccount(AccountType.FOREIGN, Currency.EUR,
                new Bank(1, "Intesa", "Glavna"),
                1);
        user.addAccount(bankAccount);
        bankAccount.setBalance(5000);
        double payment = 100;
        double newBalance = bankAccount.getBalance() + payment;

        return Stream.of(
                Arguments.of(
                        user, bankAccount, payment, newBalance
                )
        );
    }

    public static Stream<Arguments> generateBankAccountForPayOut() {
        User user = new User("0102999555888", "Nikola", "Nikolic", "Zemun");
        BankAccount bankAccount = new BankAccount(AccountType.FOREIGN, Currency.EUR,
                new Bank(1, "Intesa", "Glavna"),
                1);
        user.addAccount(bankAccount);
        bankAccount.setBalance(5000);
        double payment = 50;
        double newBalance = bankAccount.getBalance() - payment;

        return Stream.of(
                Arguments.of(
                        user, bankAccount, payment, newBalance
                )
        );
    }
    public static Stream<Arguments> generatePayOutWhenPaymentGraterThenBalance() {
        User user = new User("0102999555888", "Nikola", "Nikolic", "Zemun");
        BankAccount bankAccount = new BankAccount(AccountType.FOREIGN, Currency.EUR,
                new Bank(1, "Intesa", "Glavna"),
                1);
        user.addAccount(bankAccount);
        bankAccount.setBalance(100);
        double payment = 500;
        return Stream.of(
                Arguments.of(
                        user, bankAccount, payment
                )
        );
    }
}
