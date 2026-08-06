package com.ocp.jph.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ocp.jph.entity.Responsable;
import com.ocp.jph.entity.Secteur;
import com.ocp.jph.entity.Statut;
import com.ocp.jph.entity.Udm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReportingDto {

    private Long id;

    @NotBlank
    private String numeroDA;

    @NotBlank
    private String numeroDossier;

    @NotBlank
    private String numero;

    @NotBlank
    private String codeOracle;

    @NotBlank
    private String codeSAP;

    @Size(max = 2000)
    private String description;

    private Udm uniteDeMesure;

    private Double quantite;

    @NotNull
    private Secteur secteur;

    @NotBlank
    private String commande;

    @NotBlank
    private String fournisseur;

    private Integer pourcentageLivraison;

    private Integer delaiLivraison;

    private LocalDate dateNotification;

    private LocalDate datePrevisionnelle;

    @NotNull
    private Statut statut;

    @NotNull
    private Responsable responsable;

    @NotNull
    private Long utilisateurId;

    @Size(max = 2000)
    private String commentaire;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDA() {
        return numeroDA;
    }

    public void setNumeroDA(String numeroDA) {
        this.numeroDA = numeroDA;
    }

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodeOracle() {
        return codeOracle;
    }

    public void setCodeOracle(String codeOracle) {
        this.codeOracle = codeOracle;
    }

    public String getCodeSAP() {
        return codeSAP;
    }

    public void setCodeSAP(String codeSAP) {
        this.codeSAP = codeSAP;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Udm getUniteDeMesure() {
        return uniteDeMesure;
    }

    public void setUniteDeMesure(Udm uniteDeMesure) {
        this.uniteDeMesure = uniteDeMesure;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Secteur getSecteur() {
        return secteur;
    }

    public void setSecteur(Secteur secteur) {
        this.secteur = secteur;
    }

    public String getCommande() {
        return commande;
    }

    public void setCommande(String commande) {
        this.commande = commande;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Integer getPourcentageLivraison() {
        return pourcentageLivraison;
    }

    public void setPourcentageLivraison(Integer pourcentageLivraison) {
        this.pourcentageLivraison = pourcentageLivraison;
    }

    public Integer getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(Integer delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public LocalDate getDateNotification() {
        return dateNotification;
    }

    public void setDateNotification(LocalDate dateNotification) {
        this.dateNotification = dateNotification;
    }

    public LocalDate getDatePrevisionnelle() {
        return datePrevisionnelle;
    }

    public void setDatePrevisionnelle(LocalDate datePrevisionnelle) {
        this.datePrevisionnelle = datePrevisionnelle;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
