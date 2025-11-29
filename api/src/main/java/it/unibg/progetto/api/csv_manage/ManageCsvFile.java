package it.unibg.progetto.api.csv_manage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

	public static void readFileCsv(String name) {
		String p = "../api/temporary_fileCSV_saving/" + name + ".csv";
		try (BufferedReader br = new BufferedReader(new FileReader(p))) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] columns = line.split(",");

				// stampa in formato tabella
				for (String col : columns) {
					System.out.print(col + "\t"); // \t = tabulazione
				}
				System.out.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
