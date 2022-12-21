package common.bankarskiSistem.repository;

import common.bankarskiSistem.model.Bank;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BankRepository extends CrudRepository<Bank, Integer> {

    Optional<Bank> findByName(String name);

    Optional<Bank> findByIdBank(Integer idBank);
    Optional<Bank> deleteByIdBank(Integer idBank);


}
