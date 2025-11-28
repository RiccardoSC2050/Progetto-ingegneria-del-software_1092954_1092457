package it.unibg.progetto.api.application;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.csv_manage.ActionOnCsv;

public class AppBlocksManageCsv {

	public AppBlocksManageCsv() {
	}

	public void importMainFile() throws IOException {
		try {
			System.out.println("inserisci file aziendale di riferimento:");
			System.out.println("inserire percorso file");
			String path = GlobalScaner.scanner.nextLine();

			ActionOnCsv.getIstnce().importFileFromLocalPc(path);
		} catch (Exception e) {
		}
	}

}
