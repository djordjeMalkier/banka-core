package common.bankarskiSistem.repository;

import common.bankarskiSistem.model.Conversion;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.model.ExchangeRates;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConversionRepository extends CrudRepository<Conversion, Integer> {
     Optional<Conversion> findByCurrencyFromAndCurrencyToAndExchangeRates(
            Currency currencyFrom, Currency currencyTo, ExchangeRates exchangeRates);

     Optional<Conversion> findByIdConversion(Integer idConversion);
}
