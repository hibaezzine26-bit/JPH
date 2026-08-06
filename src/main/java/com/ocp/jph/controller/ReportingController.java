package com.ocp.jph.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.ocp.jph.dto.HistoriqueDto;
import com.ocp.jph.dto.ImportResultDto;
import com.ocp.jph.dto.ReportingDto;
import com.ocp.jph.dto.ReportingStatisticsDto;
import com.ocp.jph.entity.Reporting;
import com.ocp.jph.entity.Responsable;
import com.ocp.jph.entity.Secteur;
import com.ocp.jph.entity.Statut;
import com.ocp.jph.mapper.HistoriqueMapper;
import com.ocp.jph.mapper.ReportingMapper;
import com.ocp.jph.service.HistoriqueService;
import com.ocp.jph.service.ReportingService;

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
            @RequestParam(required = false) String fournisseur,
            @RequestParam(required = false) String sort) {
        return service.findAll(search, statut, secteur, responsable, fournisseur, sort).stream().map(mapper::toDto).toList();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Statut statut,
            @RequestParam(required = false) Secteur secteur,
            @RequestParam(required = false) Responsable responsable,
            @RequestParam(required = false) String fournisseur,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "xlsx") String format) throws IOException {
        if ("csv".equalsIgnoreCase(format)) {
            String csv = service.export(search, statut, secteur, responsable, fournisseur, sort);
            // Add UTF-8 BOM so Excel detects UTF-8 correctly
            byte[] bom = new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF};
            byte[] csvBytes = csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            byte[] payload = new byte[bom.length + csvBytes.length];
            System.arraycopy(bom, 0, payload, 0, bom.length);
            System.arraycopy(csvBytes, 0, payload, bom.length, csvBytes.length);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header("Content-Disposition", "attachment; filename=reportings.csv")
                    .body(payload);
        } else {
            byte[] excelFile = service.exportToExcel(search, statut, secteur, responsable, fournisseur, sort);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header("Content-Disposition", "attachment; filename=reportings.xlsx")
                    .body(excelFile);
        }
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

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            ImportResultDto result = service.importFromExcel(file.getInputStream());
            if (result.getErrors() == null || result.getErrors().isEmpty()) {
                return ResponseEntity.ok(result.getImportedCount() + " reportings importés.");
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Impossible de lire le fichier Excel.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne : " + e.getMessage());
        }
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
