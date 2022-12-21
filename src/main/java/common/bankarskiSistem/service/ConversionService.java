package common.bankarskiSistem.service;

import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.Conversion;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.repository.ConversionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ConversionService {

    @Autowired
    private ConversionRepository conversionRepository;

    public double convert(Currency currencyFrom, Currency currencyTo, Bank bank) {
        if (currencyFrom == null || currencyTo == null)
            throw new NullPointerException("Null currency");
        if (bank == null)
            throw new NullPointerException("Null bank");
        Optional<Conversion> conversionOptional =
                conversionRepository.findByCurrencyFromAndCurrencyToAndExchangeRates(
                        currencyFrom, currencyTo, bank.getExchangeRates());
        if(conversionOptional.isEmpty())
            throw new NullPointerException("Conversion " + currencyFrom + " to " + currencyTo + " not found");

        Conversion conversion = conversionOptional.get();

        return conversion.getValue();
    }

    @Transactional
    public Conversion addConversion(Conversion conversion) {
        if(conversion == null)
            throw new NullPointerException("Null conversion");
        Conversion existingConversion
                = conversionRepository.findByIdConversion(conversion.getIdConversion())
                .orElse(null);
        if (existingConversion == null)
            return conversionRepository.save(conversion);

        throw new NullPointerException("This conversion already exists!");
    }

    @Transactional
    public Conversion updateConversion(Conversion conversion) {
        if(conversion == null)
            throw new NullPointerException("Null conversion");
        Conversion existingConversion
                = conversionRepository.findByIdConversion(conversion.getIdConversion())
                .orElseThrow(() -> new NullPointerException("No such conversion exists!"));
        if(conversion.getCurrencyFrom() != null)
            existingConversion.setCurrencyFrom(conversion.getCurrencyFrom());
        if(conversion.getCurrencyTo() != null)
            existingConversion.setCurrencyTo(conversion.getCurrencyTo());

        existingConversion.setValue(conversion.getValue());
        return conversionRepository.save(existingConversion);
    }

    @Transactional
    public Conversion deleteConversion(Conversion conversion){
        if (conversion == null) throw new NullPointerException("Conversion does not exist");
        conversionRepository.delete(conversion);
        return conversion;
    }

    public Conversion findByIdConversion(Integer idConversion) {
        if(conversionRepository.findByIdConversion(idConversion).isEmpty())
            throw new NullPointerException("The conversion dose not exist.");
        return conversionRepository.findByIdConversion(idConversion).get();
    }
}
