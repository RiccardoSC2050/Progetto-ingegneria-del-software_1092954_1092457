package it.unibg.progetto.api.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import it.unibg.progetto.api.app.ApiMain;
import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.data.Csv;

@ActiveProfiles("test")
@SpringBootTest(classes = ApiMain.class)
class CsvUseCaseIntegrationTest {

	@Autowired
	private CsvUseCase csvUseCase;

	@BeforeEach
	@AfterEach
	void cleanCsvTable() {
		// pulizia robusta usando solo metodi del use case (così non dipendiamo dal
		// service)
		List<CsvDto> all = csvUseCase.returnAllFileCsvDtoFromData();
		if (all != null) {
			for (CsvDto dto : all) {
				csvUseCase.deleteOneFileInData(dto);
			}
		}
	}

	@Test
	void addNewFileInCsvTableFromCsvDto_persistsAndIsReadable() {
		String ownerId = "U_" + System.currentTimeMillis();
		String fileName = "file_" + System.currentTimeMillis();
		byte[] data = new byte[] { 1, 2, 3 };

		CsvDto dto = new CsvDto(fileName, ownerId, data);

		csvUseCase.addNewFileInCsvTableFromCsvDto(dto);

		List<CsvDto> all = csvUseCase.returnAllFileCsvDtoFromData();
		assertNotNull(all);

		assertTrue(all.stream().anyMatch(c -> fileName.equals(c.getFileName()) && ownerId.equals(c.getOwnerId())),
				"Il CSV appena inserito deve essere presente in tabella");
	}

	@Test
	void returnAllFileCsvDtoFromDataOfUser_filtersByOwnerId() {
		String ownerA = "A_" + System.currentTimeMillis();
		String ownerB = "B_" + System.currentTimeMillis();

		csvUseCase.addNewFileInCsvTableFromCsvDto(
				new CsvDto("a1_" + System.currentTimeMillis(), ownerA, new byte[] { 1 }));
		csvUseCase.addNewFileInCsvTableFromCsvDto(
				new CsvDto("a2_" + System.currentTimeMillis(), ownerA, new byte[] { 2 }));
		csvUseCase.addNewFileInCsvTableFromCsvDto(
				new CsvDto("b1_" + System.currentTimeMillis(), ownerB, new byte[] { 3 }));

		List<CsvDto> listA = csvUseCase.returnAllFileCsvDtoFromDataOfUser(ownerA);
		List<CsvDto> listB = csvUseCase.returnAllFileCsvDtoFromDataOfUser(ownerB);

		assertNotNull(listA);
		assertNotNull(listB);

		assertEquals(2, listA.size(), "OwnerA deve avere 2 file");
		assertEquals(1, listB.size(), "OwnerB deve avere 1 file");

		assertTrue(listA.stream().allMatch(c -> ownerA.equals(c.getOwnerId())));
		assertTrue(listB.stream().allMatch(c -> ownerB.equals(c.getOwnerId())));
	}

	@Test
	void deleteOneFileInData_removesSingleRow() {
		String owner = "U_" + System.currentTimeMillis();
		String fileName1 = "f1_" + System.currentTimeMillis();
		String fileName2 = "f2_" + System.currentTimeMillis();

		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto(fileName1, owner, new byte[] { 1 }));
		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto(fileName2, owner, new byte[] { 2 }));

		List<CsvDto> all = csvUseCase.returnAllFileCsvDtoFromData();
		CsvDto toDelete = all.stream().filter(c -> fileName1.equals(c.getFileName()) && owner.equals(c.getOwnerId()))
				.findFirst().orElse(null);

		assertNotNull(toDelete, "Devo trovare il record da eliminare");

		csvUseCase.deleteOneFileInData(toDelete);

		List<CsvDto> after = csvUseCase.returnAllFileCsvDtoFromData();
		assertTrue(after.stream().noneMatch(c -> fileName1.equals(c.getFileName()) && owner.equals(c.getOwnerId())));
		assertTrue(after.stream().anyMatch(c -> fileName2.equals(c.getFileName()) && owner.equals(c.getOwnerId())));
	}

	@Test
	void deleteAllFileOfUserDeleted_removesAllRowsOfThatOwner() {
		String owner = "U_" + System.currentTimeMillis();
		String other = "OTHER_" + System.currentTimeMillis();

		csvUseCase.addNewFileInCsvTableFromCsvDto(
				new CsvDto("u1_" + System.currentTimeMillis(), owner, new byte[] { 1 }));
		csvUseCase.addNewFileInCsvTableFromCsvDto(
				new CsvDto("u2_" + System.currentTimeMillis(), owner, new byte[] { 2 }));
		csvUseCase.addNewFileInCsvTableFromCsvDto(
				new CsvDto("x1_" + System.currentTimeMillis(), other, new byte[] { 9 }));

		csvUseCase.deleteAllFileOfUserDeleted(owner);

		List<CsvDto> ownerList = csvUseCase.returnAllFileCsvDtoFromDataOfUser(owner);
		List<CsvDto> otherList = csvUseCase.returnAllFileCsvDtoFromDataOfUser(other);

		assertNotNull(ownerList);
		assertNotNull(otherList);

		assertEquals(0, ownerList.size(), "Tutti i file dell'utente eliminato devono sparire");
		assertEquals(1, otherList.size(), "I file degli altri utenti devono rimanere");
	}

	@Test
	void returnListCsv_returnsEntitiesFromDb() {
		csvUseCase.addNewFileInCsvTableFromCsvDto(
				new CsvDto("ent_" + System.currentTimeMillis(), "U_" + System.currentTimeMillis(), new byte[] { 1 }));

		List<Csv> entities = csvUseCase.returnListCsv();

		assertNotNull(entities);
		assertFalse(entities.isEmpty(), "Deve esserci almeno una entity Csv nel DB test");
	}

	@Test
	void deleteOneFileInData_removesCorrectRowFromDatabase_equivalentToIndexDelete() {
		String owner = "IDX_" + System.currentTimeMillis();

		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto("f1", owner, new byte[] { 1 }));
		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto("f2", owner, new byte[] { 2 }));

		List<CsvDto> all = csvUseCase.returnAllFileCsvDtoFromDataOfUser(owner);
		assertEquals(2, all.size());

		// equivalente logico all’index=1
		CsvDto toDelete = all.get(1);
		csvUseCase.deleteOneFileInData(toDelete);

		List<CsvDto> remaining = csvUseCase.returnAllFileCsvDtoFromDataOfUser(owner);
		assertEquals(1, remaining.size());
		assertEquals("f1", remaining.get(0).getFileName());
	}

	@Test
	void stampListOfMyCsv_returnsCorrectValueWithDatabase() {
		String owner = "STAMP_" + System.currentTimeMillis();

		assertFalse(csvUseCase.stampListOfMyCsv(owner));

		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto("f1", owner, new byte[] { 1 }));

		assertTrue(csvUseCase.stampListOfMyCsv(owner));
	}

	@Test
	void checknameFileAlreadyExistOnlyInData_worksWithDatabase() {
		String owner = "CHK_" + System.currentTimeMillis();
		String name = "exists_" + System.currentTimeMillis();

		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto(name, owner, new byte[] { 1 }));

		assertTrue(csvUseCase.checknameFileAlreadyExistOnlyInData(name, owner));
		assertFalse(csvUseCase.checknameFileAlreadyExistOnlyInData("missing", owner));
	}

	@Test
	void returnOnlyUserThatHaveAFile_returnsDistinctOwnersFromDb() {
		String u1 = "U1_" + System.currentTimeMillis();
		String u2 = "U2_" + System.currentTimeMillis();

		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto("a", u1, new byte[] { 1 }));
		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto("b", u1, new byte[] { 2 }));
		csvUseCase.addNewFileInCsvTableFromCsvDto(new CsvDto("c", u2, new byte[] { 3 }));

		List<CsvDto> all = csvUseCase.returnAllFileCsvDtoFromData();
		List<String> owners = csvUseCase.returnOnlyUserThatHaveAFile(all);

		assertEquals(2, owners.size());
		assertTrue(owners.contains(u1));
		assertTrue(owners.contains(u2));
	}

}
