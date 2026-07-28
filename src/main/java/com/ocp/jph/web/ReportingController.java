package com.ocp.jph.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocp.jph.domain.Reporting;
import com.ocp.jph.dto.ReportingDto;
import com.ocp.jph.service.ReportingService;
import com.ocp.jph.web.mapper.ReportingMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reportings")
public class ReportingController {
    private final ReportingService service;
    private final ReportingMapper mapper;

    public ReportingController(ReportingService service, ReportingMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ReportingDto> list() {
        return service.findAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportingDto> get(@PathVariable Long id) {
        return service.findById(id).map(mapper::toDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
