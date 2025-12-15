package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Repository.BlocRepository;
import com.example.TP_Foyer.Repository.ChambreRepository;
import com.example.TP_Foyer.Repository.FoyerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BlocServiceIntegrationTest {

    @Autowired
    private BlocService blocService;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Test
    void createAndGetById_shouldPersistBloc() {
        Bloc bloc = new Bloc();
        bloc.setNomBloc("B1");
        bloc.setCapaciteBloc(10L);

        Bloc saved = blocService.create(bloc);

        assertNotNull(saved.getIdBloc());

        Bloc found = blocService.getById(saved.getIdBloc());
        assertEquals("B1", found.getNomBloc());
    }

    @Test
    void affecterBlocAFoyer_shouldAssociateFoyerToBloc() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F1");
        foyer.setCapaciteFoyer(100L);
        foyer = foyerRepository.save(foyer);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("B1");
        bloc.setCapaciteBloc(10L);
        bloc = blocRepository.save(bloc);

        Bloc updated = blocService.affecterBlocAFoyer(bloc.getIdBloc(), foyer.getIdFoyer());

        assertNotNull(updated.getFoyer());
        assertEquals(foyer.getIdFoyer(), updated.getFoyer().getIdFoyer());
    }

    @Test
    void affecterChambresABloc_shouldAssignBlocToChambres() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F1");
        foyer.setCapaciteFoyer(100L);
        foyer = foyerRepository.save(foyer);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("B2");
        bloc.setCapaciteBloc(20L);
        bloc.setFoyer(foyer);
        bloc = blocRepository.save(bloc);

        Chambre c1 = new Chambre();
        c1.setNumeroChambre(101L);
        Chambre c2 = new Chambre();
        c2.setNumeroChambre(102L);
        chambreRepository.saveAll(Arrays.asList(c1, c2));

        Bloc result = blocService.affecterChambresABloc(Arrays.asList(101L, 102L), bloc.getIdBloc());

        List<Chambre> chambres = chambreRepository.findByNumeroChambreIn(Arrays.asList(101L, 102L));
        assertEquals(2, chambres.size());
        assertEquals(bloc.getIdBloc(), chambres.get(0).getBloc().getIdBloc());
        assertEquals(bloc.getIdBloc(), chambres.get(1).getBloc().getIdBloc());
        assertEquals(bloc.getIdBloc(), result.getIdBloc());
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        assertThrows(RuntimeException.class, () -> blocService.getById(9999L));
    }
}
