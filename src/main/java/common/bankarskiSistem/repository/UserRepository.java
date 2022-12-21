package common.bankarskiSistem.repository;

import common.bankarskiSistem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByPersonalId(String personalId);
    Optional<User> deleteByPersonalId(String personalId);

}
