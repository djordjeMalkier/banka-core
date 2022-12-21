package common.bankarskiSistem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "conversion")
@Getter
@Setter
@NoArgsConstructor
public class Conversion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idConversion", nullable = false)
    private Integer idConversion;

    @Enumerated(EnumType.STRING)
    @Column(name = "currencyFrom", nullable = false)
    private Currency currencyFrom;

    @Enumerated(EnumType.STRING)
    @Column(name = "currencyTo", nullable = false)
    private Currency currencyTo;

    @Column(name = "value", nullable = false)
    private double value;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "idExchangeRates")
    @JsonIgnore
    private ExchangeRates exchangeRates;

}
