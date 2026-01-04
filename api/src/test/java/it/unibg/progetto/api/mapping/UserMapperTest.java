package it.unibg.progetto.api.mapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.application.dto.UserDto;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.data.Users;

class UserMapperTest {

	private final UserMapper mapper = new UserMapper();

	// -------- helper reflection: evita problemi se i getter si chiamano
	// diversamente
	private static Object callGetter(Object obj, String getter) {
		try {
			Method m = obj.getClass().getMethod(getter);
			return m.invoke(obj);
		} catch (Exception e) {
			fail("Getter mancante o non invocabile: " + getter + " su " + obj.getClass().getName());
			return null;
		}
	}

	// =========================
	// toUserdtoFromUser
	// =========================
	@Test
	void toUserdtoFromUser_createsUserDtoWithSameValues() {
		UserDto dto = mapper.toUserdtoFromUser("id1", "mario", "pw", AccessLevel.AL2);

		assertNotNull(dto);
		assertEquals("id1", dto.getUuid());
		assertEquals("mario", dto.getUsername());
		assertEquals("pw", dto.getPassword());
		assertEquals(AccessLevel.AL2, dto.getAccessLevelvalue());
	}

	// =========================
	// toEntityUsersFromUserdto
	// =========================
	@Test
	void toEntityUsersFromUserdto_whenNull_returnsNull() {
		assertNull(mapper.toEntityUsersFromUserdto(null));
	}

	@Test
	void toEntityUsersFromUserdto_mapsFieldsToUsersEntity() {
		UserDto dto = mock(UserDto.class);
		when(dto.getUuid()).thenReturn("u1");
		when(dto.getUsername()).thenReturn("anna");
		when(dto.getPassword()).thenReturn("pw2");
		when(dto.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel()); // qui è int nel tuo mapper

		Users entity = mapper.toEntityUsersFromUserdto(dto);

		assertNotNull(entity);
		assertEquals("u1", callGetter(entity, "getUuid"));
		assertEquals("anna", callGetter(entity, "getName"));
		assertEquals("pw2", callGetter(entity, "getPassword"));
		assertEquals(AccessLevel.AL3.getLevel(), callGetter(entity, "getAccessLevel"));
	}

	// =========================
	// toUserdtoFromUsers
	// =========================
	@Test
	void toUserdtoFromUsers_whenNull_returnsNull() {
		assertNull(mapper.toUserdtoFromUsers(null));
	}

	@Test
	void toUserdtoFromUsers_mapsAndConvertsAccessLevel() {
		Users users = mock(Users.class);
		when(users.getUuid()).thenReturn("u2");
		when(users.getName()).thenReturn("luca");
		when(users.getPassword()).thenReturn("pw3");
		when(users.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

		UserDto dto = mapper.toUserdtoFromUsers(users);

		assertNotNull(dto);
		assertEquals("u2", dto.getUuid());
		assertEquals("luca", dto.getUsername());
		assertEquals("pw3", dto.getPassword());
		assertEquals(AccessLevel.AL1, dto.getAccessLevelvalue()); // mapper usa fromLevel(...)
	}

	// =========================
	// toUserFromUserDTO
	// =========================
	@Test
	void toUserFromUserDTO_whenNull_returnsNull() {
		assertNull(mapper.toUserFromUserDTO(null));
	}

	@Test
	void toUserFromUserDTO_mapsFieldsToDomainUser() {
		UserDto dto = mapper.toUserdtoFromUser("u3", "marco", "pw4", AccessLevel.AL2);

		User user = mapper.toUserFromUserDTO(dto);

		assertNotNull(user);
		// uso reflection per non dipendere dai nomi esatti dei getter del dominio
		assertEquals("u3", callGetter(user, "getId"));
		assertEquals("marco", callGetter(user, "getName"));
		assertEquals("pw4", callGetter(user, "getPassword"));
		// access level nel dominio potrebbe essere AccessLevel oppure int:
		// nel tuo mapper passa getAccessLevelvalue(), quindi tipicamente int.
		AccessLevel al = (AccessLevel) callGetter(user, "getAccessLevel");
		assertEquals(AccessLevel.AL2, al);

	}

	// =========================
	// getAllUsersInUserFormatWithoutPassword
	// =========================
	@Test
	void getAllUsersInUserFormatWithoutPassword_whenEmpty_returnsNull() {
		assertNull(mapper.getAllUsersInUserFormatWithoutPassword(Collections.<Users>emptyList()));
	}

	@Test
	void getAllUsersInUserFormatWithoutPassword_masksPassword() {
		Users u = mock(Users.class);
		when(u.getUuid()).thenReturn("u1");
		when(u.getName()).thenReturn("anna");
		when(u.getAccessLevel()).thenReturn(AccessLevel.AL2.getLevel());

		List<User> list = mapper.getAllUsersInUserFormatWithoutPassword(Arrays.asList(u));

		assertNotNull(list);
		assertEquals(1, list.size());

		User out = list.get(0);
		assertEquals("u1", callGetter(out, "getId"));
		assertEquals("anna", callGetter(out, "getName"));
		assertEquals("*********", callGetter(out, "getPassword")); // punto chiave del metodo
	}

	// =========================
	// getAllUsersInUserFormat
	// =========================
	@Test
	void getAllUsersInUserFormat_whenEmpty_returnsNull() {
		assertNull(mapper.getAllUsersInUserFormat(Collections.<Users>emptyList()));
	}

	@Test
	void getAllUsersInUserFormat_keepsRealPassword() {
		Users u = mock(Users.class);
		when(u.getUuid()).thenReturn("u9");
		when(u.getName()).thenReturn("mario");
		when(u.getPassword()).thenReturn("realPw");
		when(u.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		List<User> list = mapper.getAllUsersInUserFormat(Arrays.asList(u));

		assertNotNull(list);
		assertEquals(1, list.size());

		User out = list.get(0);
		assertEquals("u9", callGetter(out, "getId"));
		assertEquals("mario", callGetter(out, "getName"));
		assertEquals("realPw", callGetter(out, "getPassword")); // qui NON deve essere mascherata
	}
}
