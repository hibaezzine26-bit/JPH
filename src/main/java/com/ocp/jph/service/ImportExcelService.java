package com.ocp.jph.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ocp.jph.domain.ImportExcel;
import com.ocp.jph.repository.ImportExcelRepository;

@Service
public class ImportExcelService {
    private final ImportExcelRepository repo;

    public ImportExcelService(ImportExcelRepository repo) {
        this.repo = repo;
    }

    public List<ImportExcel> findAll() {
        return repo.findAll();
    }

    public Optional<ImportExcel> findById(Long id) {
        return repo.findById(id);
    }

    public ImportExcel save(ImportExcel e) {
        return repo.save(e);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
