package common.bankarskiSistem.service;

import common.bankarskiSistem.exceptions.NameOfTheBankAlreadyExistException;
import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.model.BankAccount;
import common.bankarskiSistem.model.ExchangeRates;
import common.bankarskiSistem.model.User;
import common.bankarskiSistem.repository.BankRepository;
import common.bankarskiSistem.repository.ExchangeRatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class BankService {
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private ExchangeRatesRepository exchangeRatesRepository;

    /**
     *
     * @param bank the bank
     * @return bank that was created
     */
    @Transactional
    public Bank createBank(Bank bank) throws NameOfTheBankAlreadyExistException {
        if(bank == null)
            throw new NullPointerException("The bank is null.");
        if(bankRepository.findByName(bank.getName()).isPresent())
            throw new NameOfTheBankAlreadyExistException("Name of the bank already exists.");

        return bankRepository.save(bank);
    }

    @Transactional
    public Bank deleteBank(Bank bank) {
        if(bankRepository.findByIdBank(bank.getIdBank()).isEmpty())
            throw new NullPointerException("The bank does not exist.");
        bankRepository.deleteByIdBank(bank.getIdBank());

        return bank;
    }

    public Bank findById(Integer idBank){
        if(bankRepository.findByIdBank(idBank).isEmpty())
            throw new NullPointerException("The bank does not exist.");
        return bankRepository.findByIdBank(idBank).get();
    }

    @Transactional
    public Bank updateBank(Bank bank) throws NameOfTheBankAlreadyExistException {
        if(bankRepository.findByIdBank(bank.getIdBank()).isEmpty())
            throw new NullPointerException("The bank does not exist.");
        if(bankRepository.findByName(bank.getName()).isPresent()) {
            throw new NameOfTheBankAlreadyExistException("Name already exist.");
        }
        Bank updatedBank = bankRepository.findByIdBank(bank.getIdBank()).get();

        if(bank.getName() != null)
            updatedBank.setName(bank.getName() == null ? updatedBank.getName() : bank.getName());
        if(bank.getAddress() != null)
            updatedBank.setAddress(bank.getAddress() == null ? updatedBank.getAddress() : bank.getAddress());
        return bankRepository.save(updatedBank);
    }

    @Transactional
    public Bank addExchangeRates(Integer idExchangeRates, Bank bank) {
        if(bankRepository.findByIdBank(bank.getIdBank()).isEmpty())
            throw new NullPointerException("The bank does not exist.");
        Bank updatedBank = bankRepository.findByIdBank(bank.getIdBank()).get();
        if(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates).isEmpty()) {
            throw new NullPointerException("The exchange rate does not exist.");
        }
        updatedBank.setExchangeRates(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates).get());
        return bankRepository.save(updatedBank);

    }
    /**
     *
     * @param exchangeRates object of ExchangeRates
     * @return exchange rates that was created
     */
    @Transactional
    public ExchangeRates createExchangeRates(ExchangeRates exchangeRates) {
        if(exchangeRates == null)
            throw new NullPointerException("The exchangeRates is null");
        return exchangeRatesRepository.save(exchangeRates);
    }

    public ExchangeRates findByIdExchangeRates(Integer idExchangeRates) {
        if(exchangeRatesRepository.findByIdExchangeRates(idExchangeRates).isEmpty())
            throw new NullPointerException("The exchange rates do not exist.");
        return exchangeRatesRepository.findByIdExchangeRates(idExchangeRates).get();
    }

    /**
     *
     * @param exchangeRates object of ExchangeRates
     * @return exchange rates
     */
    @Transactional
    public ExchangeRates updateExchangeRates(ExchangeRates exchangeRates) {
        if(exchangeRatesRepository.findByIdExchangeRates(exchangeRates.getIdExchangeRates()).isEmpty())
            throw new NullPointerException("The exchanges rates do not exist.");
        ExchangeRates updatedExchangeRates = exchangeRatesRepository.findByIdExchangeRates(exchangeRates.getIdExchangeRates()).get();
        updatedExchangeRates.setName(exchangeRates.getName());

        return exchangeRatesRepository.save(updatedExchangeRates);
    }

    /**
     *
     * @param bank object
     * @return list of users
     */
    public Set<User> getAllUsers(Bank bank) {
        if (bank == null)
            throw new NullPointerException("The bank is null.");
        Set<User> users = new HashSet<>();

        for (BankAccount account: bank.getBankAccounts()) {
            users.add(account.getUser());
        }

        return users;
    }


}
