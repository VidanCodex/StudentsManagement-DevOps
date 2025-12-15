package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Etudiant;
import com.example.TP_Foyer.Repository.EtudiantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EtudiantServiceIntegrationTest {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Test
    void createAndGetById_shouldPersistEtudiant() {
        Etudiant e = new Etudiant();
        e.setNomEt("Doe");
        e.setPrenomEt("John");
        e.setCin(123L);

        Etudiant saved = etudiantService.create(e);

        assertNotNull(saved.getIdEtudiant());

        Etudiant found = etudiantService.getById(saved.getIdEtudiant());
        assertEquals("Doe", found.getNomEt());
        assertEquals("John", found.getPrenomEt());
        assertEquals(123L, found.getCin());
    }

    @Test
    void update_shouldModifyExistingEtudiant() {
        Etudiant e = new Etudiant();
        e.setNomEt("Old");
        e.setPrenomEt("Name");
        e.setCin(111L);
        e = etudiantRepository.save(e);

        Etudiant updatedData = new Etudiant();
        updatedData.setNomEt("New");
        updatedData.setPrenomEt("Name");
        updatedData.setCin(222L);

        Etudiant updated = etudiantService.update(e.getIdEtudiant(), updatedData);

        assertEquals(e.getIdEtudiant(), updated.getIdEtudiant());
        assertEquals("New", updated.getNomEt());
        assertEquals(222L, updated.getCin());
    }

    @Test
    void delete_shouldRemoveEtudiant() {
        Etudiant e = new Etudiant();
        e.setNomEt("ToDelete");
        e.setPrenomEt("User");
        e.setCin(999L);
        e = etudiantRepository.save(e);

        etudiantService.delete(e.getIdEtudiant());

        assertFalse(etudiantRepository.findById(e.getIdEtudiant()).isPresent());
    }

    @Test
    void getAll_shouldReturnAllPersistedEtudiants() {
        etudiantRepository.deleteAll();

        Etudiant e1 = new Etudiant();
        e1.setNomEt("A");
        e1.setPrenomEt("A");
        e1.setCin(1L);
        Etudiant e2 = new Etudiant();
        e2.setNomEt("B");
        e2.setPrenomEt("B");
        e2.setCin(2L);

        etudiantRepository.save(e1);
        etudiantRepository.save(e2);

        List<Etudiant> list = etudiantService.getAll();
        assertEquals(2, list.size());
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        assertThrows(RuntimeException.class, () -> etudiantService.getById(9999L));
    }
}
