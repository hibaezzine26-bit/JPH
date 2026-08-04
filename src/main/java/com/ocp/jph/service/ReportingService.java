package com.ocp.jph.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.ocp.jph.domain.Historique;
import com.ocp.jph.domain.Reporting;
import com.ocp.jph.domain.Responsable;
import com.ocp.jph.domain.Secteur;
import com.ocp.jph.domain.Statut;
import com.ocp.jph.domain.Udm;
import com.ocp.jph.dto.ImportResultDto;
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

    public List<Reporting> findAll(String search, Statut statut, Secteur secteur, Responsable responsable, String fournisseur, String sort) {
        return sortReportings(filterReportings(repo.findAll(), search, statut, secteur, responsable, fournisseur), sort);
    }

    public String export(String search, Statut statut, Secteur secteur, Responsable responsable, String fournisseur, String sort) {
        List<Reporting> reportings = findAll(search, statut, secteur, responsable, fournisseur, sort);
        String header = "N°,Code Oracle,Code SAP,Description,UDM,Quantité,Secteur,CMD,Fournisseur,% Livraison,Délai,Statut,Responsable,Commentaire";
        String rows = reportings.stream().map(this::toCsvLine).collect(Collectors.joining("\n"));
        return header + (rows.isEmpty() ? "" : "\n" + rows);
    }

    public byte[] exportToExcel(String search, Statut statut, Secteur secteur, Responsable responsable, String fournisseur, String sort) throws IOException {
        List<Reporting> reportings = findAll(search, statut, secteur, responsable, fournisseur, sort);
        
        Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reportings");
        
        // Créer l'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {"N°", "Code Oracle", "Code SAP", "Description", "UDM", "Quantité", "Secteur", "CMD", "Fournisseur", "% Livraison", "Délai", "Statut", "Responsable", "Commentaire"};
        
        org.apache.poi.xssf.usermodel.XSSFCellStyle headerStyle = (org.apache.poi.xssf.usermodel.XSSFCellStyle) workbook.createCellStyle();
        org.apache.poi.xssf.usermodel.XSSFFont headerFont = (org.apache.poi.xssf.usermodel.XSSFFont) workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Ajouter les données
        int rowIndex = 1;
        for (Reporting reporting : reportings) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(reporting.getNumero() != null ? reporting.getNumero() : "");
            row.createCell(1).setCellValue(reporting.getCodeOracle() != null ? reporting.getCodeOracle() : "");
            row.createCell(2).setCellValue(reporting.getCodeSAP() != null ? reporting.getCodeSAP() : "");
            row.createCell(3).setCellValue(reporting.getDescription() != null ? reporting.getDescription() : "");
            row.createCell(4).setCellValue(reporting.getUniteDeMesure() != null ? reporting.getUniteDeMesure().name() : "");
            row.createCell(5).setCellValue(reporting.getQuantite() != null ? reporting.getQuantite() : 0);
            row.createCell(6).setCellValue(reporting.getSecteur() != null ? reporting.getSecteur().name() : "");
            row.createCell(7).setCellValue(reporting.getCommande() != null ? reporting.getCommande() : "");
            row.createCell(8).setCellValue(reporting.getFournisseur() != null ? reporting.getFournisseur() : "");
            row.createCell(9).setCellValue(reporting.getPourcentageLivraison() != null ? reporting.getPourcentageLivraison() : 0);
            row.createCell(10).setCellValue(reporting.getDelaiLivraison() != null ? reporting.getDelaiLivraison() : 0);
            row.createCell(11).setCellValue(reporting.getStatut() != null ? reporting.getStatut().name() : "");
            row.createCell(12).setCellValue(reporting.getResponsable() != null ? reporting.getResponsable().name() : "");
            row.createCell(13).setCellValue(reporting.getCommentaire() != null ? reporting.getCommentaire() : "");
        }
        
        // Ajuster la largeur des colonnes
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Convertir en byte array
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }

    private String toCsvLine(Reporting reporting) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                escapeCsv(reporting.getNumero()),
                escapeCsv(reporting.getCodeOracle()),
                escapeCsv(reporting.getCodeSAP()),
                escapeCsv(reporting.getDescription()),
                reporting.getUniteDeMesure() == null ? "" : reporting.getUniteDeMesure().name(),
                reporting.getQuantite() == null ? "" : reporting.getQuantite().toString(),
                reporting.getSecteur() == null ? "" : reporting.getSecteur().name(),
                escapeCsv(reporting.getCommande()),
                escapeCsv(reporting.getFournisseur()),
                reporting.getPourcentageLivraison() == null ? "" : reporting.getPourcentageLivraison().toString(),
                reporting.getDelaiLivraison() == null ? "" : reporting.getDelaiLivraison().toString(),
                reporting.getStatut() == null ? "" : reporting.getStatut().name(),
                reporting.getResponsable() == null ? "" : reporting.getResponsable().name(),
                escapeCsv(reporting.getCommentaire()));
    }

    public ImportResultDto importFromExcel(InputStream inputStream) throws IOException {
        List<String> errors = new ArrayList<>();
        int importedCount = 0;
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() < 1) {
                return new ImportResultDto(0, errors);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("La première ligne doit contenir les en-têtes de colonnes.");
            }

            List<String> expectedHeaders = Arrays.asList(
                    "DA",
                    "Dossier",
                    "N°",
                    "code Oracle",
                    "Code SAP",
                    "Description",
                    "UDM",
                    "Q retenue",
                    "Secteur",
                    "Fournisseur",
                    "CMD",
                    "%Livraison",
                    "Délai livraison",
                    "Date notification",
                    "Date Prévisionnelle livraison",
                    "Commentaire",
                    "Statut Livraison",
                    "Responsable Dossier"
            );

            Map<String, Integer> headerIndex = buildHeaderIndex(headerRow);
            List<String> foundHeaders = buildRawHeaderList(headerRow);
            validateRequiredHeaders(headerIndex, expectedHeaders, foundHeaders);

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                Reporting reporting = buildReportingFromRow(row, headerIndex);
                // Business validation: require at least one identifier and a quantity
                List<String> rowErrors = new ArrayList<>();
                if (reporting.getNumeroDA() == null && reporting.getNumeroDossier() == null && reporting.getNumero() == null) {
                    rowErrors.add("Identifiant manquant (DA, Dossier ou N°)");
                }
                if (reporting.getQuantite() == null) {
                    rowErrors.add("Quantité manquante ou invalide");
                }

                if (!rowErrors.isEmpty()) {
                    errors.add("Ligne " + (rowIndex + 1) + " : " + String.join("; ", rowErrors));
                    continue;
                }

                save(reporting);
                importedCount++;
            }
            return new ImportResultDto(importedCount, errors);
        }
    }

    private Map<String, Integer> buildHeaderIndex(Row headerRow) {
        DataFormatter formatter = new DataFormatter();
        Map<String, Integer> headerIndex = new HashMap<>();
        for (Cell cell : headerRow) {
            String headerValue = formatter.formatCellValue(cell);
            if (headerValue != null && !headerValue.isBlank()) {
                headerIndex.put(normalizeHeader(headerValue), cell.getColumnIndex());
            }
        }
        return headerIndex;
    }

    private List<String> buildRawHeaderList(Row headerRow) {
        DataFormatter formatter = new DataFormatter();
        List<String> rawHeaders = new ArrayList<>();
        for (Cell cell : headerRow) {
            String headerValue = formatter.formatCellValue(cell);
            rawHeaders.add(headerValue == null ? "" : headerValue.trim());
        }
        return rawHeaders;
    }

    private void validateRequiredHeaders(Map<String, Integer> headerIndex, List<String> expectedHeaders, List<String> foundHeaders) {
        for (String expected : expectedHeaders) {
            String normalized = normalizeHeader(expected);
            if (!headerIndex.containsKey(normalized)) {
                throw new IllegalArgumentException("Colonne manquante : " + expected + ". En-têtes trouvées : " + String.join(", ", foundHeaders));
            }
        }
    }

    private String normalizeHeader(String header) {
        if (header == null) {
            return null;
        }
        String normalized = java.text.Normalizer.normalize(header.trim().toLowerCase(Locale.ROOT), java.text.Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.replaceAll("[^a-z0-9]", "");
    }

    private boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK && !getCellString(cell).isBlank()) {
                return false;
            }
        }
        return true;
    }

    private Reporting buildReportingFromRow(Row row, Map<String, Integer> headerIndex) {
        Reporting reporting = new Reporting();
        reporting.setNumeroDA(getCellValue(row, headerIndex, "da", "DA"));
        reporting.setNumeroDossier(getCellValue(row, headerIndex, "dossier", "Dossier"));
        reporting.setNumero(getCellValue(row, headerIndex, "N°", "N", "n", "numero", "numero"));
        reporting.setCodeOracle(getCellValue(row, headerIndex, "codeOracle", "code Oracle"));
        reporting.setCodeSAP(getCellValue(row, headerIndex, "codeSAP", "Code SAP"));
        reporting.setDescription(getCellValue(row, headerIndex, "description", "Description"));
        reporting.setUniteDeMesure(parseEnumValue(row, headerIndex, Udm.class, "UDM", "Udm", "udm"));
        reporting.setQuantite(getDoubleValue(row, headerIndex, "Q retenue", "Qretenue", "q retenue", "qretenue", "quantite", "quantite"));
        reporting.setSecteur(parseEnumValue(row, headerIndex, Secteur.class, "Secteur", "secteur"));
        reporting.setCommande(getCellValue(row, headerIndex, "CMD", "cmd", "Commande", "commande"));
        reporting.setFournisseur(getCellValue(row, headerIndex, "Fournisseur", "fournisseur"));
        reporting.setPourcentageLivraison(getIntegerValue(row, headerIndex, "%Livraison", "pourcentage livraison", "pourcentagelivraison", "pourcentage"));
        reporting.setDelaiLivraison(getIntegerValue(row, headerIndex, "Délai livraison", "Delai livraison", "delai livraison", "delailivraison", "delaiLivraison"));
        reporting.setDateNotification(getDateValue(row, headerIndex, "Date notification", "date notification", "datenotification", "date notif"));
        reporting.setDatePrevisionnelle(getDateValue(row, headerIndex, "Date Prévisionnelle livraison", "date prévisionnelle livraison", "dateprevisionnellelivraison", "dateprevisionnelle"));
        reporting.setStatut(parseEnumValue(row, headerIndex, Statut.class, "statut livraison", "statut", "statutlivraison"));
        reporting.setResponsable(parseEnumValue(row, headerIndex, Responsable.class, "Responsable Dossier", "responsable dossier", "responsabledossier", "responsable"));
        reporting.setCommentaire(getCellValue(row, headerIndex, "Commentaire", "commentaire"));
        return reporting;
    }

    private String getCellValue(Row row, Map<String, Integer> headerIndex, String... headerNames) {
        for (String headerName : headerNames) {
            Integer index = headerIndex.get(normalizeHeader(headerName));
            if (index == null) {
                continue;
            }
            Cell cell = row.getCell(index);
            String value = getCellString(cell);
            if (!value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private String getCellString(Cell cell) {
        if (cell == null) {
            return "";
        }
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private Double getDoubleValue(Row row, Map<String, Integer> headerIndex, String... headerNames) {
        String stringValue = getCellValue(row, headerIndex, headerNames);
        if (stringValue == null || stringValue.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(normalizeNumber(stringValue));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer getIntegerValue(Row row, Map<String, Integer> headerIndex, String... headerNames) {
        Double doubleValue = getDoubleValue(row, headerIndex, headerNames);
        return doubleValue == null ? null : doubleValue.intValue();
    }

    private String normalizeNumber(String value) {
        return value.trim().replaceAll("%", "").replaceAll("\\s+", "").replace(',', '.');
    }

    private <E extends Enum<E>> E parseEnumValue(Row row, Map<String, Integer> headerIndex, Class<E> enumType, String... headerNames) {
        String stringValue = getCellValue(row, headerIndex, headerNames);
        if (stringValue == null || stringValue.isBlank()) {
            return null;
        }
        String normalized = normalizeEnumValue(stringValue);
        try {
            return Enum.valueOf(enumType, normalized);
        } catch (IllegalArgumentException e) {
            return tryEnumSynonyms(enumType, normalized);
        }
    }

    private String normalizeEnumValue(String value) {
        String normalized = java.text.Normalizer.normalize(value.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT)
                .replaceAll("[\\s\\-]", "_")
                .replaceAll("[^A-Z0-9_]", "");
        return normalized;
    }

    private <E extends Enum<E>> E tryEnumSynonyms(Class<E> enumType, String normalized) {
        Map<String, String> synonyms = new HashMap<>();
        synonyms.put("LIVRE", "LIVRE");
        synonyms.put("LIVRE", "LIVRE");
        synonyms.put("LIVRE", "LIVRE");
        synonyms.put("LIVRE", "LIVRE");

        if (enumType == Statut.class) {
            synonyms.put("LIVRE", "LIVRE");
            synonyms.put("LIVRE", "LIVRE");
            synonyms.put("ARTICLE_ADJUGE", "ADJUGE");
            synonyms.put("ARTICLE_ADJUGE", "ADJUGE");
            synonyms.put("ARTICLE_ADJUGE", "ADJUGE");
            synonyms.put("LIVRE", "LIVRE");
            synonyms.put("LIVRE", "LIVRE");
            synonyms.put("LIVRE", "LIVRE");
            synonyms.put("LIVRER", "LIVRE");
            synonyms.put("LIVRE", "LIVRE");
            synonyms.put("ARTICLES_ADJUGE", "ADJUGE");
        } else if (enumType == Udm.class) {
            synonyms.put("PIECE", "PIECE");
            synonyms.put("PCE", "PCE");
        }
        String mapped = synonyms.get(normalized);
        if (mapped != null) {
            try {
                return Enum.valueOf(enumType, mapped);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private LocalDate getDateValue(Row row, Map<String, Integer> headerIndex, String... headerNames) {
        Integer index = null;
        for (String headerName : headerNames) {
            Integer headerIndexValue = headerIndex.get(normalizeHeader(headerName));
            if (headerIndexValue != null) {
                index = headerIndexValue;
                break;
            }
        }
        if (index == null) {
            return null;
        }
        Cell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        String stringValue = getCellString(cell);
        if (stringValue.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(stringValue);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("d/MM/yyyy"));
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

    private List<Reporting> filterReportings(List<Reporting> reportings, String search, Statut statut, Secteur secteur, Responsable responsable, String fournisseur) {
        return reportings.stream()
                .filter(reporting -> matchesStatut(reporting, statut))
                .filter(reporting -> matchesSecteur(reporting, secteur))
                .filter(reporting -> matchesResponsable(reporting, responsable))
                .filter(reporting -> matchesFournisseur(reporting, fournisseur))
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
                reporting.getNumero(),
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

    private boolean matchesFournisseur(Reporting reporting, String fournisseur) {
        return fournisseur == null || fournisseur.isBlank() || reporting.getFournisseur() == null || reporting.getFournisseur().toLowerCase().contains(fournisseur.trim().toLowerCase());
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
        compareField(changements, existing.getNumero(), updated.getNumero(), "N°", now, existing);
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
        target.setNumero(source.getNumero());
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
