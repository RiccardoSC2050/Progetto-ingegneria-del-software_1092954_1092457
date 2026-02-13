package it.unibg.progetto.api.mapping;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.data.Csv;
import it.unibg.progetto.service.CsvService;

class CsvMapperTest {

    private final CsvMapper mapper = new CsvMapper();

    @Test
    void csvConverterFromData_convertsFields() {
        byte[] data = "a,b,c\n1,2,3".getBytes(StandardCharsets.UTF_8);

        Csv c = new Csv(Integer.valueOf(7), "file.csv", "user-1", data);

        CsvDto dto = mapper.csvConverterFromData(c);

        assertEquals(7, dto.getId()); 
        assertEquals("file.csv", dto.getFileName());
        assertEquals("user-1", dto.getOwnerId());
        assertArrayEquals(data, dto.getData()); // byte[]
    }

    @Test
    void toCsvFromCsvDtoNoId_createsCsvWithoutId() {
        byte[] data = "DATA".getBytes(StandardCharsets.UTF_8);

        CsvDto dto = new CsvDto("x.csv", "user-2", data);

        Csv c = mapper.toCsvFromCsvDtoNoId(dto);

      
        assertEquals("x.csv", c.getFileName());
        assertEquals("user-2", c.getOwnerId());
        assertArrayEquals(data, c.getData());
    }

    @Test
    void toCsvFromCsvDtoWithId_createsCsvWithId() {
        byte[] data = "DATA2".getBytes(StandardCharsets.UTF_8);

        CsvDto dto = new CsvDto(10, "y.csv", "user-3", data);

        Csv c = mapper.toCsvFromCsvDtoWithId(dto);

        assertEquals(Integer.valueOf(10), c.getId()); 
        assertEquals("y.csv", c.getFileName());
        assertEquals("user-3", c.getOwnerId());
        assertArrayEquals(data, c.getData());
    }

    @Test
    void ListCsvFromDatabase_convertsListToDto() {
        CsvService service = mock(CsvService.class);

        List<Csv> data = Arrays.asList(
                new Csv(Integer.valueOf(1), "a.csv", "u1", "A".getBytes(StandardCharsets.UTF_8)),
                new Csv(Integer.valueOf(2), "b.csv", "u2", "B".getBytes(StandardCharsets.UTF_8))
        );

        when(service.getAllFileCsv()).thenReturn(data);

        List<CsvDto> out = mapper.ListCsvFromDatabase(service);

        assertEquals(2, out.size());
        assertEquals("a.csv", out.get(0).getFileName());
        assertEquals("b.csv", out.get(1).getFileName());
        verify(service).getAllFileCsv();
    }

    @Test
    void ListCsvFromDatabaseWithSpecificUUID_filtersByOwnerId() {
        CsvService service = mock(CsvService.class);

        List<Csv> data = Arrays.asList(
                new Csv(Integer.valueOf(1), "a.csv", "u1", "A".getBytes(StandardCharsets.UTF_8)),
                new Csv(Integer.valueOf(2), "b.csv", "u2", "B".getBytes(StandardCharsets.UTF_8)),
                new Csv(Integer.valueOf(3), "c.csv", "u1", "C".getBytes(StandardCharsets.UTF_8))
        );

        when(service.getAllFileCsv()).thenReturn(data);

        List<CsvDto> out = mapper.ListCsvFromDatabaseWithSpecificUUID(service, "u1");

        assertEquals(2, out.size());
        assertEquals("u1", out.get(0).getOwnerId());
        assertEquals("u1", out.get(1).getOwnerId());
        verify(service).getAllFileCsv();
    }

    @Test
    void ListCSv_returnsSameListFromService() {
        CsvService service = mock(CsvService.class);

        List<Csv> data = Arrays.asList(
                new Csv(Integer.valueOf(1), "a.csv", "u1", "A".getBytes(StandardCharsets.UTF_8))
        );
        when(service.getAllFileCsv()).thenReturn(data);

        List<Csv> out = mapper.ListCSv(service);

        assertSame(data, out);
        verify(service).getAllFileCsv();
    }

    @Test
    void addCSVToDataBase_callsService() {
        CsvService service = mock(CsvService.class);

        Csv c = new Csv("z.csv", "u9", "DATA".getBytes(StandardCharsets.UTF_8));

        mapper.addCSVToDataBase(service, c);

        verify(service).addNewFileCSV(c);
    }

    @Test
    void deleteCsvEntityFromData_callsServiceWithMappedCsv() {
        CsvService service = mock(CsvService.class);
        byte[] data = "DEL".getBytes(StandardCharsets.UTF_8);

        CsvDto dto = new CsvDto(99, "del.csv", "uX", data);

        mapper.deleteCsvEntityFromData(service, dto);

        verify(service).deleteFileCsv(argThat(c ->
                c != null
                && Integer.valueOf(99).equals(c.getId())
                && "del.csv".equals(c.getFileName())
                && "uX".equals(c.getOwnerId())
                && Arrays.equals(data, c.getData())
        ));
    }
}
