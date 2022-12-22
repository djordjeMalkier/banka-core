package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.model.*;
import common.bankarskiSistem.repository.BankAccountRepository;
import common.bankarskiSistem.repository.UserRepository;
import common.bankarskiSistem.service.UserService;
import org.junit.jupiter.api.Test;
import org.mapstruct.control.MappingControl;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceUnitTests {
	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private BankAccountRepository bankAccountRepository;

	@Test
	public void whenValidPersonalId_thenUserShouldBeFound() {
		String personalId = "2011445745511";
		String name = "Pera";
		String surname = "Petrovic";
		String address = "Adr 1";

		User user = new User(personalId, name, surname, address);

		Mockito.when(userRepository.findByPersonalId(personalId)).thenReturn(Optional.of(user));

		User found = userService.getUserByPersonalID(personalId);

		assertThat(found.getPersonalId()).isEqualTo(personalId);
	}

	@Test
	public void whenValidBankAccountId_thenBankAccountShouldBeFound() {
		String personalId = "2011445745511";
		String user_name = "Pera";
		String user_surname = "Petrovic";
		String user_address = "Adr 1";

		User user = new User(personalId, user_name, user_surname, user_address);

		Integer id_bank = 1;
		String bank_address = "Mihaila Pupina 13";
		String bank_name = "Banka Intesa";

		Bank bank = new Bank(id_bank, bank_name, bank_address);

		Integer account_id = 1;
		AccountType accountType = AccountType.FOREIGN;
		Currency currency = Currency.EUR;

		BankAccount bankAccount = new BankAccount(accountType,currency,user,bank,account_id);

		user.addAccount(bankAccount);

		Mockito.when(bankAccountRepository.findById(account_id)).thenReturn(Optional.of(bankAccount));
		Mockito.when(userRepository.findByPersonalId(personalId)).thenReturn(Optional.of(user));

		BankAccount found = userService.getBankAccountByID(personalId,account_id);

		assertThat(found.getIdAccount()).isEqualTo(account_id);
	}

	@Test
	void contextLoads() {
	}

}
