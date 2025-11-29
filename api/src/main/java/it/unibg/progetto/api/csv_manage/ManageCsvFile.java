package it.unibg.progetto.api.csv_manage;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ManageCsvFile {

	public ManageCsvFile() {
	}

	/**
	 * crea un file csv con un nome specifico
	 * 
	 * @param nameFile
	 */
	public static void createFileCsvOnFolder(String nameFile) {
		String p = "../api/temporary_fileCSV_saving/" + nameFile + ".csv";

		try (FileWriter fileCsv = new FileWriter(p)) {

		} catch (Exception e) {

		}
	}

}
