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
      
        for (Responsable responsable : Responsable.values()) {
            createAdmin(responsable, "admin123");
          
        }

     
        createConsultant("Export", "export@jph.local", "export123");
        createConsultant("Soufre", "soufre@jph.local", "soufre123");
        createConsultant("Ammoniac", "ammoniac@jph.local", "ammoniac123");
      
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
