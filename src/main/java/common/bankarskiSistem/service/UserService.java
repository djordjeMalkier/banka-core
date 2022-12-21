package common.bankarskiSistem.service;

import common.bankarskiSistem.BankarskiSistem;
import common.bankarskiSistem.exceptions.EntityAlreadyExistsException;
import common.bankarskiSistem.exceptions.EntityNotFoundException;
import common.bankarskiSistem.model.BankAccount;
import common.bankarskiSistem.model.Currency;
import common.bankarskiSistem.model.User;
import common.bankarskiSistem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(BankarskiSistem.class);
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @PersistenceContext
    private EntityManager entityManager;

    // UPDATE user
    @Transactional
    public User updateUser(User user) throws EntityNotFoundException {
        if(user == null)
            throw new NullPointerException("Null user");
        User existingUser
                = userRepository.getReferenceById(user.getPersonalId());
        if (existingUser.getPersonalId().isEmpty())
            throw new EntityNotFoundException("User not found!");

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setAddress(user.getAddress());
        return userRepository.save(existingUser);
    }

    // CREATE user
    @Transactional
    public User saveUser(User user) throws EntityAlreadyExistsException {
        if(user == null)
            throw new NullPointerException("Null user");
        Optional<User> existingUser
                = userRepository.findByPersonalId(user.getPersonalId());
        if (existingUser.isEmpty()) {
            return userRepository.save(user);
        }

        throw new EntityAlreadyExistsException("This user already exists!");
    }

    // DELETE user by personal id (jmbg)
    @Transactional
    public User deleteUserById(String id) throws EntityNotFoundException {
        if(id == null)
            throw new NullPointerException("Null personal id");
        Optional<User> userOptional = userRepository.findByPersonalId(id);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("User not found!");
        userRepository.deleteByPersonalId(id);
        return userOptional.get();
    }

    //CREATE BANK ACCOUNT for user
    @Transactional
    public BankAccount createBankAccount(BankAccount bankAccount) throws EntityAlreadyExistsException {
        if(bankAccount == null)
            throw new NullPointerException("Null bank account");
        User existingUser = getUserByPersonalID(bankAccount.getUser().getPersonalId());
        if (existingUser == null)
            throw new EntityAlreadyExistsException("This bank account already exists!");

        BankAccount bankAccountMerged = entityManager.merge(bankAccount);
        existingUser.addAccount(bankAccountMerged);
        userRepository.save(existingUser);
        return bankAccountMerged;
    }

    @Transactional
    public double payIn(String personalId, Integer idAccount, double payment) throws EntityNotFoundException {
        if (idAccount == null) throw new NullPointerException("No account");

        if (payment <= 0)
            throw new IllegalArgumentException("Payment must be positive");

        User user = getUserByPersonalID(personalId);
        BankAccount bankAccount = getBankAccountByIdAccount(user, idAccount);
        bankAccount.setBalance(bankAccount.getBalance() + payment);
        userRepository.save(user);

        return bankAccount.getBalance();
    }

    @Transactional
    public double payOut(String personalId, Integer idAccount, double payment)
            throws IllegalArgumentException, ArithmeticException, EntityNotFoundException {
        if (idAccount == null) throw new NullPointerException("No account");

        if (payment <= 0)
            throw new IllegalArgumentException("Payout must be positive");
        User user = getUserByPersonalID(personalId);
        BankAccount bankAccount = getBankAccountByIdAccount(user, idAccount);
        if (payment > bankAccount.getBalance())
            throw new ArithmeticException("Payout is greater than balance");
        bankAccount.setBalance(bankAccount.getBalance() - payment);
        userRepository.save(user);

        return bankAccount.getBalance();
    }

    @Transactional
    public double transfer(String personalId, Integer idAccountFrom, Integer idAccountTo, double payment)
            throws EntityNotFoundException {
        if (idAccountFrom == null || idAccountTo == null)
            throw new NullPointerException("No accounts");

        if (payment <= 0)
            throw new IllegalArgumentException("Payout must be positive");

        BankAccount accountFrom = getBankAccountByID(personalId, idAccountFrom);
        BankAccount accountTo = getBankAccountByID(personalId, idAccountTo);


        if (accountFrom.getCurrency() != accountTo.getCurrency()) {
            payOut(personalId, accountFrom.getIdAccount(), payment);
            double convertedCurrency = conversionService.convert(
                    accountFrom.getCurrency(),
                    accountTo.getCurrency(),
                    accountFrom.getBank());
            payment *= convertedCurrency;
            payIn(personalId, accountTo.getIdAccount(), payment);
            return payment;
        } else {
            payOut(personalId, accountFrom.getIdAccount(), payment);
            payIn(personalId, accountTo.getIdAccount(), payment);
            return payment;
        }
    }

    public double getBalance(String personalId, Integer idAccount, Optional<Currency> currencyTo)
            throws EntityNotFoundException {
        if (idAccount == null) throw new NullPointerException("No account");
        User user = getUserByPersonalID(personalId);
        BankAccount bankAccount = getBankAccountByIdAccount(user, idAccount);
        double conversionRate = 1;

        if (currencyTo.isPresent()) {
            if (!currencyTo.get().equals(bankAccount.getCurrency()))
                conversionRate = conversionService.convert(bankAccount.getCurrency(),
                    currencyTo.get(),
                    bankAccount.getBank());
        }
        return bankAccount.getBalance() * conversionRate;
    }

    private BankAccount getBankAccountByIdAccount(User user, Integer bankAccountId) throws EntityNotFoundException {
        Optional<BankAccount> bankAccountOptional = user.getBankAccounts()
                .stream()
                .filter(ba -> Objects.equals(ba.getIdAccount(), bankAccountId))
                .findAny();
        if (bankAccountOptional.isEmpty())
            throw new EntityNotFoundException("Account [" + bankAccountId + "] not found");
        return bankAccountOptional.get();
    }

    public double getAllBalance(String personalId, Optional<Currency> currencyTo) {
        User user = getUserByPersonalID(personalId);
        Optional<Currency> finalCurrencyTo = currencyTo.isEmpty() ?  Optional.of(Currency.EUR) : currencyTo;
        return user.getBankAccounts().stream()
                .map(account -> {
                    try {
                        return getBalance(personalId, account.getIdAccount(), finalCurrencyTo);
                    } catch (EntityNotFoundException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .reduce((double) 0, Double::sum);
    }

    @Transactional
    public BankAccount deleteBankAccountById(String personalId, Integer idAccount) throws EntityNotFoundException {
        if(personalId == null)
            throw new NullPointerException("Null personal id");
        if(idAccount == null)
            throw new NullPointerException("Null account id");

        User user = getUserByPersonalID(personalId);
        BankAccount bankAccountToBeDeleted = getBankAccountByID(personalId, idAccount);
        if (user.getBankAccounts().contains(bankAccountToBeDeleted)) {
            user.remove(bankAccountToBeDeleted);
            userRepository.save(user);
        }
        else
            throw new EntityNotFoundException("User " + personalId + " doesn't have account " + idAccount);

        return bankAccountToBeDeleted;
    }

    public List<BankAccount> getAllAccounts(String personalId){
        User user = getUserByPersonalID(personalId);
        return user.getBankAccounts();
    }

    public BankAccount getBankAccountByID(String personalId, Integer accountId){
        User user = getUserByPersonalID(personalId);
        for (BankAccount bankAccount : user.getBankAccounts()){
            if(Objects.equals(bankAccount.getIdAccount(), accountId)) {
                return bankAccount;
            }
        }
        return null;
    }

    public User getUserByPersonalID(String personalID) throws NullPointerException {
        if(personalID == null)
            throw new NullPointerException("Null personal id");
        Optional<User> userOptional = userRepository.findByPersonalId(personalID);
        if (userOptional.isEmpty())
            throw new NullPointerException("User [" + personalID + "] not found");
        return userOptional.get();
    }

    @Override
    public UserDetails loadUserByUsername(String personalId) throws UsernameNotFoundException {
        return getUserByPersonalID(personalId);
    }
}
