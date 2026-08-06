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

import com.ocp.jph.dto.ImportExcelDto;
import com.ocp.jph.entity.ImportExcel;
import com.ocp.jph.service.ImportExcelService;
import com.ocp.jph.web.mapper.ImportExcelMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/imports")
public class ImportExcelController {
    private final ImportExcelService service;
    private final ImportExcelMapper mapper;

    public ImportExcelController(ImportExcelService service, ImportExcelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ImportExcelDto> list() {
        return service.findAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportExcelDto> get(@PathVariable Long id) {
        return service.findById(id).map(mapper::toDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ImportExcelDto> create(@Valid @RequestBody ImportExcelDto e) {
        ImportExcel saved = service.save(mapper.toEntity(e));
        return ResponseEntity.created(URI.create("/api/imports/" + saved.getId())).body(mapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImportExcelDto> update(@PathVariable Long id, @Valid @RequestBody ImportExcelDto e) {
        e.setId(id);
        ImportExcel saved = service.save(mapper.toEntity(e));
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
