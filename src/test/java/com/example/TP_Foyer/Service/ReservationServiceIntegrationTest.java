package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Entity.Etudiant;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Entity.Reservation;
import com.example.TP_Foyer.Repository.BlocRepository;
import com.example.TP_Foyer.Repository.ChambreRepository;
import com.example.TP_Foyer.Repository.EtudiantRepository;
import com.example.TP_Foyer.Repository.FoyerRepository;
import com.example.TP_Foyer.Repository.ReservationRepository;
import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Test
    void ajouterReservation_shouldCreateReservationAndLinkEntities() {
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
        chambre.setNumeroChambre(101L);
        chambre.setCapacite(1L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setBloc(bloc);
        chambre = chambreRepository.save(chambre);

        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("Doe");
        etudiant.setPrenomEt("John");
        etudiant.setCin(123L);
        etudiant = etudiantRepository.save(etudiant);

        Reservation reservation = reservationService.ajouterReservation(bloc.getIdBloc(), etudiant.getCin());

        assertNotNull(reservation.getIdReservation());
        assertTrue(reservation.getEstValide());
        assertEquals(1, reservation.getEtudiants().size());
        assertEquals(etudiant.getCin(), reservation.getEtudiants().get(0).getCin());

        Reservation persisted = reservationRepository.findById(reservation.getIdReservation()).orElseThrow();
        assertTrue(persisted.getEstValide());
    }

    @Test
    void ajouterReservation_whenNoAvailableChambre_shouldThrow() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F2");
        foyer.setCapaciteFoyer(100L);
        foyer = foyerRepository.save(foyer);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("B2");
        bloc.setCapaciteBloc(10L);
        bloc.setFoyer(foyer);
        bloc = blocRepository.save(bloc);

        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(201L);
        chambre.setCapacite(1L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setBloc(bloc);
        chambre = chambreRepository.save(chambre);

        Etudiant etu1 = new Etudiant();
        etu1.setNomEt("A");
        etu1.setPrenomEt("A");
        etu1.setCin(1L);
        etu1 = etudiantRepository.save(etu1);

        Etudiant etu2 = new Etudiant();
        etu2.setNomEt("B");
        etu2.setPrenomEt("B");
        etu2.setCin(2L);
        etu2 = etudiantRepository.save(etu2);

        reservationService.ajouterReservation(bloc.getIdBloc(), etu1.getCin());

        Long blocId = bloc.getIdBloc();
        Long etu2Cin = etu2.getCin();

        assertThrows(RuntimeException.class,
                () -> reservationService.ajouterReservation(blocId, etu2Cin));
    }

    @Test
    void annulerReservation_shouldInvalidateReservationAndClearEtudiants() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F3");
        foyer.setCapaciteFoyer(100L);
        foyer = foyerRepository.save(foyer);

        Bloc bloc = new Bloc();
        bloc.setNomBloc("B3");
        bloc.setCapaciteBloc(10L);
        bloc.setFoyer(foyer);
        bloc = blocRepository.save(bloc);

        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(301L);
        chambre.setCapacite(1L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setBloc(bloc);
        chambre = chambreRepository.save(chambre);

        Etudiant etu = new Etudiant();
        etu.setNomEt("C");
        etu.setPrenomEt("C");
        etu.setCin(10L);
        etu = etudiantRepository.save(etu);

        Reservation reservation = reservationService.ajouterReservation(bloc.getIdBloc(), etu.getCin());

        Reservation cancelled = reservationService.annulerReservation(etu.getCin());

        assertFalse(cancelled.getEstValide());
        assertTrue(cancelled.getEtudiants().isEmpty());
    }
}
