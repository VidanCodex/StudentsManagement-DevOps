package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Entity.Universite;
import com.example.TP_Foyer.Repository.FoyerRepository;
import com.example.TP_Foyer.Repository.UniversiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniversiteServiceUnitTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private UniversiteService universiteService;

    @Test
    void createShouldSaveUniversite() {
        Universite universite = new Universite();
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.create(universite);

        assertSame(universite, result);
        verify(universiteRepository).save(universite);
    }

    @Test
    void updateShouldSetIdAndSaveUniversite() {
        Universite universite = new Universite();
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.update(1L, universite);

        assertEquals(1L, universite.getIdUniversite());
        assertSame(universite, result);
        verify(universiteRepository).save(universite);
    }

    @Test
    void deleteShouldCallRepository() {
        universiteService.delete(1L);
        verify(universiteRepository).deleteById(1L);
    }

    @Test
    void getAllShouldReturnAllUniversites() {
        List<Universite> universites = Arrays.asList(new Universite(), new Universite());
        when(universiteRepository.findAll()).thenReturn(universites);

        List<Universite> result = universiteService.getAll();

        assertEquals(2, result.size());
        verify(universiteRepository).findAll();
    }

    @Test
    void getByIdWhenFoundShouldReturnUniversite() {
        Universite universite = new Universite();
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite result = universiteService.getById(1L);

        assertSame(universite, result);
        verify(universiteRepository).findById(1L);
    }

    @Test
    void getByIdWhenNotFoundShouldThrowException() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> universiteService.getById(1L));
        assertEquals("Universite not found", exception.getMessage());
        verify(universiteRepository).findById(1L);
    }

    @Test
    void affecterFoyerAUniversiteShouldLinkFoyerAndUniversite() {
        Universite universite = new Universite();
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(5L);

        when(universiteRepository.findByNomUniversite("Uni"))
                .thenReturn(Optional.of(universite));
        when(foyerRepository.findById(5L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.affecterFoyerAUniversite(5L, "Uni");

        assertSame(foyer, universite.getFoyer());
        assertSame(universite, foyer.getUniversite());
        assertSame(universite, result);

        verify(universiteRepository).findByNomUniversite("Uni");
        verify(foyerRepository).findById(5L);
        verify(universiteRepository).save(universite);
    }

    @Test
    void desaffecterFoyerAUniversiteShouldUnlinkFoyer() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        Foyer foyer = new Foyer();
        universite.setFoyer(foyer);
        foyer.setUniversite(universite);

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = universiteService.desaffecterFoyerAUniversite(1L);

        assertNull(universite.getFoyer());
        assertNull(foyer.getUniversite());
        assertSame(universite, result);
        verify(universiteRepository).findById(1L);
        verify(universiteRepository).save(universite);
    }

    @Test
    void desaffecterFoyerAUniversiteWhenNotFoundShouldThrowException() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> universiteService.desaffecterFoyerAUniversite(1L));

        assertEquals("Universite not found with id: 1", exception.getMessage());
        verify(universiteRepository).findById(1L);
    }
}
