package com.ocp.jph.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ImportExcelDto {

    private Long id;

    @NotBlank
    private String nomFichier;

    @NotNull
    private LocalDateTime dateImport;

    @NotNull
    private Integer nombreLignes;

    @NotBlank
    private String statutImport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public LocalDateTime getDateImport() {
        return dateImport;
    }

    public void setDateImport(LocalDateTime dateImport) {
        this.dateImport = dateImport;
    }

    public Integer getNombreLignes() {
        return nombreLignes;
    }

    public void setNombreLignes(Integer nombreLignes) {
        this.nombreLignes = nombreLignes;
    }

    public String getStatutImport() {
        return statutImport;
    }

    public void setStatutImport(String statutImport) {
        this.statutImport = statutImport;
    }
}
