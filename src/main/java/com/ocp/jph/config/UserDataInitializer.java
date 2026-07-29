package com.ocp.jph.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ocp.jph.domain.Role;
import com.ocp.jph.domain.Utilisateur;
import com.ocp.jph.repository.UtilisateurRepository;

@Component
public class UserDataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@jph.local";
        if (utilisateurRepository.findByEmail(adminEmail).isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setEmail(adminEmail);
            admin.setMotDePasse(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMINISTRATEUR);
            utilisateurRepository.save(admin);
        }
    }
}
