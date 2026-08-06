package com.ocp.jph.controller;

import java.net.URI;
import java.security.Principal;
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

import com.ocp.jph.dto.ChangePasswordDto;
import com.ocp.jph.dto.UtilisateurDto;
import com.ocp.jph.entity.Utilisateur;
import com.ocp.jph.mapper.UtilisateurMapper;
import com.ocp.jph.service.UtilisateurService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    private final UtilisateurService service;
    private final UtilisateurMapper mapper;

    public UtilisateurController(UtilisateurService service, UtilisateurMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<UtilisateurDto> list() {
        return service.findAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDto> get(@PathVariable Long id) {
        return service.findById(id).map(mapper::toDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UtilisateurDto> create(@Valid @RequestBody UtilisateurDto u) {
        Utilisateur saved = service.save(mapper.toEntity(u));
        return ResponseEntity.created(URI.create("/api/utilisateurs/" + saved.getId())).body(mapper.toDto(saved));
    }

    @PostMapping("/register")
    public ResponseEntity<UtilisateurDto> register(@Valid @RequestBody UtilisateurDto u) {
        Utilisateur saved = service.register(mapper.toEntity(u));
        return ResponseEntity.created(URI.create("/api/utilisateurs/" + saved.getId())).body(mapper.toDto(saved));
    }

    @GetMapping("/me")
    public ResponseEntity<UtilisateurDto> me(Principal principal) {
        return service.findByEmail(principal.getName()).map(mapper::toDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<UtilisateurDto> updateMe(Principal principal, @Valid @RequestBody UtilisateurDto u) {
        Utilisateur updated = mapper.toEntity(u);
        Utilisateur saved = service.updateProfile(principal.getName(), updated);
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @PutMapping("/me/mot-de-passe")
    public ResponseEntity<UtilisateurDto> changePassword(Principal principal, @Valid @RequestBody ChangePasswordDto payload) {
        Utilisateur utilisateur = service.findByEmail(principal.getName()).orElseThrow(() -> new IllegalArgumentException("Utilisateur not found"));
        Utilisateur updated = service.changePassword(utilisateur.getId(), payload.getCurrentPassword(), payload.getNewPassword());
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDto> update(@PathVariable Long id, @Valid @RequestBody UtilisateurDto u) {
        u.setId(id);
        Utilisateur saved = service.save(mapper.toEntity(u));
        return ResponseEntity.ok(mapper.toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
