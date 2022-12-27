package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.model.Bank;
import common.bankarskiSistem.repository.BankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceUnitTests {

	@Autowired
	private BankRepository underTest;
	@Test
	void contextLoads() {
	}

}
