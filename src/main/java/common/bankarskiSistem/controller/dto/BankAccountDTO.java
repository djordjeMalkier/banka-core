package common.bankarskiSistem.controller.dto;

import common.bankarskiSistem.model.AccountType;
import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BankAccountDTO {
    private double balance;

    private Integer idAccount;

    private Currency currency;
    private AccountType accountType;
    private UserDTO user;

    private BankDto bank;

    public BankAccountDTO(AccountType accountType, Currency currency, UserDTO user, int idAccount, BankDto bank) {
        if(accountType == null || currency == null || user == null) {
            throw new NullPointerException("Null value while creating account");
        }
        this.accountType = accountType;
        this.balance = 0;
        this.currency = currency;
        this.idAccount = idAccount;
        this.user = user;
        this.bank = bank;
    }

    public BankAccountDTO(AccountType accountType, Currency currency, UserDTO user, BankDto bank, float balance, int idAccount) {
        this(accountType, currency, user,  idAccount, bank);
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                ", idAccount=" + idAccount +
                ", currency=" + currency +
                ", account type=" + accountType +
                ", user=" + user +
                '}';
    }
}
