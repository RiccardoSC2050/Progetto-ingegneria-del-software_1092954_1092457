package it.unibg.progetto.api.infrastructure.csv;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.cli.components.Constant;
import it.unibg.progetto.api.cli.components.GlobalScanner;

class CsvFileManagerTest {

	@TempDir
	Path tempDir;

	@AfterEach
	void resetScanner() {
		GlobalScanner.scanner = new Scanner(System.in);
	}

	private String tempPathWithSlash() {
		String p = tempDir.toAbsolutePath().toString();
		return p.endsWith("/") ? p : (p + "/");
	}

	@Test
	void getTempFolder_createsFolderIfNotExists() throws Exception {
		try (MockedStatic<Constant> c = mockStatic(Constant.class)) {
			c.when(Constant::getFilePathCsv).thenReturn(tempPathWithSlash());

			assertEquals(tempDir.toFile().getAbsolutePath(), CsvFileManager.getTempFolder().getAbsolutePath());
			assertTrue(CsvFileManager.getTempFolder().exists());
			assertTrue(CsvFileManager.getTempFolder().isDirectory());
		}
	}

	@Test
	void getTempCsvFile_returnsCorrectFileInTempFolder() throws Exception {
		try (MockedStatic<Constant> c = mockStatic(Constant.class)) {
			c.when(Constant::getFilePathCsv).thenReturn(tempPathWithSlash());

			assertEquals(tempDir.resolve("abc.csv").toFile().getAbsolutePath(),
					CsvFileManager.getTempCsvFile("abc").getAbsolutePath());
		}
	}

	@Test
	void createFileCsvOnFolder_createsEmptyCsvFile() throws Exception {
		try (MockedStatic<Constant> c = mockStatic(Constant.class)) {
			c.when(Constant::getFilePathCsv).thenReturn(tempPathWithSlash());

			CsvFileManager.createFileCsvOnFolder("file1");
			Path f = tempDir.resolve("file1.csv");

			assertTrue(Files.exists(f));
			assertEquals(0, Files.size(f));
		}
	}

	@Test
	void readHeader_returnsFirstLine() throws Exception {
		try (MockedStatic<Constant> c = mockStatic(Constant.class)) {
			c.when(Constant::getFilePathCsv).thenReturn(tempPathWithSlash());

			Path f = tempDir.resolve("h.csv");
			Files.write(f, Arrays.asList("A,B,C", "1,2,3"), StandardCharsets.UTF_8);

			String header = CsvFileManager.readHeader("h");

			assertEquals("A,B,C", header);
		}
	}

	@Test
	void readAllRows_readsAllRows_andCanSkipHeader() throws Exception {
		try (MockedStatic<Constant> c = mockStatic(Constant.class)) {
			c.when(Constant::getFilePathCsv).thenReturn(tempPathWithSlash());

			Path f = tempDir.resolve("data.csv");
			Files.write(f, Arrays.asList("ID,NOME,COGNOME", "1,Mario,Rossi", "2,Anna,"

			), StandardCharsets.UTF_8);

			List<String[]> all = CsvFileManager.readAllRows("data", false);
			assertEquals(3, all.size());

			assertArrayEquals(new String[] { "ID", "NOME", "COGNOME" }, all.get(0));

			assertArrayEquals(new String[] { "2", "Anna" }, 
					all.get(2));

			List<String[]> noHeader = CsvFileManager.readAllRows("data", true);
			assertEquals(2, noHeader.size());

			assertArrayEquals(new String[] { "1", "Mario", "Rossi" }, noHeader.get(0));

			assertArrayEquals(new String[] { "2", "Anna" },
					noHeader.get(1));
		}
	}

	@Test
	void printRows_printsTableLikeOutput() throws Exception {
	
		List<String[]> rows = Arrays.asList(new String[] { "ID", "NOME" }, new String[] { "1", "Mario" });

		PrintStream oldOut = System.out;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos, true, "UTF-8"));

		try {
			CsvFileManager.printRows(rows);
		} finally {
			System.setOut(oldOut);
		}

		String out = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		assertTrue(out.contains("ID"));
		assertTrue(out.contains("Mario"));
		
		assertTrue(out.contains("|") || out.contains("----"));
	}

	@Test
	void writeCsvLikeEditor_appendsLinesUntilWq() throws Exception {
		try (MockedStatic<Constant> c = mockStatic(Constant.class)) {
			c.when(Constant::getFilePathCsv).thenReturn(tempPathWithSlash());

			Path f = tempDir.resolve("edit.csv");
			Files.write(f, Arrays.asList("HEADER1,HEADER2"), StandardCharsets.UTF_8);

			
			String input = "1,Mario\n2,Anna\n:wq\n";
			GlobalScanner.scanner = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

			CsvFileManager.writeCsvLikeEditor(f.toString());

			String content = new String(Files.readAllBytes(f), StandardCharsets.UTF_8);
			assertTrue(content.contains("HEADER1,HEADER2"));
			assertTrue(content.contains("1,Mario"));
			assertTrue(content.contains("2,Anna"));
		}
	}

	@Test
	void readFileCsv_printsFileContent() throws Exception {
		try (MockedStatic<Constant> c = mockStatic(Constant.class)) {
			c.when(Constant::getFilePathCsv).thenReturn(tempPathWithSlash());

			Path f = tempDir.resolve("print.csv");
			Files.write(f, Arrays.asList("ID,NOME", "1,Mario"), StandardCharsets.UTF_8);

			PrintStream oldOut = System.out;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(baos, true, "UTF-8"));

			try {
				CsvFileManager.readFileCsv("print");
			} finally {
				System.setOut(oldOut);
			}

			String out = new String(baos.toByteArray(), StandardCharsets.UTF_8);
			assertTrue(out.contains("ID"));
			assertTrue(out.contains("Mario"));
		}
	}
}
