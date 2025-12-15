package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Repository.BlocRepository;
import com.example.TP_Foyer.Repository.ChambreRepository;
import com.example.TP_Foyer.Repository.FoyerRepository;
import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChambreServiceIntegrationTest {

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Test
    void createAndGetById_shouldPersistChambre() {
        Chambre c = new Chambre();
        c.setNumeroChambre(101L);
        c.setCapacite(1L);
        c.setTypeC(TypeChambre.SIMPLE);

        Chambre saved = chambreService.create(c);

        assertNotNull(saved.getIdChambre());

        Chambre found = chambreService.getById(saved.getIdChambre());
        assertEquals(101L, found.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, found.getTypeC());
    }

    @Test
    void affecterChambreABloc_shouldAssociateBlocToChambre() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F1");
        foyer.setCapaciteFoyer(100L);
        foyer = foyerRepository.save(foyer);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("B1");
        bloc.setCapaciteBloc(10L);
        bloc.setFoyer(foyer);
        bloc = blocRepository.save(bloc);

        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(201L);
        chambre.setCapacite(1L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre = chambreRepository.save(chambre);

        Chambre updated = chambreService.affecterChambreABloc(chambre.getIdChambre(), bloc.getIdBloc());

        assertNotNull(updated.getBloc());
        assertEquals(bloc.getIdBloc(), updated.getBloc().getIdBloc());
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        assertThrows(RuntimeException.class, () -> chambreService.getById(9999L));
    }
}
