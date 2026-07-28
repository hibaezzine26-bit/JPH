package com.ocp.jph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ocp.jph.domain.ImportExcel;

@Repository
public interface ImportExcelRepository extends JpaRepository<ImportExcel, Long> {

}
