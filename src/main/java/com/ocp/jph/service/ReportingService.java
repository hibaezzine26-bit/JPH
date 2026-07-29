package com.ocp.jph.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.ocp.jph.domain.Historique;
import com.ocp.jph.domain.Reporting;
import com.ocp.jph.domain.Responsable;
import com.ocp.jph.domain.Secteur;
import com.ocp.jph.domain.Statut;
import com.ocp.jph.dto.ReportingStatisticsDto;
import com.ocp.jph.repository.ReportingRepository;

@Service
public class ReportingService {
    private final ReportingRepository repo;

    public ReportingService(ReportingRepository repo) {
        this.repo = repo;
    }

    public List<Reporting> findAll() {
        return repo.findAll();
    }

    public List<Reporting> findAll(String search, Statut statut, Secteur secteur, Responsable responsable, String sort) {
        return sortReportings(filterReportings(repo.findAll(), search, statut, secteur, responsable), sort);
    }

    public String export(String search, Statut statut, Secteur secteur, Responsable responsable, String sort) {
        List<Reporting> reportings = findAll(search, statut, secteur, responsable, sort);
        String header = "id,numeroDA,numeroDossier,statut,secteur,responsable,quantite,dateCreation,dateModification";
        String rows = reportings.stream().map(this::toCsvLine).collect(Collectors.joining("\n"));
        return header + (rows.isEmpty() ? "" : "\n" + rows);
    }

    private List<Reporting> filterReportings(List<Reporting> reportings, String search, Statut statut, Secteur secteur, Responsable responsable) {
        return reportings.stream()
                .filter(reporting -> matchesStatut(reporting, statut))
                .filter(reporting -> matchesSecteur(reporting, secteur))
                .filter(reporting -> matchesResponsable(reporting, responsable))
                .filter(reporting -> matchesSearch(reporting, search))
                .collect(Collectors.toList());
    }

    private boolean matchesStatut(Reporting reporting, Statut statut) {
        return statut == null || statut.equals(reporting.getStatut());
    }

    private boolean matchesSecteur(Reporting reporting, Secteur secteur) {
        return secteur == null || secteur.equals(reporting.getSecteur());
    }

    private boolean matchesResponsable(Reporting reporting, Responsable responsable) {
        return responsable == null || responsable.equals(reporting.getResponsable());
    }

    private boolean matchesSearch(Reporting reporting, String search) {
        if (search == null || search.isBlank()) {
            return true;
        }
        String normalized = search.trim().toLowerCase();
        return Stream.of(
                reporting.getNumeroDA(),
                reporting.getNumeroDossier(),
                reporting.getCodeOracle(),
                reporting.getCodeSAP(),
                reporting.getDescription(),
                reporting.getCommande(),
                reporting.getFournisseur(),
                reporting.getCommentaire()
        ).filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(value -> value.contains(normalized));
    }

    private List<Reporting> sortReportings(List<Reporting> reportings, String sort) {
        if (sort == null || sort.isBlank()) {
            return reportings;
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        boolean ascending = parts.length < 2 || !"desc".equalsIgnoreCase(parts[1].trim());
        Comparator<Reporting> comparator = switch (field) {
            case "numeroDA" -> Comparator.comparing(Reporting::getNumeroDA, Comparator.nullsLast(String::compareToIgnoreCase));
            case "numeroDossier" -> Comparator.comparing(Reporting::getNumeroDossier, Comparator.nullsLast(String::compareToIgnoreCase));
            case "dateCreation" -> Comparator.comparing(Reporting::getDateCreation, Comparator.nullsLast(Comparator.naturalOrder()));
            case "dateModification" -> Comparator.comparing(Reporting::getDateModification, Comparator.nullsLast(Comparator.naturalOrder()));
            case "quantite" -> Comparator.comparing(Reporting::getQuantite, Comparator.nullsLast(Double::compareTo));
            default -> Comparator.comparing(Reporting::getId, Comparator.nullsLast(Long::compareTo));
        };
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return reportings.stream().sorted(comparator).collect(Collectors.toList());
    }

    private String toCsvLine(Reporting reporting) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s",
                reporting.getId(),
                escapeCsv(reporting.getNumeroDA()),
                escapeCsv(reporting.getNumeroDossier()),
                reporting.getStatut(),
                reporting.getSecteur(),
                reporting.getResponsable(),
                reporting.getQuantite(),
                reporting.getDateCreation(),
                reporting.getDateModification());
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"");
    }

    public Optional<Reporting> findById(Long id) {
        return repo.findById(id);
    }

    public Reporting save(Reporting reporting) {
        LocalDateTime now = LocalDateTime.now();
        if (reporting.getId() == null) {
            reporting.setDateCreation(now);
            reporting.setDateModification(now);
            Historique historique = buildHistorique("Création du reporting", null, summarize(reporting), now, reporting);
            reporting.getHistoriques().add(historique);
            return repo.save(reporting);
        }

        Reporting existing = repo.findById(reporting.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reporting introuvable"));
        List<Historique> modifications = buildModifications(existing, reporting, now);
        copyReportingFields(reporting, existing);
        existing.setDateModification(now);
        existing.getHistoriques().addAll(modifications);
        return repo.save(existing);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public ReportingStatisticsDto statistics() {
        List<Reporting> reportings = repo.findAll();
        ReportingStatisticsDto statistics = new ReportingStatisticsDto();
        statistics.setTotalReportings(reportings.size());
        statistics.setReportingsByStatut(reportings.stream()
                .map(Reporting::getStatut)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Statut::name, Collectors.counting())));
        statistics.setReportingsBySecteur(reportings.stream()
                .map(Reporting::getSecteur)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Secteur::name, Collectors.counting())));
        statistics.setReportingsByResponsable(reportings.stream()
                .map(Reporting::getResponsable)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Responsable::name, Collectors.counting())));
        statistics.setTotalQuantite(reportings.stream()
                .filter(reporting -> reporting.getQuantite() != null)
                .mapToDouble(Reporting::getQuantite)
                .sum());
        statistics.setAverageQuantite(reportings.stream()
                .filter(reporting -> reporting.getQuantite() != null)
                .mapToDouble(Reporting::getQuantite)
                .average()
                .orElse(0.0));
        statistics.setAveragePourcentageLivraison(reportings.stream()
                .filter(reporting -> reporting.getPourcentageLivraison() != null)
                .mapToInt(Reporting::getPourcentageLivraison)
                .average()
                .orElse(0.0));
        statistics.setAverageDelaiLivraison(reportings.stream()
                .filter(reporting -> reporting.getDelaiLivraison() != null)
                .mapToInt(Reporting::getDelaiLivraison)
                .average()
                .orElse(0.0));
        return statistics;
    }

    private List<Historique> buildModifications(Reporting existing, Reporting updated, LocalDateTime now) {
        List<Historique> changements = new ArrayList<>();

        compareField(changements, existing.getNumeroDA(), updated.getNumeroDA(), "numéro DA", now, existing);
        compareField(changements, existing.getNumeroDossier(), updated.getNumeroDossier(), "numéro dossier", now, existing);
        compareField(changements, existing.getCodeOracle(), updated.getCodeOracle(), "code Oracle", now, existing);
        compareField(changements, existing.getCodeSAP(), updated.getCodeSAP(), "code SAP", now, existing);
        compareField(changements, existing.getDescription(), updated.getDescription(), "description", now, existing);
        compareField(changements, existing.getUniteDeMesure(), updated.getUniteDeMesure(), "unité de mesure", now, existing);
        compareField(changements, existing.getQuantite(), updated.getQuantite(), "quantité", now, existing);
        compareField(changements, existing.getSecteur(), updated.getSecteur(), "secteur", now, existing);
        compareField(changements, existing.getCommande(), updated.getCommande(), "commande", now, existing);
        compareField(changements, existing.getFournisseur(), updated.getFournisseur(), "fournisseur", now, existing);
        compareField(changements, existing.getPourcentageLivraison(), updated.getPourcentageLivraison(), "pourcentage livraison", now, existing);
        compareField(changements, existing.getDelaiLivraison(), updated.getDelaiLivraison(), "délai livraison", now, existing);
        compareField(changements, existing.getDateNotification(), updated.getDateNotification(), "date notification", now, existing);
        compareField(changements, existing.getDatePrevisionnelle(), updated.getDatePrevisionnelle(), "date prévisionnelle", now, existing);
        compareField(changements, existing.getStatut(), updated.getStatut(), "statut", now, existing);
        compareField(changements, existing.getResponsable(), updated.getResponsable(), "responsable", now, existing);
        compareField(changements, existing.getCommentaire(), updated.getCommentaire(), "commentaire", now, existing);
        compareField(changements, existing.getUtilisateur() != null ? existing.getUtilisateur().getId() : null,
                updated.getUtilisateur() != null ? updated.getUtilisateur().getId() : null,
                "utilisateur", now, existing);

        return changements;
    }

    private <T> void compareField(List<Historique> changements, T oldValue, T newValue, String field,
            LocalDateTime now, Reporting reporting) {
        if (!Objects.equals(oldValue, newValue)) {
            String ancienne = oldValue == null ? null : oldValue.toString();
            String nouvelle = newValue == null ? null : newValue.toString();
            changements.add(buildHistorique("Modification " + field, ancienne, nouvelle, now, reporting));
        }
    }

    private Historique buildHistorique(String action, String ancienneValeur, String nouvelleValeur,
            LocalDateTime dateAction, Reporting reporting) {
        Historique historique = new Historique();
        historique.setAction(action);
        historique.setAncienneValeur(ancienneValeur);
        historique.setNouvelleValeur(nouvelleValeur);
        historique.setDateAction(dateAction);
        historique.setReporting(reporting);
        return historique;
    }

    private String summarize(Reporting reporting) {
        return String.format("Création du reporting %s / %s - %s", reporting.getNumeroDA(), reporting.getNumeroDossier(), reporting.getStatut());
    }

    private void copyReportingFields(Reporting source, Reporting target) {
        target.setNumeroDA(source.getNumeroDA());
        target.setNumeroDossier(source.getNumeroDossier());
        target.setCodeOracle(source.getCodeOracle());
        target.setCodeSAP(source.getCodeSAP());
        target.setDescription(source.getDescription());
        target.setUniteDeMesure(source.getUniteDeMesure());
        target.setQuantite(source.getQuantite());
        target.setSecteur(source.getSecteur());
        target.setCommande(source.getCommande());
        target.setFournisseur(source.getFournisseur());
        target.setPourcentageLivraison(source.getPourcentageLivraison());
        target.setDelaiLivraison(source.getDelaiLivraison());
        target.setDateNotification(source.getDateNotification());
        target.setDatePrevisionnelle(source.getDatePrevisionnelle());
        target.setStatut(source.getStatut());
        target.setResponsable(source.getResponsable());
        target.setCommentaire(source.getCommentaire());
        target.setUtilisateur(source.getUtilisateur());
    }
}
