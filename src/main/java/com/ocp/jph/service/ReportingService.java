package com.ocp.jph.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ocp.jph.domain.Reporting;
import com.ocp.jph.repository.ReportingRepository;

@Service
public class ReportingService {
    private final ReportingRepository repo;

    public ReportingService(ReportingRepository repo) {
        this.repo = repo;
    }

    public List<Reporting> findAll() {
        return repo.findAll();
    }

    public Optional<Reporting> findById(Long id) {
        return repo.findById(id);
    }

    public Reporting save(Reporting reporting) {
        return repo.save(reporting);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
