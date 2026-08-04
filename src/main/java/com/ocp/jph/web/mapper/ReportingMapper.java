package com.ocp.jph.web.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ocp.jph.domain.Reporting;
import com.ocp.jph.domain.Utilisateur;
import com.ocp.jph.dto.ReportingDto;

@Component
public class ReportingMapper {
    public ReportingMapper() {
    }

    public ReportingDto toDto(Reporting reporting) {
        if (reporting == null) {
            return null;
        }
        ReportingDto dto = new ReportingDto();
        dto.setId(reporting.getId());
        dto.setNumeroDA(reporting.getNumeroDA());
        dto.setNumeroDossier(reporting.getNumeroDossier());
        dto.setNumero(reporting.getNumero());
        dto.setCodeOracle(reporting.getCodeOracle());
        dto.setCodeSAP(reporting.getCodeSAP());
        dto.setDescription(reporting.getDescription());
        dto.setUniteDeMesure(reporting.getUniteDeMesure());
        dto.setQuantite(reporting.getQuantite());
        dto.setSecteur(reporting.getSecteur());
        dto.setCommande(reporting.getCommande());
        dto.setFournisseur(reporting.getFournisseur());
        dto.setPourcentageLivraison(reporting.getPourcentageLivraison());
        dto.setDelaiLivraison(reporting.getDelaiLivraison());
        dto.setDateNotification(reporting.getDateNotification());
        dto.setDatePrevisionnelle(reporting.getDatePrevisionnelle());
        dto.setStatut(reporting.getStatut());
        dto.setResponsable(reporting.getResponsable());
        dto.setUtilisateurId(Optional.ofNullable(reporting.getUtilisateur()).map(Utilisateur::getId).orElse(null));
        dto.setCommentaire(reporting.getCommentaire());
        dto.setDateCreation(reporting.getDateCreation());
        dto.setDateModification(reporting.getDateModification());
        return dto;
    }

    public Reporting toEntity(ReportingDto dto) {
        if (dto == null) {
            return null;
        }
        Reporting reporting = new Reporting();
        reporting.setId(dto.getId());
        reporting.setNumeroDA(dto.getNumeroDA());
        reporting.setNumeroDossier(dto.getNumeroDossier());
        reporting.setNumero(dto.getNumero());
        reporting.setCodeOracle(dto.getCodeOracle());
        reporting.setCodeSAP(dto.getCodeSAP());
        reporting.setDescription(dto.getDescription());
        reporting.setUniteDeMesure(dto.getUniteDeMesure());
        reporting.setQuantite(dto.getQuantite());
        reporting.setSecteur(dto.getSecteur());
        reporting.setCommande(dto.getCommande());
        reporting.setFournisseur(dto.getFournisseur());
        reporting.setPourcentageLivraison(dto.getPourcentageLivraison());
        reporting.setDelaiLivraison(dto.getDelaiLivraison());
        reporting.setDateNotification(dto.getDateNotification());
        reporting.setDatePrevisionnelle(dto.getDatePrevisionnelle());
        reporting.setStatut(dto.getStatut());
        reporting.setResponsable(dto.getResponsable());
        reporting.setCommentaire(dto.getCommentaire());
        if (dto.getUtilisateurId() != null) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(dto.getUtilisateurId());
            reporting.setUtilisateur(utilisateur);
        }
        reporting.setDateCreation(dto.getDateCreation());
        reporting.setDateModification(dto.getDateModification());
        return reporting;
    }
}
