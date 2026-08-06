package com.ocp.jph.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Reporting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroDA;
    private String numeroDossier;
    private String numero;
    private String codeOracle;
    private String codeSAP;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Udm uniteDeMesure;

    private Double quantite;

    @Enumerated(EnumType.STRING)
    private Secteur secteur;

    private String commande;
    private String fournisseur;

    private Integer pourcentageLivraison;
    private Integer delaiLivraison;
    private LocalDate dateNotification;
    private LocalDate datePrevisionnelle;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Enumerated(EnumType.STRING)
    private Responsable responsable;


    @ManyToOne
    private Utilisateur utilisateur;

    @Column(length = 2000)
    private String commentaire;

    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "reporting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Historique> historiques = new ArrayList<>();

    public Reporting() {}

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
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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

    public List<Historique> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<Historique> historiques) {
        this.historiques = historiques;
    }
}
