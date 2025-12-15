package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Entity.Universite;
import com.example.TP_Foyer.Repository.FoyerRepository;
import com.example.TP_Foyer.Repository.UniversiteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FoyerServiceIntegrationTest {

    @Autowired
    private FoyerService foyerService;

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private UniversiteRepository universiteRepository;

    @Test
    void createAndGetById_shouldPersistFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F1");
        foyer.setCapaciteFoyer(100L);

        Foyer saved = foyerService.create(foyer);

        assertNotNull(saved.getIdFoyer());

        Foyer found = foyerService.getById(saved.getIdFoyer());
        assertEquals("F1", found.getNomFoyer());
    }

    @Test
    void ajouterFoyerEtAffecterAUniversite_shouldLinkEntities() {
        Universite universite = new Universite();
        universite.setNomUniversite("Uni1");
        universite.setAdresse("Addr");
        universite = universiteRepository.save(universite);

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F2");
        foyer.setCapaciteFoyer(200L);

        Bloc b1 = new Bloc();
        b1.setNomBloc("B1");
        Bloc b2 = new Bloc();
        b2.setNomBloc("B2");
        foyer.setBlocs(Arrays.asList(b1, b2));

        Foyer saved = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, universite.getIdUniversite());

        assertNotNull(saved.getIdFoyer());
        assertEquals(2, saved.getBlocs().size());

        Universite reloadedUni = universiteRepository.findById(universite.getIdUniversite()).orElseThrow();
        assertNotNull(reloadedUni.getFoyer());
        assertEquals(saved.getIdFoyer(), reloadedUni.getFoyer().getIdFoyer());
    }

    @Test
    void getFoyerAvecMaxChambres_whenNoFoyer_shouldThrow() {
        foyerRepository.deleteAll();
        assertThrows(RuntimeException.class, () -> foyerService.getFoyerAvecMaxChambres());
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        assertThrows(RuntimeException.class, () -> foyerService.getById(9999L));
    }
}
