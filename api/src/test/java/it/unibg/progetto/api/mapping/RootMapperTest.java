package it.unibg.progetto.api.mapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.application.dto.RootDto;
import it.unibg.progetto.api.domain.User;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.data.Users;

class RootMapperTest {

    private final RootMapper mapper = new RootMapper();

    @Test
    void fromUsers_whenNull_returnsNull() {
        assertNull(mapper.fromUsers(null));
    }

    @Test
    void fromUser_whenNull_returnsNull() {
        assertNull(mapper.fromUser(null));
    }

    @Test
    void fromUsers_mapsAllFields_andConvertsAccessLevel() {
        Users u = mock(Users.class);

        when(u.getUuid()).thenReturn("uuid-1");
        when(u.getName()).thenReturn("mario");
        when(u.getPassword()).thenReturn("pwd");
        when(u.getAccessLevel()).thenReturn(AccessLevel.AL2.getLevel());

        RootDto dto = mapper.fromUsers(u);

        assertNotNull(dto);
        assertEquals("uuid-1", dto.getUuid());
        assertEquals("mario", dto.getUsername());
        assertEquals("pwd", dto.getPassword());
        assertEquals(AccessLevel.AL2.getLevel(), dto.getAccessLevel());
    }

    @Test
    void fromUser_mapsAllFields() {
        User u = mock(User.class);

        when(u.getId()).thenReturn("uuid-2");
        when(u.getName()).thenReturn("anna");
        when(u.getPassword()).thenReturn("pw2");
        when(u.getAccessLevel()).thenReturn(AccessLevel.AL3);

        RootDto dto = mapper.fromUser(u);

        assertNotNull(dto);
        assertEquals("uuid-2", dto.getUuid());
        assertEquals("anna", dto.getUsername());
        assertEquals("pw2", dto.getPassword());
        assertEquals(AccessLevel.AL3.getLevel(), dto.getAccessLevel());
    }
}
