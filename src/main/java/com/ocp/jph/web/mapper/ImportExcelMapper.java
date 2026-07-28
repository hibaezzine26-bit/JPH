package com.ocp.jph.web.mapper;

import org.springframework.stereotype.Component;

import com.ocp.jph.domain.ImportExcel;
import com.ocp.jph.dto.ImportExcelDto;

@Component
public class ImportExcelMapper {

    public ImportExcelDto toDto(ImportExcel importExcel) {
        if (importExcel == null) {
            return null;
        }
        ImportExcelDto dto = new ImportExcelDto();
        dto.setId(importExcel.getId());
        dto.setNomFichier(importExcel.getNomFichier());
        dto.setDateImport(importExcel.getDateImport());
        dto.setNombreLignes(importExcel.getNombreLignes());
        dto.setStatutImport(importExcel.getStatutImport());
        return dto;
    }

    public ImportExcel toEntity(ImportExcelDto dto) {
        if (dto == null) {
            return null;
        }
        ImportExcel importExcel = new ImportExcel();
        importExcel.setId(dto.getId());
        importExcel.setNomFichier(dto.getNomFichier());
        importExcel.setDateImport(dto.getDateImport());
        importExcel.setNombreLignes(dto.getNombreLignes());
        importExcel.setStatutImport(dto.getStatutImport());
        return importExcel;
    }
}
