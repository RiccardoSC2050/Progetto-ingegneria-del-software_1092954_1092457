package it.unibg.progetto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unibg.progetto.data.Csv;
import it.unibg.progetto.data.CsvRepository;

@Service
public class CsvService {

    private final CsvRepository repository;

    public CsvService(CsvRepository repository) {
        this.repository = repository;
    }

    // <<< questo è quello usato dal mapper >>>
    public Csv addNewFileCSV(Csv c) {
        return repository.save(c);
    }

    public List<Csv> getAllFileCsv() {
        return repository.findAll();
    }

    public void deleteFileCsv(Csv csv) {
        repository.delete(csv);
    }
}
