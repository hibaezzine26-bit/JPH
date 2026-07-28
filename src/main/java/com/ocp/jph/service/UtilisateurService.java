package com.ocp.jph.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ocp.jph.domain.Utilisateur;
import com.ocp.jph.repository.UtilisateurRepository;

@Service
public class UtilisateurService {
    private final UtilisateurRepository repo;

    public UtilisateurService(UtilisateurRepository repo) {
        this.repo = repo;
    }

    public List<Utilisateur> findAll() {
        return repo.findAll();
    }

    public Optional<Utilisateur> findById(Long id) {
        return repo.findById(id);
    }

    public Utilisateur save(Utilisateur u) {
        return repo.save(u);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
