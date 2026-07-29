package com.ocp.jph.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ocp.jph.domain.Historique;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique, Long> {
    List<Historique> findByReportingIdOrderByDateActionDesc(Long reportingId);
}
