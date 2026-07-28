package com.ocp.jph.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.ocp.jph.domain.Reporting;
import com.ocp.jph.domain.Responsable;
import com.ocp.jph.domain.Secteur;
import com.ocp.jph.domain.Statut;
import com.ocp.jph.domain.Udm;
import com.ocp.jph.dto.ReportingDto;
import com.ocp.jph.service.ReportingService;
import com.ocp.jph.web.exception.ValidationExceptionHandler;
import com.ocp.jph.web.mapper.ReportingMapper;

@WebMvcTest(
        controllers = ReportingController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration.class,
                org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration.class,
                org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration.class
        })
@Import({
        ReportingMapper.class,
        ValidationExceptionHandler.class
})
class ReportingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportingService reportingService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldReturnBadRequestWhenCreateReportingDtoIsInvalid() throws Exception {

        String body = """
                {
                  "numeroDA":"",
                  "numeroDossier":"",
                  "codeOracle":"",
                  "codeSAP":"",
                  "uniteDeMesure":null,
                  "quantite":null,
                  "secteur":null,
                  "commande":"",
                  "fournisseur":"",
                  "pourcentageLivraison":null,
                  "delaiLivraison":null,
                  "dateNotification":null,
                  "datePrevisionnelle":null,
                  "statut":null,
                  "responsable":null,
                  "utilisateurId":null
                }
                """;

        mockMvc.perform(post("/api/reportings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void shouldCreateReportingWhenValidDto() throws Exception {

        ReportingDto dto = new ReportingDto();

        dto.setNumeroDA("DA-1001");
        dto.setNumeroDossier("DOS-1001");
        dto.setCodeOracle("ORC-1001");
        dto.setCodeSAP("SAP-1001");
        dto.setDescription("Test");
        dto.setUniteDeMesure(Udm.KG);
        dto.setQuantite(50.0);
        dto.setSecteur(Secteur.SOUFRE);
        dto.setCommande("CMD-1001");
        dto.setFournisseur("Fournisseur A");
        dto.setPourcentageLivraison(80);
        dto.setDelaiLivraison(15);
        dto.setDateNotification(LocalDate.now());
        dto.setDatePrevisionnelle(LocalDate.now().plusDays(15));
        dto.setStatut(Statut.EN_COURS);
        dto.setResponsable(Responsable.ATTOUCHI);
        dto.setUtilisateurId(1L);
        dto.setCommentaire("Test");

        Reporting reporting = new Reporting();

        reporting.setId(1L);
        reporting.setNumeroDA(dto.getNumeroDA());
        reporting.setNumeroDossier(dto.getNumeroDossier());
        reporting.setCodeOracle(dto.getCodeOracle());
        reporting.setCodeSAP(dto.getCodeSAP());
        reporting.setDescription(dto.getDescription());
        reporting.setUniteDeMesure(dto.getUniteDeMesure());
        reporting.setQuantite(dto.getQuantite());
        reporting.setSecteur(dto.getSecteur());
        reporting.setCommande(dto.getCommande());
        reporting.setFournisseur(dto.getFournisseur());
        reporting.setPourcentageLivraison(dto.getPourcentageLivraison());
        reporting.setDelaiLivraison(dto.getDelaiLivraison());
        reporting.setDateNotification(dto.getDateNotification());
        reporting.setDatePrevisionnelle(dto.getDatePrevisionnelle());
        reporting.setStatut(dto.getStatut());
        reporting.setResponsable(dto.getResponsable());
        reporting.setCommentaire(dto.getCommentaire());
        reporting.setDateCreation(LocalDateTime.now());
        reporting.setDateModification(LocalDateTime.now());

        when(reportingService.save(any(Reporting.class))).thenReturn(reporting);

        mockMvc.perform(post("/api/reportings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroDA").value("DA-1001"));
    }
}