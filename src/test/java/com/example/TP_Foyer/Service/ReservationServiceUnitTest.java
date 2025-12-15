package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Entity.Etudiant;
import com.example.TP_Foyer.Entity.Reservation;
import com.example.TP_Foyer.Repository.BlocRepository;
import com.example.TP_Foyer.Repository.ChambreRepository;
import com.example.TP_Foyer.Repository.EtudiantRepository;
import com.example.TP_Foyer.Repository.ReservationRepository;
import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceUnitTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void createShouldSaveReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.create(reservation);

        assertSame(reservation, result);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateShouldSetIdAndSaveReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.update("RES-1", reservation);

        assertEquals("RES-1", reservation.getIdReservation());
        assertSame(reservation, result);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void deleteShouldCallRepository() {
        reservationService.delete("RES-1");
        verify(reservationRepository).deleteById("RES-1");
    }

    @Test
    void getAllShouldReturnAllReservations() {
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAll();

        assertEquals(2, result.size());
        verify(reservationRepository).findAll();
    }

    @Test
    void getByIdWhenFoundShouldReturnReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.findById("RES-1")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getById("RES-1");

        assertSame(reservation, result);
        verify(reservationRepository).findById("RES-1");
    }

    @Test
    void getByIdWhenNotFoundShouldThrowException() {
        when(reservationRepository.findById("RES-1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.getById("RES-1"));

        assertEquals("Reservation not found", exception.getMessage());
        verify(reservationRepository).findById("RES-1");
    }

    @Test
    void ajouterReservationShouldCreateReservationWhenChambreAvailable() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("B1");

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(123L);

        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setReservations(new ArrayList<>());

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        when(etudiantRepository.findByCin(123L)).thenReturn(Optional.of(etudiant));
        when(chambreRepository.findByBlocIdBloc(1L)).thenReturn(Collections.singletonList(chambre));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = reservationService.ajouterReservation(1L, 123L);

        assertNotNull(result.getIdReservation());
        assertTrue(result.getIdReservation().contains("101"));
        assertTrue(result.getIdReservation().contains("B1"));
        assertTrue(result.getEstValide());
        assertEquals(1, result.getEtudiants().size());
        assertSame(etudiant, result.getEtudiants().get(0));
        assertNotNull(result.getAnneeUniversitaire());

        assertNotNull(chambre.getReservations());
        assertTrue(chambre.getReservations().contains(result));

        verify(blocRepository).findById(1L);
        verify(etudiantRepository).findByCin(123L);
        verify(chambreRepository).findByBlocIdBloc(1L);
        verify(reservationRepository).save(any(Reservation.class));
        verify(chambreRepository).save(chambre);
    }

    @Test
    void ajouterReservationWhenNoAvailableChambreShouldThrowException() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(123L);

        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);

        Reservation existingReservation = new Reservation();
        existingReservation.setEstValide(true);
        chambre.setReservations(Collections.singletonList(existingReservation));

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        when(etudiantRepository.findByCin(123L)).thenReturn(Optional.of(etudiant));
        when(chambreRepository.findByBlocIdBloc(1L)).thenReturn(Collections.singletonList(chambre));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.ajouterReservation(1L, 123L));

        assertEquals("Aucune chambre disponible dans ce bloc", exception.getMessage());
        verify(chambreRepository).findByBlocIdBloc(1L);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void annulerReservationShouldInvalidateReservationAndRemoveFromChambre() {
        Etudiant etudiant = new Etudiant();
        etudiant.setCin(123L);

        Reservation reservation = new Reservation();
        reservation.setIdReservation("RES-1");
        reservation.setEstValide(true);
        reservation.setEtudiants(new ArrayList<>(Collections.singletonList(etudiant)));

        Chambre chambre = new Chambre();
        chambre.setReservations(new ArrayList<>(Collections.singletonList(reservation)));

        when(reservationRepository.findByEtudiantsCinAndEstValide(123L, true))
                .thenReturn(Optional.of(reservation));
        when(chambreRepository.findAll()).thenReturn(Collections.singletonList(chambre));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.annulerReservation(123L);

        assertFalse(result.getEstValide());
        assertTrue(result.getEtudiants().isEmpty());
        assertFalse(chambre.getReservations().contains(reservation));

        verify(reservationRepository)
                .findByEtudiantsCinAndEstValide(123L, true);
        verify(chambreRepository).findAll();
        verify(chambreRepository).save(chambre);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void annulerReservationWhenNotFoundShouldThrowException() {
        when(reservationRepository.findByEtudiantsCinAndEstValide(123L, true))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.annulerReservation(123L));

        assertTrue(exception.getMessage().contains("Aucune réservation valide trouvée"));
        verify(reservationRepository).findByEtudiantsCinAndEstValide(123L, true);
    }

    @Test
    void getReservationParAnneeUniversitaireEtNomUniversiteShouldFilterByYearAndUniversite() {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JANUARY, 1);
        Date targetYear = cal.getTime();

        Reservation res2024 = new Reservation();
        res2024.setIdReservation("R-2024");
        res2024.setAnneeUniversitaire(targetYear);

        cal.set(2023, Calendar.JANUARY, 1);
        Reservation res2023 = new Reservation();
        res2023.setIdReservation("R-2023");
        res2023.setAnneeUniversitaire(cal.getTime());

        Chambre chambre = new Chambre();
        chambre.setReservations(new ArrayList<>(Collections.singletonList(res2024)));

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(res2024, res2023));
        when(chambreRepository.findByBlocFoyerUniversiteNomUniversite("Uni"))
                .thenReturn(Collections.singletonList(chambre));

        List<Reservation> result = reservationService
                .getReservationParAnneeUniversitaireEtNomUniversite(targetYear, "Uni");

        assertEquals(1, result.size());
        assertSame(res2024, result.get(0));

        verify(reservationRepository).findAll();
        verify(chambreRepository).findByBlocFoyerUniversiteNomUniversite("Uni");
    }
}
