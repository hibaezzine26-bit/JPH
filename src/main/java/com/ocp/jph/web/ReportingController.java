package com.ocp.jph.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocp.jph.domain.Reporting;
import com.ocp.jph.domain.Responsable;
import com.ocp.jph.domain.Secteur;
import com.ocp.jph.domain.Statut;
import com.ocp.jph.dto.HistoriqueDto;
import com.ocp.jph.dto.ReportingDto;
import com.ocp.jph.dto.ReportingStatisticsDto;
import com.ocp.jph.service.HistoriqueService;
import com.ocp.jph.service.ReportingService;
import com.ocp.jph.web.mapper.HistoriqueMapper;
import com.ocp.jph.web.mapper.ReportingMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reportings")
public class ReportingController {
    private final ReportingService service;
    private final ReportingMapper mapper;
    private final HistoriqueService historiqueService;
    private final HistoriqueMapper historiqueMapper;

    public ReportingController(ReportingService service, ReportingMapper mapper,
            HistoriqueService historiqueService, HistoriqueMapper historiqueMapper) {
        this.service = service;
        this.mapper = mapper;
        this.historiqueService = historiqueService;
        this.historiqueMapper = historiqueMapper;
    }

    @GetMapping
    public List<ReportingDto> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Statut statut,
            @RequestParam(required = false) Secteur secteur,
            @RequestParam(required = false) Responsable responsable,
            @RequestParam(required = false) String sort) {
        return service.findAll(search, statut, secteur, responsable, sort).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/export")
    public ResponseEntity<String> export(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Statut statut,
            @RequestParam(required = false) Secteur secteur,
            @RequestParam(required = false) Responsable responsable,
            @RequestParam(required = false) String sort) {
        String csv = service.export(search, statut, secteur, responsable, sort);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "attachment; filename=reportings.csv")
                .body(csv);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportingDto> get(@PathVariable Long id) {
        return service.findById(id).map(mapper::toDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReportingDto> create(@Valid @RequestBody ReportingDto r) {
        Reporting saved = service.save(mapper.toEntity(r));
        return ResponseEntity.created(URI.create("/api/reportings/" + saved.getId())).body(mapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportingDto> update(@PathVariable Long id, @Valid @RequestBody ReportingDto r) {
        r.setId(id);
        Reporting saved = service.save(mapper.toEntity(r));
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @GetMapping("/statistiques")
    public ReportingStatisticsDto statistics() {
        return service.statistics();
    }

    @GetMapping("/{id}/historiques")
    public List<HistoriqueDto> history(@PathVariable Long id) {
        return historiqueService.findByReportingId(id).stream().map(historiqueMapper::toDto).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
