package common.bankarskiSistem.parametrised;

import common.bankarskiSistem.model.*;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.stream.Stream;

public final class UserServiceParameters {


    public static Stream<Arguments> generateUser() {
        return  Stream.of(
                Arguments.of(
                        new User("2011445745511","Pera","Petrovic","Adr")
                )
        );
    }

    public static Stream<Arguments> generateBankAccount(){
        String personalId = "2011445745511";
        String user_name = "Pera";
        String user_surname = "Petrovic";
        String user_address = "Adr 1";

        User user = new User(personalId, user_name, user_surname, user_address);

        Integer id_bank = 1;
        String bank_address = "Mihaila Pupina 13";
        String bank_name = "Banka Intesa";

        Bank bank = new Bank(id_bank, bank_name, bank_address);

        Integer account_id = 1;
        AccountType accountType = AccountType.FOREIGN;
        Currency currency = Currency.EUR;

        BankAccount bankAccount = new BankAccount(accountType,currency,user,bank,account_id);
        user.addAccount(bankAccount);

        return  Stream.of(
                Arguments.of(user,bankAccount)
        );
    }

    public static Stream<Arguments> generateBankAccountWithoutUser(){

        Conversion conversion = new Conversion(
                1,
                Currency.EUR,
                Currency.RSD,
                117,
                null
        );

        Conversion conversion2 = new Conversion(
                2,
                Currency.RSD,
                Currency.EUR,
                0.0085,
                null);

        ArrayList<Conversion> conversions = new ArrayList<>();
        conversions.add(conversion);
        conversions.add(conversion2);


        String personalId = "2011445745511";
        String user_name = "Pera";
        String user_surname = "Petrovic";
        String user_address = "Adr 1";

        User user = new User(personalId, user_name, user_surname, user_address);

        Integer id_bank = 1;
        String bank_address = "Mihaila Pupina 13";
        String bank_name = "Banka Intesa";

        Bank bank = new Bank(id_bank, bank_name, bank_address);

        ExchangeRates exchangeRates = new ExchangeRates(1,null,conversions,null);

        Integer account_id = 1;
        AccountType accountType = AccountType.FOREIGN;
        Currency currency = Currency.EUR;

        BankAccount bankAccount = new BankAccount(accountType,currency,user,bank,account_id);

        return  Stream.of(
                Arguments.of(user,bankAccount)
        );
    }

}
