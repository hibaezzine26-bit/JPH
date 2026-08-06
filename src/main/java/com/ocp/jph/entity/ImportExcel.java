package com.ocp.jph.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ImportExcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomFichier;

    private LocalDateTime dateImport;

    private Integer nombreLignes;

    private String statutImport;

    public ImportExcel() {}

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
