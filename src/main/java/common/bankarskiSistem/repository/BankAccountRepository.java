package common.bankarskiSistem.repository;

import common.bankarskiSistem.model.BankAccount;
import common.bankarskiSistem.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {
    Optional<BankAccount> deleteByIdAccount(Integer id);
}
