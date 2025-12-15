package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Entity.Universite;
import com.example.TP_Foyer.Repository.FoyerRepository;
import com.example.TP_Foyer.Repository.UniversiteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UniversiteServiceIntegrationTest {

    @Autowired
    private UniversiteService universiteService;

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Test
    void createAndGetById_shouldPersistUniversite() {
        Universite u = new Universite();
        u.setNomUniversite("Uni1");
        u.setAdresse("Addr");

        Universite saved = universiteService.create(u);

        assertNotNull(saved.getIdUniversite());

        Universite found = universiteService.getById(saved.getIdUniversite());
        assertEquals("Uni1", found.getNomUniversite());
    }

    @Test
    void affecterFoyerAUniversite_shouldLinkFoyerAndUniversite() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F1");
        foyer.setCapaciteFoyer(100L);
        foyer = foyerRepository.save(foyer);

        Universite uni = new Universite();
        uni.setNomUniversite("Uni2");
        uni.setAdresse("Addr2");
        uni = universiteRepository.save(uni);

        Universite updated = universiteService.affecterFoyerAUniversite(foyer.getIdFoyer(), "Uni2");

        assertNotNull(updated.getFoyer());
        assertEquals(foyer.getIdFoyer(), updated.getFoyer().getIdFoyer());
        assertEquals(updated, updated.getFoyer().getUniversite());
    }

    @Test
    void desaffecterFoyerAUniversite_shouldUnlinkFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F2");
        foyer.setCapaciteFoyer(200L);
        foyer = foyerRepository.save(foyer);

        Universite uni = new Universite();
        uni.setNomUniversite("Uni3");
        uni.setAdresse("Addr3");
        uni.setFoyer(foyer);
        foyer.setUniversite(uni);
        uni = universiteRepository.save(uni);

        Universite updated = universiteService.desaffecterFoyerAUniversite(uni.getIdUniversite());

        assertNull(updated.getFoyer());
        Foyer reloadedFoyer = foyerRepository.findById(foyer.getIdFoyer()).orElseThrow();
        assertNull(reloadedFoyer.getUniversite());
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        assertThrows(RuntimeException.class, () -> universiteService.getById(9999L));
    }
}
