package common.bankarskiSistem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Kurs klasa u sebi sadrzi tabelu kurseva za EUR,RSD i USD.
 * Tabela je predstavljena preko matrice redom kolone i redovi : EUR, RSD, USD.
 * U tabeli je predstavljena vrstom jedinica valute u vrednosti druge valute sa presekom odgovarajuce kolone.
 */

@Entity
@Table(name = "exchangeRates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRates {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idExchangeRates", nullable = false)
    private Integer idExchangeRates;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "exchangeRates", cascade = CascadeType.MERGE)
    //@OneToMany(mappedBy = "exchangeRates")
    private List<Conversion> conversions;

    @OneToMany(mappedBy = "exchangeRates", cascade = CascadeType.MERGE)
    //@OneToMany(mappedBy = "exchangeRates")
    @JsonIgnore
    private List<Bank> banks;

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "idExchangeRates=" + idExchangeRates +
                ", name='" + name + '\'' +
                ", conversions=" + conversions.size() +
                ", banks=" + banks.size() +
                '}';
    }
}
