package it.unibg.progetto.api.application.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.domain.rules.AccessLevel;

class UserDtoTest {

    @Test
    void gettersAndSetters_workCorrectly() {
        UserDto dto = new UserDto();

        dto.setUuid("uuid-1");
        dto.setUsername("user");
        dto.setPassword("pwd");

        
        dto.setAccessLevel(AccessLevel.values()[0].getLevel());

        assertEquals("uuid-1", dto.getUuid());
        assertEquals("user", dto.getUsername());
        assertEquals("pwd", dto.getPassword());
        assertEquals(AccessLevel.values()[0], dto.getAccessLevelvalue());
    }

    @Test
    void constructor_withParameters_setsAllFieldsCorrectly() {
        AccessLevel level = AccessLevel.values()[0];

        UserDto dto = new UserDto("uuid-2", "user2", "pwd2", level);

        assertEquals("uuid-2", dto.getUuid());
        assertEquals("user2", dto.getUsername());
        assertEquals("pwd2", dto.getPassword());
        assertEquals(level, dto.getAccessLevelvalue());
    }
}
