package common.bankarskiSistem.controller.dto;

import common.bankarskiSistem.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ConversionDTO {
    private Integer idConversion;
    private Currency currencyFrom;
    private Currency currencyTo;
    private double value;
    private ExchangeRatesDTO exchangeRates;

    @Override
    public String toString() {
        return "ConversionDTO{" +
                "idConversion=" + idConversion +
                ", currencyFrom=" + currencyFrom +
                ", currencyTo=" + currencyTo +
                ", value=" + value +
                ", exchangeRates=" + exchangeRates +
                '}';
    }
}
