package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Entity.Universite;
import com.example.TP_Foyer.Repository.FoyerRepository;
import com.example.TP_Foyer.Repository.UniversiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class FoyerServiceUnitTest {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private FoyerService foyerService;

    @Test
    void createShouldSaveFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.create(foyer);

        assertSame(foyer, result);
        verify(foyerRepository).save(foyer);
    }

    @Test
    void updateShouldSetIdAndSaveFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.update(1L, foyer);

        assertEquals(1L, foyer.getIdFoyer());
        assertSame(foyer, result);
        verify(foyerRepository).save(foyer);
    }

    @Test
    void deleteShouldCallRepository() {
        foyerService.delete(1L);
        verify(foyerRepository).deleteById(1L);
    }

    @Test
    void getAllShouldReturnAllFoyers() {
        List<Foyer> foyers = Arrays.asList(new Foyer(), new Foyer());
        when(foyerRepository.findAll()).thenReturn(foyers);

        List<Foyer> result = foyerService.getAll();

        assertEquals(2, result.size());
        verify(foyerRepository).findAll();
    }

    @Test
    void getByIdWhenFoundShouldReturnFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.getById(1L);

        assertSame(foyer, result);
        verify(foyerRepository).findById(1L);
    }

    @Test
    void getByIdWhenNotFoundShouldThrowException() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> foyerService.getById(1L));
        assertEquals("Foyer not found", exception.getMessage());
        verify(foyerRepository).findById(1L);
    }

    @Test
    void ajouterFoyerEtAffecterAUniversiteShouldLinkFoyerBlocsAndUniversite() {
        Foyer foyer = new Foyer();
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        foyer.setBlocs(Arrays.asList(bloc1, bloc2));

        Universite universite = new Universite();
        universite.setIdUniversite(10L);

        when(universiteRepository.findById(10L)).thenReturn(Optional.of(universite));
        when(foyerRepository.save(foyer)).thenAnswer(invocation -> {
            foyer.setIdFoyer(1L);
            return foyer;
        });
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 10L);

        assertEquals(1L, foyer.getIdFoyer());
        assertSame(foyer, bloc1.getFoyer());
        assertSame(foyer, bloc2.getFoyer());
        assertSame(foyer, universite.getFoyer());
        assertSame(foyer, result);

        verify(universiteRepository).findById(10L);
        verify(foyerRepository).save(foyer);
        verify(universiteRepository).save(universite);
        verify(foyerRepository).findById(1L);
    }

    @Test
    void ajouterFoyerEtAffecterAUniversiteWhenUniversiteNotFoundShouldThrowException() {
        Foyer foyer = new Foyer();
        when(universiteRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 10L));

        assertEquals("Universite not found with id: 10", exception.getMessage());
        verify(universiteRepository).findById(10L);
        verifyNoInteractions(foyerRepository);
    }

    @Test
    void getFoyerAvecMaxChambresShouldReturnFirstFoyer() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        when(foyerRepository.findFoyersOrderByNombreChambresDesc())
                .thenReturn(Arrays.asList(foyer1, foyer2));

        Foyer result = foyerService.getFoyerAvecMaxChambres();

        assertSame(foyer1, result);
        verify(foyerRepository).findFoyersOrderByNombreChambresDesc();
    }

    @Test
    void getFoyerAvecMaxChambresWhenListEmptyShouldThrowException() {
        when(foyerRepository.findFoyersOrderByNombreChambresDesc())
                .thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> foyerService.getFoyerAvecMaxChambres());

        assertEquals("Aucun foyer trouvé", exception.getMessage());
        verify(foyerRepository).findFoyersOrderByNombreChambresDesc();
    }
}
