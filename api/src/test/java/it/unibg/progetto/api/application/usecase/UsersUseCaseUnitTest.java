package it.unibg.progetto.api.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.application.dto.RootDto;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.InvalidValues;
import it.unibg.progetto.api.domain.rules.Validators;
import it.unibg.progetto.api.mapping.RootMapper;
import it.unibg.progetto.api.mapping.UserMapper;
import it.unibg.progetto.api.security.Hash;
import it.unibg.progetto.data.Users;
import it.unibg.progetto.service.UsersService;

class UsersUseCaseUnitTest {

	@Test
	void numberOfAllOperators_returns0_whenDbListEmpty() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		when(usersService.getAllUsersFromDataBase()).thenReturn(Collections.emptyList());

		assertEquals(0, useCase.numberOfAllOperators());
		verify(usersService).getAllUsersFromDataBase();
		verifyNoInteractions(userMapper);
	}

	@Test
	void numberOfAllOperators_countsNonNullUsers() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		List<Users> entities = Arrays.asList(mock(Users.class), mock(Users.class));
		when(usersService.getAllUsersFromDataBase()).thenReturn(entities);

		List<User> mapped = Arrays.asList(new User("id1", "u1", "pw", AccessLevel.AL1), null,
				new User("id2", "u2", "pw", AccessLevel.AL2));
		when(userMapper.getAllUsersInUserFormat(entities)).thenReturn(mapped);

		assertEquals(2, useCase.numberOfAllOperators());
		verify(userMapper).getAllUsersInUserFormat(entities);
	}

	@Test
	void rootIsOnData_returnsRootDto_whenRootPresent() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		List<Users> entities = Arrays.asList(mock(Users.class));
		when(usersService.getAllUsersFromDataBase()).thenReturn(entities);

		User rootUser = new User(String.valueOf(InvalidValues.ROOTid.getLevel()), // "0"
				InvalidValues.ROOT.toString(), // "ROOT"
				"any", AccessLevel.AL5);
		when(userMapper.getAllUsersInUserFormat(entities)).thenReturn(Arrays.asList(rootUser));

		RootDto expected = new RootDto();
		when(rootMapper.fromUser(rootUser)).thenReturn(expected);

		RootDto result = useCase.rootIsOnData();
		assertSame(expected, result);
		verify(rootMapper).fromUser(rootUser);
	}

	@Test
	void rootIsOnData_returnsNull_whenNoRoot() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		List<Users> entities = Arrays.asList(mock(Users.class));
		when(usersService.getAllUsersFromDataBase()).thenReturn(entities);

		User u = new User("1", "u1", "pw", AccessLevel.AL1);
		when(userMapper.getAllUsersInUserFormat(entities)).thenReturn(Arrays.asList(u));

		assertNull(useCase.rootIsOnData());
		verifyNoInteractions(rootMapper);
	}

	@Test
	void addRootOnData_buildsUsersEntityAndPersists() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		Root root = mock(Root.class);
		when(root.getId()).thenReturn("0");
		when(root.getName()).thenReturn("ROOT");
		when(root.getPassword()).thenReturn("pw");
		when(root.getAccessLevelValue()).thenReturn(AccessLevel.AL5.getLevel());

		RootDto rootDto = new RootDto();
		Users usersEntity = mock(Users.class);

		when(rootMapper.toRootdtoFromRoot(eq("0"), eq("ROOT"), eq("pw"), eq(AccessLevel.AL5))).thenReturn(rootDto);
		when(rootMapper.toUsersfromRootdto(rootDto)).thenReturn(usersEntity);

		useCase.addRootOnData(root);

		verify(usersService).addUsersIntoDataUsers(usersEntity);
	}

	@Test
	void trasformListUsersIntoListUserWithoutPassword_delegatesToMapperProtected() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		List<Users> entities = Arrays.asList(mock(Users.class));
		when(usersService.getAllUsersFromDataBase()).thenReturn(entities);

		List<User> expected = Arrays.asList(new User("1", "u1", "pw", AccessLevel.AL1));
		when(userMapper.getAllUsersInUserFormatWithoutPassword(entities)).thenReturn(expected);

		List<User> result = useCase.trasformListUsersIntoListUserWithoutPassword();
		assertSame(expected, result);
		verify(userMapper).getAllUsersInUserFormatWithoutPassword(entities);
	}

	@Test
	void printNameUserAll_doesNotThrow() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		List<User> list = Arrays.asList(new User("1", "u1", "pw", AccessLevel.AL1),
				new User("2", "u2", "pw", AccessLevel.AL2));

		assertDoesNotThrow(() -> useCase.printNameUserAll(Validators.neutral, list));
		assertDoesNotThrow(() -> useCase.printNameUserAll(Validators.affermative, list));
	}

	@Test
	void trasformUserIntoUsersEntity_delegatesToUserMapper() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		User u = new User("id1", "u1", "pw", AccessLevel.AL1);

		// userMapper.toUserdtoFromUser(...) e userMapper.toEntityUsersFromUserdto(...)
		// non serve costruire un UserDto reale: basta far ritornare una entity Users
		// finale
		Users expected = mock(Users.class);
		when(userMapper.toEntityUsersFromUserdto(any())).thenReturn(expected);

		Users result = useCase.trasformUserIntoUsersEntity(u);

		assertSame(expected, result);
		verify(userMapper).toEntityUsersFromUserdto(any());
	}

	@Test
	void addUserOnData_persistsUsersEntity() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = spy(new UsersUseCase(userMapper, usersService, rootMapper));

		User u = new User("id1", "u1", "pw", AccessLevel.AL1);
		Users entity = mock(Users.class);
		doReturn(entity).when(useCase).trasformUserIntoUsersEntity(u);

		useCase.addUserOnData(u);

		verify(usersService).addUsersIntoDataUsers(entity);
	}

	@Test
	void deleteUser_deletesUsersEntity() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = spy(new UsersUseCase(userMapper, usersService, rootMapper));

		User u = new User("id1", "u1", "pw", AccessLevel.AL1);
		Users entity = mock(Users.class);
		doReturn(entity).when(useCase).trasformUserIntoUsersEntity(u);

		useCase.deleteUser(u);

		verify(usersService).deleteUsers(entity);
	}

	@Test
	void loginAuthenticator_returnsProtectedUser_whenCredentialsOk() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		List<Users> entities = Arrays.asList(mock(Users.class));
		when(usersService.getAllUsersFromDataBase()).thenReturn(entities);

		String pw = "pw123456";
		User dbUser = new User("id1", "u1", Hash.hash(pw), AccessLevel.AL2);

		when(userMapper.getAllUsersInUserFormat(entities)).thenReturn(Arrays.asList(dbUser));

		User result = useCase.LoginAuthenticator("u1", pw);

		assertNotNull(result);
		assertEquals("id1", result.getId());
		assertEquals("u1", result.getName());
		assertEquals(AccessLevel.AL2, result.getAccessLevel());
		assertEquals(InvalidValues.secret.toString(), result.getPassword()); // password mascherata
	}

	@Test
	void loginAuthenticator_returnsNull_whenNoMatch() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		List<Users> entities = Arrays.asList(mock(Users.class));
		when(usersService.getAllUsersFromDataBase()).thenReturn(entities);

		User dbUser = new User("id1", "u1", Hash.hash("pw123456"), AccessLevel.AL1);
		when(userMapper.getAllUsersInUserFormat(entities)).thenReturn(Arrays.asList(dbUser));

		assertNull(useCase.LoginAuthenticator("u1", "wrong"));
	}

	@Test
	void loginAuthenticator_returnsSecretUser_whenUserListNull() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		when(usersService.getAllUsersFromDataBase()).thenReturn(Collections.emptyList());

		User result = useCase.LoginAuthenticator("u1", "pw");

		assertNotNull(result);
		assertEquals(InvalidValues.secret.toString(), result.getId());
		assertEquals(InvalidValues.secret.toString(), result.getName());
		assertEquals(InvalidValues.secret.toString(), result.getPassword());
		assertNull(result.getAccessLevel());
	}

	@Test
	void changePassordToUser_delegatesToService() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		useCase.changePassordToUser("pw", "id1");
		verify(usersService).changePw("id1", "pw");
	}

	@Test
	void changeAccessLevelToUser_delegatesToService() {
		UsersService usersService = mock(UsersService.class);
		UserMapper userMapper = mock(UserMapper.class);
		RootMapper rootMapper = mock(RootMapper.class);
		UsersUseCase useCase = new UsersUseCase(userMapper, usersService, rootMapper);

		useCase.changeAccessLevelToUser(2, "id1");
		verify(usersService).changeAl("id1", 2);
	}
}
