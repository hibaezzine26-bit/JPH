package com.ocp.jph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ocp.jph.entity.Reporting;

@Repository
public interface ReportingRepository extends JpaRepository<Reporting, Long> {

}
