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

import com.ocp.jph.domain.Historique;
import com.ocp.jph.dto.HistoriqueDto;
import com.ocp.jph.service.HistoriqueService;
import com.ocp.jph.web.mapper.HistoriqueMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/historiques")
public class HistoriqueController {
    private final HistoriqueService service;
    private final HistoriqueMapper mapper;

    public HistoriqueController(HistoriqueService service, HistoriqueMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<HistoriqueDto> list() {
        return service.findAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueDto> get(@PathVariable Long id) {
        return service.findById(id).map(mapper::toDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HistoriqueDto> create(@Valid @RequestBody HistoriqueDto h) {
        Historique saved = service.save(mapper.toEntity(h));
        return ResponseEntity.created(URI.create("/api/historiques/" + saved.getId())).body(mapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueDto> update(@PathVariable Long id, @Valid @RequestBody HistoriqueDto h) {
        h.setId(id);
        Historique saved = service.save(mapper.toEntity(h));
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
