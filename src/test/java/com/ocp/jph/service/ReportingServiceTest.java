package com.ocp.jph.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ocp.jph.dto.ImportResultDto;
import com.ocp.jph.entity.Reporting;
import com.ocp.jph.entity.Responsable;
import com.ocp.jph.entity.Secteur;
import com.ocp.jph.entity.Statut;
import com.ocp.jph.entity.Udm;
import com.ocp.jph.repository.ReportingRepository;

@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

    @Mock
    private ReportingRepository reportingRepository;

    @InjectMocks
    private ReportingService reportingService;

    private Reporting sampleReporting;

    @BeforeEach
    void setUp() {
        sampleReporting = new Reporting();
        sampleReporting.setId(1L);
        sampleReporting.setNumeroDA("DA-1001");
        sampleReporting.setNumeroDossier("DOS-2024-01");
        sampleReporting.setNumero("N123");
        sampleReporting.setCodeOracle("ORACLE-01");
        sampleReporting.setCodeSAP("SAP-001");
        sampleReporting.setDescription("Pièces de rechange PDR");
        sampleReporting.setUniteDeMesure(Udm.PCE);
        sampleReporting.setQuantite(50.0);
        sampleReporting.setSecteur(Secteur.AMMONIAC);
        sampleReporting.setFournisseur("OCP Supplier");
        sampleReporting.setCommande("CMD-550");
        sampleReporting.setPourcentageLivraison(100);
        sampleReporting.setDelaiLivraison(15);
        sampleReporting.setStatut(Statut.LIVRE);
        sampleReporting.setResponsable(Responsable.ATTOUCHI);
    }

    @Test
    @DisplayName("Devrait retourner tous les reportings de la base")
    void shouldFindAllReportings() {
        when(reportingRepository.findAll()).thenReturn(List.of(sampleReporting));

        List<Reporting> result = reportingService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DA-1001", result.get(0).getNumeroDA());
        verify(reportingRepository).findAll();
    }

    @Test
    @DisplayName("Devrait filtrer les reportings par statut et secteur")
    void shouldFilterReportingsByStatutAndSecteur() {
        Reporting r2 = new Reporting();
        r2.setId(2L);
        r2.setStatut(Statut.EN_COURS);
        r2.setSecteur(Secteur.SOUFRE);

        when(reportingRepository.findAll()).thenReturn(List.of(sampleReporting, r2));

        List<Reporting> filtered = reportingService.findAll(null, Statut.LIVRE, Secteur.AMMONIAC, null, null, null, null);

        assertEquals(1, filtered.size());
        assertEquals(1L, filtered.get(0).getId());
        assertEquals(Statut.LIVRE, filtered.get(0).getStatut());
    }

    @Test
    @DisplayName("Devrait exporter les reportings filtrés en fichier Excel")
    void shouldExportReportingsToExcel() throws IOException {
        when(reportingRepository.findAll()).thenReturn(List.of(sampleReporting));

        byte[] excelBytes = reportingService.exportToExcel(null, null, null, null, null, null, null);

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);
    }

    @Test
    @DisplayName("Devrait générer une exception si le fichier Excel à importer n'a pas les en-têtes requises")
    void shouldThrowExceptionWhenImportHeaderIsInvalid() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("InvalideHeader");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        assertThrows(IllegalArgumentException.class, () -> reportingService.importFromExcel(in));
    }

    @Test
    @DisplayName("Devrait importer correctement un fichier Excel valide")
    void shouldImportValidExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        Row headerRow = sheet.createRow(0);

        String[] headers = {
            "DA", "Dossier", "N°", "code Oracle", "Code SAP", "Description",
            "UDM", "Q retenue", "Secteur", "Fournisseur", "CMD", "%Livraison",
            "Délai livraison", "Date notification", "Date Prévisionnelle livraison",
            "Commentaire", "Statut Livraison", "Responsable Dossier"
        };

        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("DA-9999");
        dataRow.createCell(1).setCellValue("DOS-9999");
        dataRow.createCell(2).setCellValue("N9999");
        dataRow.createCell(3).setCellValue("ORACLE99");
        dataRow.createCell(4).setCellValue("SAP99");
        dataRow.createCell(5).setCellValue("Pompe hydraulique");
        dataRow.createCell(6).setCellValue("PCE");
        dataRow.createCell(7).setCellValue(10.0);
        dataRow.createCell(8).setCellValue("AMMONIAC");
        dataRow.createCell(9).setCellValue("Fournisseur OCP");
        dataRow.createCell(10).setCellValue("CMD-999");
        dataRow.createCell(11).setCellValue(50);
        dataRow.createCell(12).setCellValue(10);
        dataRow.createCell(13).setCellValue("2024-01-01");
        dataRow.createCell(14).setCellValue("2024-01-15");
        dataRow.createCell(15).setCellValue("RAS");
        dataRow.createCell(16).setCellValue("EN_COURS");
        dataRow.createCell(17).setCellValue("ATTOUCHI");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        when(reportingRepository.save(any(Reporting.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ImportResultDto result = reportingService.importFromExcel(in);

        assertNotNull(result);
        assertEquals(1, result.getImportedCount());
        assertTrue(result.getErrors().isEmpty());
    }
}
