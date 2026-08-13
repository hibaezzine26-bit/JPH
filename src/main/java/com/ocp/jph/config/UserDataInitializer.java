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
        // Deduplicate existing users by email
        java.util.List<Utilisateur> allUsers = utilisateurRepository.findAll();
        java.util.Map<String, Utilisateur> uniqueUsers = new java.util.HashMap<>();
        for (Utilisateur u : allUsers) {
            if (u.getEmail() != null) {
                String emailKey = u.getEmail().toLowerCase();
                if (uniqueUsers.containsKey(emailKey)) {
                    utilisateurRepository.delete(u);
                } else {
                    uniqueUsers.put(emailKey, u);
                }
            }
        }
      
        for (Responsable responsable : Responsable.values()) {
            createAdmin(responsable, "admin123");
        }
     
        createConsultant("Export", "export@jph.local", "export123");
        createConsultant("Soufre", "soufre@jph.local", "soufre123");
        createConsultant("Ammoniac", "ammoniac@jph.local", "ammoniac123");
    }

    private void createAdmin(Responsable responsable, String password) {
        String email = (responsable.name().charAt(0) + "." + responsable.name() + "@ocpgroup.ma").toLowerCase();
        if (utilisateurRepository.findByEmail(email).isPresent()) {
            return;
        }
        Utilisateur admin = new Utilisateur();
        admin.setNom(responsable.name());
        admin.setPrenom("Admin");
        admin.setEmail(email);
        admin.setMotDePasse(passwordEncoder.encode(password));
        admin.setRole(Role.ADMINISTRATEUR);
        utilisateurRepository.save(admin);
    }

    private void createConsultant(String nom, String email, String password) {
        String lowerEmail = email.toLowerCase();
        if (utilisateurRepository.findByEmail(lowerEmail).isPresent()) {
            return;
        }
        Utilisateur consultant = new Utilisateur();
        consultant.setNom(nom);
        consultant.setPrenom("Consultant");
        consultant.setEmail(lowerEmail);
        consultant.setMotDePasse(passwordEncoder.encode(password));
        consultant.setRole(Role.CONSULTANT);
        utilisateurRepository.save(consultant);
    }
}
