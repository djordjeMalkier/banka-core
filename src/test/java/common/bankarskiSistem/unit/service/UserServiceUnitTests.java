package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.exceptions.EntityNotFoundException;
import common.bankarskiSistem.model.ExchangeRates;
import common.bankarskiSistem.model.User;
import common.bankarskiSistem.repository.ExchangeRatesRepository;
import common.bankarskiSistem.repository.UserRepository;
import common.bankarskiSistem.service.BankService;
import common.bankarskiSistem.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceUnitTests {

	@MockBean
	private UserRepository userRepository;


	@Autowired
	private UserService userService;

	@ParameterizedTest
	@MethodSource({"common.bankarskiSistem.unit.service.parametrized.UserParameters#generateUpdateUser"})
	void canUpdateUser(User userOld, User userNew) throws EntityNotFoundException {

		when(userRepository.getReferenceById(userOld.getPersonalId())).thenReturn(userOld);
		when(userRepository.save(any(User.class))).thenReturn(userNew);

		User result= null;

			result = userService.updateUser(userOld);


		assertThat(result.getPersonalId()).isEqualTo(userNew.getPersonalId());

	}

	@ParameterizedTest
	@NullSource
	void canUpdateWithEmptyUser(User user) {
		assertThatThrownBy(() -> userService.updateUser(user))
				.isInstanceOf(NullPointerException.class);

	}

}
