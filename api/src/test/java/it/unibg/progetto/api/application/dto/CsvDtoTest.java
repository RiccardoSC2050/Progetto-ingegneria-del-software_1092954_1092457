package it.unibg.progetto.api.application.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CsvDtoTest {

    @Test
    void gettersAndSetters_workCorrectly() {
        CsvDto dto = new CsvDto();

        dto.setId(10);
        dto.setFileName("test.csv");
        dto.setOwnerId("owner-123");

        byte[] data = new byte[] {1, 2, 3};
        dto.setData(data);

        assertEquals(10, dto.getId());
        assertEquals("test.csv", dto.getFileName());
        assertEquals("owner-123", dto.getOwnerId());
        assertArrayEquals(data, dto.getData());
    }

    @Test
    void constructor_withId_setsAllFields() {
        byte[] data = new byte[] {4, 5, 6};

        CsvDto dto = new CsvDto(1, "file.csv", "owner-1", data);

        assertEquals(1, dto.getId());
        assertEquals("file.csv", dto.getFileName());
        assertEquals("owner-1", dto.getOwnerId());
        assertArrayEquals(data, dto.getData());
    }

    @Test
    void constructor_withoutId_setsAllFieldsExceptId() {
        byte[] data = new byte[] {7, 8, 9};

        CsvDto dto = new CsvDto("file.csv", "owner-2", data);

        assertEquals("file.csv", dto.getFileName());
        assertEquals("owner-2", dto.getOwnerId());
        assertArrayEquals(data, dto.getData());
    }
}
