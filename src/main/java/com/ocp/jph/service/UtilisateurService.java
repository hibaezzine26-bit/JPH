package com.ocp.jph.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ocp.jph.domain.Utilisateur;
import com.ocp.jph.repository.UtilisateurRepository;

@Service
public class UtilisateurService {
    private final UtilisateurRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> findAll() {
        return repo.findAll();
    }

    public Optional<Utilisateur> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<Utilisateur> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public Utilisateur register(Utilisateur utilisateur) {
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (repo.findByEmail(utilisateur.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        return repo.save(utilisateur);
    }

    public Utilisateur save(Utilisateur utilisateur) {
        if (utilisateur.getId() != null) {
            Utilisateur existing = repo.findById(utilisateur.getId()).orElse(utilisateur);
            if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isBlank()) {
                utilisateur.setMotDePasse(existing.getMotDePasse());
            } else if (!utilisateur.getMotDePasse().equals(existing.getMotDePasse())) {
                utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
            }
        } else if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        }
        return repo.save(utilisateur);
    }

    public Utilisateur changePassword(Long id, String currentPassword, String newPassword) {
        Utilisateur utilisateur = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Utilisateur not found"));
        if (!passwordEncoder.matches(currentPassword, utilisateur.getMotDePasse())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
        return repo.save(utilisateur);
    }

    public Utilisateur updateProfile(String email, Utilisateur updated) {
        Utilisateur utilisateur = repo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Utilisateur not found"));
        utilisateur.setNom(updated.getNom());
        utilisateur.setPrenom(updated.getPrenom());
        utilisateur.setEmail(updated.getEmail());
        // Ne pas permettre de modifier le rôle via le profil utilisateur
        if (updated.getMotDePasse() != null && !updated.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(updated.getMotDePasse()));
        }
        return repo.save(utilisateur);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
