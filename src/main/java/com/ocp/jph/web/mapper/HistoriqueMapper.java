package com.ocp.jph.web.mapper;

import org.springframework.stereotype.Component;

import com.ocp.jph.domain.Historique;
import com.ocp.jph.domain.Reporting;
import com.ocp.jph.dto.HistoriqueDto;

@Component
public class HistoriqueMapper {

    public HistoriqueDto toDto(Historique historique) {
        if (historique == null) {
            return null;
        }
        HistoriqueDto dto = new HistoriqueDto();
        dto.setId(historique.getId());
        dto.setAction(historique.getAction());
        dto.setAncienneValeur(historique.getAncienneValeur());
        dto.setNouvelleValeur(historique.getNouvelleValeur());
        dto.setDateAction(historique.getDateAction());
        dto.setReportingId(historique.getReporting() != null ? historique.getReporting().getId() : null);
        return dto;
    }

    public Historique toEntity(HistoriqueDto dto) {
        if (dto == null) {
            return null;
        }
        Historique historique = new Historique();
        historique.setId(dto.getId());
        historique.setAction(dto.getAction());
        historique.setAncienneValeur(dto.getAncienneValeur());
        historique.setNouvelleValeur(dto.getNouvelleValeur());
        historique.setDateAction(dto.getDateAction());
        if (dto.getReportingId() != null) {
            Reporting reporting = new Reporting();
            reporting.setId(dto.getReportingId());
            historique.setReporting(reporting);
        }
        return historique;
    }
}
