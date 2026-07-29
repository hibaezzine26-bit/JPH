package com.ocp.jph.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ocp.jph.domain.Historique;
import com.ocp.jph.repository.HistoriqueRepository;

@Service
public class HistoriqueService {
    private final HistoriqueRepository repo;

    public HistoriqueService(HistoriqueRepository repo) {
        this.repo = repo;
    }

    public List<Historique> findAll() {
        return repo.findAll();
    }

    public Optional<Historique> findById(Long id) {
        return repo.findById(id);
    }

    public List<Historique> findByReportingId(Long reportingId) {
        return repo.findByReportingIdOrderByDateActionDesc(reportingId);
    }

    public Historique save(Historique h) {
        return repo.save(h);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
