package com.ocp.jph.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ocp.jph.entity.Responsable;
import com.ocp.jph.entity.Role;
import com.ocp.jph.entity.Utilisateur;
import com.ocp.jph.repository.ReportingRepository;
import com.ocp.jph.repository.UtilisateurRepository;

@Component
public class UserDataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final ReportingRepository reportingRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UtilisateurRepository utilisateurRepository, ReportingRepository reportingRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.reportingRepository = reportingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Clear all reportings first (to avoid foreign key constraint violations)
        System.out.println("🔄 UserDataInitializer START - Suppression de tous les reportings...");
        reportingRepository.deleteAll();
        System.out.println("✅ Tous les reportings supprimés");

        // Then clear existing users
        System.out.println("🔄 Suppression de tous les utilisateurs...");
        utilisateurRepository.deleteAll();
        System.out.println("✅ Tous les utilisateurs supprimés");

        System.out.println("➕ Création des 4 administrateurs...");
        for (Responsable responsable : Responsable.values()) {
            createAdmin(responsable, "admin123");
            System.out.println("   ✓ Admin créé: " + responsable.name());
        }

        System.out.println("➕ Création des 3 consultants...");
        createConsultant("Export", "export@jph.local", "export123");
        System.out.println("   ✓ Consultant créé: export@jph.local");
        createConsultant("Soufre", "soufre@jph.local", "soufre123");
        System.out.println("   ✓ Consultant créé: soufre@jph.local");
        createConsultant("Ammoniac", "ammoniac@jph.local", "ammoniac123");
        System.out.println("   ✓ Consultant créé: ammoniac@jph.local");
        System.out.println("✅ UserDataInitializer TERMINÉ - 7 utilisateurs créés");
    }

    private void createAdmin(Responsable responsable, String password) {
        String email = responsable.name().charAt(0) + "." + responsable.name() + "@ocpgroup.ma";
        Utilisateur admin = new Utilisateur();
        admin.setNom(responsable.name());
        admin.setPrenom("Admin");
        admin.setEmail(email);
        admin.setMotDePasse(passwordEncoder.encode(password));
        admin.setRole(Role.ADMINISTRATEUR);
        utilisateurRepository.save(admin);
    }

    private void createConsultant(String nom, String email, String password) {
        Utilisateur consultant = new Utilisateur();
        consultant.setNom(nom);
        consultant.setPrenom("Consultant");
        consultant.setEmail(email);
        consultant.setMotDePasse(passwordEncoder.encode(password));
        consultant.setRole(Role.CONSULTANT);
        utilisateurRepository.save(consultant);
    }
}
