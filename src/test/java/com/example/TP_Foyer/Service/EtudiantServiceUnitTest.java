package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Etudiant;
import com.example.TP_Foyer.Repository.EtudiantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceUnitTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    @Test
    void createShouldSaveEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.create(etudiant);

        assertSame(etudiant, result);
        verify(etudiantRepository).save(etudiant);
    }

    @Test
    void updateShouldSetIdAndSaveEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.update(1L, etudiant);

        assertEquals(1L, etudiant.getIdEtudiant());
        assertSame(etudiant, result);
        verify(etudiantRepository).save(etudiant);
    }

    @Test
    void deleteShouldCallRepository() {
        etudiantService.delete(1L);
        verify(etudiantRepository).deleteById(1L);
    }

    @Test
    void getAllShouldReturnAllEtudiants() {
        List<Etudiant> etudiants = Arrays.asList(new Etudiant(), new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.getAll();

        assertEquals(2, result.size());
        verify(etudiantRepository).findAll();
    }

    @Test
    void getByIdWhenFoundShouldReturnEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.getById(1L);

        assertSame(etudiant, result);
        verify(etudiantRepository).findById(1L);
    }

    @Test
    void getByIdWhenNotFoundShouldThrowException() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> etudiantService.getById(1L));
        assertEquals("Etudiant not found", exception.getMessage());
        verify(etudiantRepository).findById(1L);
    }

    @Test
    void getEtudiantsAvecReservationValideParAnneeShouldDelegateToRepository() {
        List<Etudiant> etudiants = Collections.singletonList(new Etudiant());
        when(etudiantRepository.findEtudiantsAvecReservationValideParAnnee(2024)).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.getEtudiantsAvecReservationValideParAnnee(2024);

        assertEquals(1, result.size());
        verify(etudiantRepository).findEtudiantsAvecReservationValideParAnnee(2024);
    }

    @Test
    void getEtudiantsSansReservationsShouldDelegateToRepository() {
        List<Etudiant> etudiants = Collections.singletonList(new Etudiant());
        when(etudiantRepository.findEtudiantsSansReservations()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.getEtudiantsSansReservations();

        assertEquals(1, result.size());
        verify(etudiantRepository).findEtudiantsSansReservations();
    }
}
