package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Entity.Reservation;
import com.example.TP_Foyer.Repository.BlocRepository;
import com.example.TP_Foyer.Repository.ChambreRepository;
import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceUnitTest {

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private ChambreService chambreService;

    @Test
    void createShouldSaveChambre() {
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.create(chambre);

        assertSame(chambre, result);
        verify(chambreRepository).save(chambre);
    }

    @Test
    void updateShouldSetIdAndSaveChambre() {
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.update(1L, chambre);

        assertEquals(1L, chambre.getIdChambre());
        assertSame(chambre, result);
        verify(chambreRepository).save(chambre);
    }

    @Test
    void deleteShouldCallRepository() {
        chambreService.delete(1L);
        verify(chambreRepository).deleteById(1L);
    }

    @Test
    void getAllShouldReturnAllChambres() {
        List<Chambre> chambres = Arrays.asList(new Chambre(), new Chambre());
        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> result = chambreService.getAll();

        assertEquals(2, result.size());
        verify(chambreRepository).findAll();
    }

    @Test
    void getByIdWhenFoundShouldReturnChambre() {
        Chambre chambre = new Chambre();
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre result = chambreService.getById(1L);

        assertSame(chambre, result);
        verify(chambreRepository).findById(1L);
    }

    @Test
    void getByIdWhenNotFoundShouldThrowException() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> chambreService.getById(1L));
        assertEquals("Chambre not found", exception.getMessage());
        verify(chambreRepository).findById(1L);
    }

    @Test
    void affecterChambreABlocShouldLinkChambreAndBloc() {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        Bloc bloc = new Bloc();
        bloc.setIdBloc(2L);

        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));
        when(blocRepository.findById(2L)).thenReturn(Optional.of(bloc));
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.affecterChambreABloc(1L, 2L);

        assertSame(bloc, chambre.getBloc());
        assertSame(chambre, result);
        verify(chambreRepository).findById(1L);
        verify(blocRepository).findById(2L);
        verify(chambreRepository).save(chambre);
    }

    @Test
    void getChambresParNomUniversiteShouldDelegateToRepository() {
        List<Chambre> chambres = Collections.singletonList(new Chambre());
        when(chambreRepository.findByBlocFoyerUniversiteNomUniversite("Uni"))
                .thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomUniversite("Uni");

        assertEquals(1, result.size());
        verify(chambreRepository).findByBlocFoyerUniversiteNomUniversite("Uni");
    }

    @Test
    void getChambresParBlocEtTypeShouldDelegateToRepository() {
        List<Chambre> chambres = Collections.singletonList(new Chambre());
        when(chambreRepository.findByBlocIdBlocAndTypeC(1L, TypeChambre.SIMPLE))
                .thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParBlocEtType(1L, TypeChambre.SIMPLE);

        assertEquals(1, result.size());
        verify(chambreRepository).findByBlocIdBlocAndTypeC(1L, TypeChambre.SIMPLE);
    }

    @Test
    void getChambresNonReserveParNomUniversiteEtTypeChambreShouldFilterByReservations() {
        Chambre chambre1 = new Chambre();
        chambre1.setReservations(new ArrayList<>());

        Chambre chambre2 = new Chambre();
        Reservation reservationValide = new Reservation();
        reservationValide.setEstValide(true);
        List<Reservation> reservations2 = new ArrayList<>();
        reservations2.add(reservationValide);
        chambre2.setReservations(reservations2);

        Chambre chambre3 = new Chambre();
        Reservation reservationNonValide = new Reservation();
        reservationNonValide.setEstValide(false);
        List<Reservation> reservations3 = new ArrayList<>();
        reservations3.add(reservationNonValide);
        chambre3.setReservations(reservations3);

        List<Chambre> fromRepo = Arrays.asList(chambre1, chambre2, chambre3);
        when(chambreRepository.findByBlocFoyerUniversiteNomUniversiteAndTypeC("Uni", TypeChambre.SIMPLE))
                .thenReturn(fromRepo);

        List<Chambre> result = chambreService.getChambresNonReserveParNomUniversiteEtTypeChambre("Uni", TypeChambre.SIMPLE);

        assertEquals(2, result.size());
        assertTrue(result.contains(chambre1));
        assertTrue(result.contains(chambre3));
        assertFalse(result.contains(chambre2));
        verify(chambreRepository).findByBlocFoyerUniversiteNomUniversiteAndTypeC("Uni", TypeChambre.SIMPLE);
    }

    @Test
    void countChambresParTypeDansUniversiteShouldDelegateToRepository() {
        List<Object[]> data = Collections.singletonList(new Object[]{TypeChambre.SIMPLE, 1L});
        when(chambreRepository.countChambresParTypeDansUniversite("Uni")).thenReturn(data);

        List<Object[]> result = chambreService.countChambresParTypeDansUniversite("Uni");

        assertEquals(1, result.size());
        verify(chambreRepository).countChambresParTypeDansUniversite("Uni");
    }

    @Test
    void getChambresDisponiblesSansReservationValideShouldDelegateToRepository() {
        List<Chambre> chambres = Collections.singletonList(new Chambre());
        when(chambreRepository.findChambresSansReservationValide()).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresDisponiblesSansReservationValide();

        assertEquals(1, result.size());
        verify(chambreRepository).findChambresSansReservationValide();
    }
}
