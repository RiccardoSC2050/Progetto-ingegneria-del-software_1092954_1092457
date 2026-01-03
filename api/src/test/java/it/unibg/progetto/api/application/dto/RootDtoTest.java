package it.unibg.progetto.api.application.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.domain.rules.AccessLevel;

class RootDtoTest {

    @Test
    void defaultConstructor_initializesWithDefaults() {
        RootDto dto = new RootDto();

        assertNull(dto.getUuid());
        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
        assertEquals(0, dto.getAccessLevel()); // default int
    }

    @Test
    void constructor_withAccessLevel_setsAllFieldsCorrectly() {
        AccessLevel level = AccessLevel.values()[0]; // robusto: non dipende dal nome della costante

        RootDto dto = new RootDto("uuid-1", "root", "pwd", level);

        assertEquals("uuid-1", dto.getUuid());
        assertEquals("root", dto.getUsername());
        assertEquals("pwd", dto.getPassword());
        assertEquals(level.getLevel(), dto.getAccessLevel());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        RootDto dto = new RootDto();

        dto.setUuid("uuid-2");
        dto.setUsername("admin");
        dto.setPassword("secret");
        dto.setAccessLevel(5);

        assertEquals("uuid-2", dto.getUuid());
        assertEquals("admin", dto.getUsername());
        assertEquals("secret", dto.getPassword());
        assertEquals(5, dto.getAccessLevel());
    }
}
