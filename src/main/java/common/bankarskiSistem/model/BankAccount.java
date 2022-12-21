package common.bankarskiSistem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table (name = "bankAccount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Column(name = "balance", nullable = false)
    private double balance;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idAccount", nullable = false)
    private Integer idAccount;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @ManyToOne
    @JoinColumn(name="personal_id")
    @JsonIgnore
    private User user;
    @ManyToOne
    @JoinColumn(name="idBank")
    @JsonIgnore
    private Bank bank;

    public BankAccount(AccountType accountType, Currency currency, User user, Bank bank, int idAccount) {
        if(accountType == null || currency == null || user == null || bank == null) {
            throw new NullPointerException("Null value while creating account");
        }
        this.accountType = accountType;
        this.balance = 0;
        this.currency = currency;
        this.idAccount = idAccount;
        this.user = user;
        this.bank = bank;
    }

    public BankAccount(AccountType accountType, Currency currency, Bank bank, int idAccount) {
        if(accountType == null || currency == null || user == null || bank == null) {
            throw new NullPointerException("Null value while creating account");
        }
        this.accountType = accountType;
        this.balance = 0;
        this.currency = currency;
        this.idAccount = idAccount;
        this.bank = bank;
    }

    public BankAccount(AccountType accountType, Currency currency, User user, Bank bank, float balance, int idAccount) {
        this(accountType, currency, user, bank, idAccount);
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
                ", bank=" + bank +
                '}';
    }
}
