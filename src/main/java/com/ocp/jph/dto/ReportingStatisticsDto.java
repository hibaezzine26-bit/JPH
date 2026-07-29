package com.ocp.jph.dto;

import java.util.Map;

public class ReportingStatisticsDto {

    private long totalReportings;
    private Map<String, Long> reportingsByStatut;
    private Map<String, Long> reportingsBySecteur;
    private Map<String, Long> reportingsByResponsable;
    private double totalQuantite;
    private double averageQuantite;
    private double averagePourcentageLivraison;
    private double averageDelaiLivraison;

    public long getTotalReportings() {
        return totalReportings;
    }

    public void setTotalReportings(long totalReportings) {
        this.totalReportings = totalReportings;
    }

    public Map<String, Long> getReportingsByStatut() {
        return reportingsByStatut;
    }

    public void setReportingsByStatut(Map<String, Long> reportingsByStatut) {
        this.reportingsByStatut = reportingsByStatut;
    }

    public Map<String, Long> getReportingsBySecteur() {
        return reportingsBySecteur;
    }

    public void setReportingsBySecteur(Map<String, Long> reportingsBySecteur) {
        this.reportingsBySecteur = reportingsBySecteur;
    }

    public Map<String, Long> getReportingsByResponsable() {
        return reportingsByResponsable;
    }

    public void setReportingsByResponsable(Map<String, Long> reportingsByResponsable) {
        this.reportingsByResponsable = reportingsByResponsable;
    }

    public double getTotalQuantite() {
        return totalQuantite;
    }

    public void setTotalQuantite(double totalQuantite) {
        this.totalQuantite = totalQuantite;
    }

    public double getAverageQuantite() {
        return averageQuantite;
    }

    public void setAverageQuantite(double averageQuantite) {
        this.averageQuantite = averageQuantite;
    }

    public double getAveragePourcentageLivraison() {
        return averagePourcentageLivraison;
    }

    public void setAveragePourcentageLivraison(double averagePourcentageLivraison) {
        this.averagePourcentageLivraison = averagePourcentageLivraison;
    }

    public double getAverageDelaiLivraison() {
        return averageDelaiLivraison;
    }

    public void setAverageDelaiLivraison(double averageDelaiLivraison) {
        this.averageDelaiLivraison = averageDelaiLivraison;
    }
}
