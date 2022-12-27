package common.bankarskiSistem.unit.service.parametrized;

import common.bankarskiSistem.model.*;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ExchangeRatesParameters {

    public static Stream<Arguments> generateExchangeRates(){
        ExchangeRates exchangeRates1= new ExchangeRates(
                null,
                "KursnaLista1",
                null,
                null);

        ExchangeRates exchangeRates2= new ExchangeRates(
                1,
                "KursnaLista2",
                null,
                null);

        ExchangeRates exchangeRates3=new ExchangeRates(
                1,
                null,
                null,
                null

        );
        Conversion conversion1 = new Conversion(
                1,
                Currency.EUR,
                Currency.RSD,
                117,
                null);

        Conversion conversion2 = new Conversion(
                2,
                Currency.RSD,
                Currency.EUR,
                0.0085,
                null);

        ExchangeRates exchangeRates4 = new ExchangeRates(
                1,
                "kursnaLista4",
                new ArrayList<>(List.of(conversion1,conversion2)),
                null);



        Bank bank1=new Bank(
                1,
                "Banka 1",
                "Adresa 1"
        );

        Bank bank2=new Bank(
                2,
                "Banka 2",
                "Adresa 2"
        );




        ExchangeRates exchangeRates5=new ExchangeRates(
                1,
                null,
                null,
                new ArrayList<>(List.of(bank1,bank2))


        );



        return Stream.of(
                Arguments.of(exchangeRates1),
                Arguments.of(exchangeRates2),
                Arguments.of(exchangeRates3),
                Arguments.of(exchangeRates4),
                Arguments.of(exchangeRates5)

        );
    }



    public static Stream<Arguments> generateUpdate(){
        ExchangeRates exchangeRates1= new ExchangeRates(
                1,
                "KursnaLista1",
                null,
                null);

        ExchangeRates exchangeRates2= new ExchangeRates(
                1,
                "KursnaLista2",
                null,
                null);

        return Stream.of(Arguments.of(exchangeRates1, exchangeRates2));
    }



    public static Stream<Arguments> generateBank() {


        User user1 = new User(
                "1111111111111",
                "Prvi",
                " korisnik",
                "Adressa1"

        );

        User user2 = new User(
                "222222222222",
                "Drugi",
                " korisnik",
                "Adressa2"

        );



        Bank bank = new Bank(
                1,
                "Banka Intesa",
                "Milutina Milankovica 1",
                null,
                null
        );

        BankAccount bankAccount1 = new BankAccount(
                AccountType.DINAR,
                Currency.RSD,
                user1,
                bank,
                1

        );

        BankAccount bankAccount2 = new BankAccount(
                AccountType.DINAR,
                Currency.RSD,
                user2,
                bank,
                2

        );

        bank.setBankAccounts(new ArrayList<>(List.of(bankAccount1,bankAccount2)));

        return Stream.of(
                Arguments.of(bank)
        );
    }
}
