package it.unibg.progetto.api.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.api.mapping.CsvMapper;
import it.unibg.progetto.api.security.session.Session;
import it.unibg.progetto.data.Csv;
import it.unibg.progetto.service.CsvService;

class CsvUseCaseUnitTest {

	@Test
	void conversionFromCsvdtoToCsvNoId_delegatesToMapper() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		CsvDto dto = new CsvDto("file", "owner", new byte[] { 1, 2 });
		Csv expected = mock(Csv.class);

		when(csvMapper.toCsvFromCsvDtoNoId(dto)).thenReturn(expected);

		Csv result = useCase.conversionFromCsvdtoToCsvNoId(dto);

		assertSame(expected, result);
		verify(csvMapper).toCsvFromCsvDtoNoId(dto);
		verifyNoMoreInteractions(csvMapper);
	}

	@Test
	void conversionFromCsvdtoToCsvWithId_delegatesToMapper() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		CsvDto dto = new CsvDto(7, "file", "owner", new byte[] { 9 });
		Csv expected = mock(Csv.class);

		when(csvMapper.toCsvFromCsvDtoWithId(dto)).thenReturn(expected);

		Csv result = useCase.conversionFromCsvdtoToCsvWithId(dto);

		assertSame(expected, result);
		verify(csvMapper).toCsvFromCsvDtoWithId(dto);
		verifyNoMoreInteractions(csvMapper);
	}

	@Test
	void conversionFromCsvToCsvDto_delegatesToMapper() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		Csv csv = mock(Csv.class);
		CsvDto expected = new CsvDto("file", "owner", new byte[] { 1 });

		when(csvMapper.csvConverterFromData(csv)).thenReturn(expected);

		CsvDto result = useCase.conversionFromCsvToCsvDto(csv);

		assertSame(expected, result);
		verify(csvMapper).csvConverterFromData(csv);
		verifyNoMoreInteractions(csvMapper);
	}

	@Test
	void returnOnlyUserThatHaveAFile_returnsUniqueOwnerIds() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		CsvDto a1 = new CsvDto("f1", "A", new byte[] { 1 });
		CsvDto a2 = new CsvDto("f2", "A", new byte[] { 2 });
		CsvDto b1 = new CsvDto("f3", "B", new byte[] { 3 });

		List<String> owners = useCase.returnOnlyUserThatHaveAFile(Arrays.asList(a1, a2, b1));

		assertEquals(Arrays.asList("A", "B"), owners);
	}

	@Test
	void returnAllFileCsvDtoFromData_delegatesToMapper() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		List<CsvDto> expected = Arrays.asList(new CsvDto("f1", "U1", new byte[] { 1 }),
				new CsvDto("f2", "U2", new byte[] { 2 }));

		when(csvMapper.ListCsvFromDatabase(csvService)).thenReturn(expected);

		List<CsvDto> result = useCase.returnAllFileCsvDtoFromData();

		assertSame(expected, result);
		verify(csvMapper).ListCsvFromDatabase(csvService);
		verifyNoMoreInteractions(csvMapper);
	}

	@Test
	void returnAllFileCsvDtoFromDataOfUser_delegatesToMapper() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		String uuid = "U1";
		List<CsvDto> expected = Arrays.asList(new CsvDto("f1", uuid, new byte[] { 1 }),
				new CsvDto("f2", uuid, new byte[] { 2 }));

		when(csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuid)).thenReturn(expected);

		List<CsvDto> result = useCase.returnAllFileCsvDtoFromDataOfUser(uuid);

		assertSame(expected, result);
		verify(csvMapper).ListCsvFromDatabaseWithSpecificUUID(csvService, uuid);
		verifyNoMoreInteractions(csvMapper);
	}

	@Test
	void returnListCsv_delegatesToMapper() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		@SuppressWarnings("unchecked")
		List<Csv> expected = (List<Csv>) mock(List.class);

		when(csvMapper.ListCSv(csvService)).thenReturn(expected);

		List<Csv> result = useCase.returnListCsv();

		assertSame(expected, result);
		verify(csvMapper).ListCSv(csvService);
		verifyNoMoreInteractions(csvMapper);
	}

	@Test
	void addNewFileInCsvTableFromCsvDto_callsMapperAddCSVToDataBase() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);

		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		CsvDto dto = new CsvDto("file", "U1", new byte[] { 1 });
		Csv converted = mock(Csv.class);

		doReturn(converted).when(useCase).conversionFromCsvdtoToCsvNoId(dto);

		useCase.addNewFileInCsvTableFromCsvDto(dto);

		verify(csvMapper).addCSVToDataBase(csvService, converted);
		verifyNoMoreInteractions(csvMapper);
	}

	@Test
	void deleteOneFileInData_delegatesToMapper() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		CsvDto dto = new CsvDto(10, "file", "U1", new byte[] { 1 });

		useCase.deleteOneFileInData(dto);

		verify(csvMapper).deleteCsvEntityFromData(csvService, dto);
		verifyNoMoreInteractions(csvMapper);
	}

	@Test
	void checknameFileAlreadyExistOnlyInData_returnsTrueWhenNameMatches() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = new CsvUseCase(csvService, csvMapper);

		String uuid = "U1";
		List<CsvDto> list = Arrays.asList(new CsvDto("doc1", uuid, new byte[] { 1 }),
				new CsvDto("doc2", uuid, new byte[] { 2 }));

		when(csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuid)).thenReturn(list);

		assertTrue(useCase.checknameFileAlreadyExistOnlyInData("doc2", uuid));
		assertFalse(useCase.checknameFileAlreadyExistOnlyInData("missing", uuid));

		verify(csvMapper, times(2)).ListCsvFromDatabaseWithSpecificUUID(csvService, uuid);
	}

	@Test
	void checknameFileAlreadyExist_returnsTrueIfExistsInFolderOrData() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		doReturn(false).when(useCase).checknameFileAlreadyExistOnlyInFolder("doc");
		doReturn(true).when(useCase).checknameFileAlreadyExistOnlyInData("doc", "U1");
		assertTrue(useCase.checknameFileAlreadyExist("doc", "U1"));

		doReturn(true).when(useCase).checknameFileAlreadyExistOnlyInFolder("doc2");
		assertTrue(useCase.checknameFileAlreadyExist("doc2", "U1"));
	}

	@Test
	void deleteMyCsvByIndex_callsDeleteOneFileInData_whenIndexValid() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);

		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		Session session = mock(Session.class);
		when(session.getUuid()).thenReturn("U1");

		CsvDto dto0 = new CsvDto("f0", "U1", new byte[] { 1 });
		CsvDto dto1 = new CsvDto("f1", "U1", new byte[] { 2 });

		doReturn(Arrays.asList(dto0, dto1)).when(useCase).returnAllFileCsvDtoFromDataOfUser("U1");
		doNothing().when(useCase).deleteOneFileInData(any(CsvDto.class));

		boolean ok = useCase.deleteMyCsvByIndex(1, session);

		assertTrue(ok);
		verify(useCase).deleteOneFileInData(dto1);
	}

	@Test
	void deleteMyCsvByIndex_returnsFalse_whenIndexInvalidOrEmpty() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		Session session = mock(Session.class);
		when(session.getUuid()).thenReturn("U1");

		// lista vuota
		doReturn(Collections.emptyList()).when(useCase).returnAllFileCsvDtoFromDataOfUser("U1");
		assertFalse(useCase.deleteMyCsvByIndex(0, session));

		// indice invalido
		CsvDto dto0 = new CsvDto("f0", "U1", new byte[] { 1 });
		doReturn(Arrays.asList(dto0)).when(useCase).returnAllFileCsvDtoFromDataOfUser("U1");
		assertFalse(useCase.deleteMyCsvByIndex(-1, session));
		assertFalse(useCase.deleteMyCsvByIndex(5, session));

		verify(useCase, never()).deleteOneFileInData(any());
	}

	@Test
	void stampListOfMyCsv_returnsTrue_whenUserHasFiles() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		String uuid = "U1";
		doReturn(Arrays.asList(new CsvDto("f1", uuid, new byte[] { 1 }), new CsvDto("f2", uuid, new byte[] { 2 })))
				.when(useCase).returnAllFileCsvDtoFromDataOfUser(uuid);

		assertTrue(useCase.stampListOfMyCsv(uuid));
	}

	@Test
	void stampListOfMyCsv_returnsFalse_whenUserHasNoFiles() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		String uuid = "U1";

		doReturn(Collections.emptyList()).when(useCase).returnAllFileCsvDtoFromDataOfUser(uuid);

		assertFalse(useCase.stampListOfMyCsv(uuid));
	}

	@Test
	void deleteAllFileOfUserDeleted_deletesAllCsvOfUser() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		String uuid = "U1";

		CsvDto d1 = new CsvDto("f1", uuid, new byte[] { 1 });
		CsvDto d2 = new CsvDto("f2", uuid, new byte[] { 2 });

		doReturn(Arrays.asList(d1, d2)).when(useCase).returnAllFileCsvDtoFromDataOfUser(uuid);

		doNothing().when(useCase).deleteOneFileInData(any(CsvDto.class));

		useCase.deleteAllFileOfUserDeleted(uuid);

		verify(useCase).deleteOneFileInData(d1);
		verify(useCase).deleteOneFileInData(d2);
	}

	@Test
	void checknameFileAlreadyExist_returnsFalse_whenNotInFolderNorData() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		doReturn(false).when(useCase).checknameFileAlreadyExistOnlyInFolder("doc");
		doReturn(false).when(useCase).checknameFileAlreadyExistOnlyInData("doc", "U1");

		assertFalse(useCase.checknameFileAlreadyExist("doc", "U1"));
	}

	@Test
	void deleteAllFileOfUserDeleted_doesNothing_whenUserHasNoFiles() {
		CsvService csvService = mock(CsvService.class);
		CsvMapper csvMapper = mock(CsvMapper.class);
		CsvUseCase useCase = spy(new CsvUseCase(csvService, csvMapper));

		doReturn(Collections.emptyList()).when(useCase).returnAllFileCsvDtoFromDataOfUser("U1");

		useCase.deleteAllFileOfUserDeleted("U1");

		verify(useCase, never()).deleteOneFileInData(any());
	}

}
