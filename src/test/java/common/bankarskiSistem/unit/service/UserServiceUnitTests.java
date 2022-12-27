package common.bankarskiSistem.unit.service;

import common.bankarskiSistem.exceptions.EntityNotFoundException;
import common.bankarskiSistem.model.User;
import common.bankarskiSistem.repository.UserRepository;
import common.bankarskiSistem.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

	@Mock
	private UserRepository userRepository;


	@InjectMocks
	private UserService userService;

	@ParameterizedTest
	@MethodSource({"common.bankarskiSistem.unit.service.parametrized.UserParameters#generateUpdateUser"})
	void updateUser_ok(User userOld, User userNew) throws EntityNotFoundException {
		when(userRepository.getReferenceById(userOld.getPersonalId())).thenReturn(userOld);
		when(userRepository.save(any(User.class))).thenReturn(userNew);

		User result= userService.updateUser(userOld);

		assertThat(result.getPersonalId()).isEqualTo(userNew.getPersonalId());

	}

	@ParameterizedTest
	@MethodSource({"common.bankarskiSistem.unit.service.parametrized.UserParameters#generateUpdateUserWithNoPersonalId"})
	void updateUser_userNotExists_throwsEntityNotFoundException(User userOld) {

		when(userRepository.getReferenceById(userOld.getPersonalId())).thenReturn(userOld);

		assertThatThrownBy(() -> userService.updateUser(userOld))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessageContaining("User not found!")

				;

	}
	@ParameterizedTest
	@NullSource
	void updateUser_invalidUser_throwsNullPointerException(User user) {
		assertThatThrownBy(() -> userService.updateUser(user))
				.isInstanceOf(NullPointerException.class);

	}

}
