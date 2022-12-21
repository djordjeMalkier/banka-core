package common.bankarskiSistem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ova klasa je zaduzena za sve u vezi sa Bankma. Klasa se pravi sa dva konstruktora.
 * Prvi konstruktor sa tri parametra  se koristi u koliko se Bank tek kreira i trenutno nema nijednog korisnika.
 * Drugi konstruktor sa cetiri parametra  u koliko zelimo da napravimo banku sa korisnicima.
 * I default-ni konstruktor.
 */

@Entity
@Table(name = "bank")
@Getter
@Setter
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idBank", nullable = false)
    private Integer idBank;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="address", nullable = false)
    private String address;
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idExchangeRates")
    @JsonIgnore
    private ExchangeRates exchangeRates;

    public Bank(Integer idBank, String name, String address){
        this.idBank = idBank;
        this.name = name;
        this.address = address;
        this.bankAccounts = new ArrayList<>();
    }

    public Bank(String name, String address, ExchangeRates exchangeRates) {
        this.name = name;
        this.address = address;
        this.bankAccounts = new ArrayList<>();
        this.exchangeRates = exchangeRates;
    }

    public Bank( String name, String address, List<BankAccount> bankAccounts, ExchangeRates exchangeRates) {
        this.name = name;
        this.address = address;
        this.bankAccounts = bankAccounts;
        this.exchangeRates = exchangeRates;
    }

    public Bank() {
        this.bankAccounts = new ArrayList<>();

    }

    @Override
    public String toString() {
        return "Bank{" +
                "idBank=" + idBank +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", bankAccounts=" + bankAccounts.size() +
                ", exchangeRates=" + exchangeRates.getName() +
                '}';
    }
}
