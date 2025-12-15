package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Repository.BlocRepository;
import com.example.TP_Foyer.Repository.ChambreRepository;
import com.example.TP_Foyer.Repository.FoyerRepository;
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
class BlocServiceUnitTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private BlocService blocService;

    @Test
    void createShouldSaveBloc() {
        Bloc bloc = new Bloc();
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.create(bloc);

        assertSame(bloc, result);
        verify(blocRepository).save(bloc);
    }

    @Test
    void updateShouldSetIdAndSaveBloc() {
        Bloc bloc = new Bloc();
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.update(1L, bloc);

        assertEquals(1L, bloc.getIdBloc());
        assertSame(bloc, result);
        verify(blocRepository).save(bloc);
    }

    @Test
    void deleteShouldCallRepository() {
        blocService.delete(1L);
        verify(blocRepository).deleteById(1L);
    }

    @Test
    void getAllShouldReturnAllBlocs() {
        List<Bloc> blocs = Arrays.asList(new Bloc(), new Bloc());
        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.getAll();

        assertEquals(2, result.size());
        verify(blocRepository).findAll();
    }

    @Test
    void getByIdWhenFoundShouldReturnBloc() {
        Bloc bloc = new Bloc();
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.getById(1L);

        assertSame(bloc, result);
        verify(blocRepository).findById(1L);
    }

    @Test
    void getByIdWhenNotFoundShouldThrowException() {
        when(blocRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> blocService.getById(1L));
        assertEquals("Bloc not found", exception.getMessage());
        verify(blocRepository).findById(1L);
    }

    @Test
    void affecterBlocAFoyerShouldLinkBlocAndFoyer() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(2L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        when(foyerRepository.findById(2L)).thenReturn(Optional.of(foyer));
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.affecterBlocAFoyer(1L, 2L);

        assertSame(foyer, bloc.getFoyer());
        assertSame(bloc, result);
        verify(blocRepository).findById(1L);
        verify(foyerRepository).findById(2L);
        verify(blocRepository).save(bloc);
    }

    @Test
    void affecterChambresABlocShouldAssignBlocToChambres() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        List<Chambre> chambres = Arrays.asList(chambre1, chambre2);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        when(chambreRepository.findByNumeroChambreIn(Arrays.asList(101L, 102L))).thenReturn(chambres);
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.affecterChambresABloc(Arrays.asList(101L, 102L), 1L);

        assertSame(bloc, chambre1.getBloc());
        assertSame(bloc, chambre2.getBloc());
        assertSame(bloc, result);
        verify(chambreRepository).findByNumeroChambreIn(Arrays.asList(101L, 102L));
        verify(chambreRepository).saveAll(chambres);
    }

    @Test
    void affecterChambresABlocWhenNoChambresFoundShouldThrowException() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        when(chambreRepository.findByNumeroChambreIn(Arrays.asList(101L, 102L))).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> blocService.affecterChambresABloc(Arrays.asList(101L, 102L), 1L));

        assertEquals("No chambres found with the provided numbers", exception.getMessage());
        verify(chambreRepository).findByNumeroChambreIn(Arrays.asList(101L, 102L));
    }
}
