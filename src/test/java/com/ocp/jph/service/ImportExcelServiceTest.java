package com.ocp.jph.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ocp.jph.entity.ImportExcel;
import com.ocp.jph.repository.ImportExcelRepository;

@ExtendWith(MockitoExtension.class)
class ImportExcelServiceTest {

    @Mock
    private ImportExcelRepository repository;

    @InjectMocks
    private ImportExcelService importExcelService;

    private ImportExcel importExcel;

    @BeforeEach
    void setUp() {
        importExcel = new ImportExcel();
        importExcel.setId(1L);
        importExcel.setNomFichier("reportings_janvier_2024.xlsx");
        importExcel.setDateImport(LocalDateTime.now());
        importExcel.setNombreLignes(45);
        importExcel.setStatutImport("SUCCES");
    }

    @Test
    @DisplayName("Devrait retourner la liste complète des imports Excel")
    void shouldFindAllImports() {
        when(repository.findAll()).thenReturn(List.of(importExcel));

        List<ImportExcel> result = importExcelService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("reportings_janvier_2024.xlsx", result.get(0).getNomFichier());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Devrait retourner un import Excel par son identifiant")
    void shouldFindImportById() {
        when(repository.findById(1L)).thenReturn(Optional.of(importExcel));

        Optional<ImportExcel> result = importExcelService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("reportings_janvier_2024.xlsx", result.get().getNomFichier());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Devrait sauvegarder un enregistrement d'import Excel")
    void shouldSaveImportExcel() {
        when(repository.save(importExcel)).thenReturn(importExcel);

        ImportExcel saved = importExcelService.save(importExcel);

        assertNotNull(saved);
        assertEquals("SUCCES", saved.getStatutImport());
        verify(repository).save(importExcel);
    }

    @Test
    @DisplayName("Devrait supprimer un import Excel par son identifiant")
    void shouldDeleteImportExcelById() {
        importExcelService.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
