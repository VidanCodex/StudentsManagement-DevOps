package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import java.util.List;

public interface IChambreService {
    Chambre create(Chambre chambre);

    Chambre update(Long id, Chambre chambre);

    void delete(Long id);

    List<Chambre> getAll();

    Chambre getById(Long id);

    Chambre affecterChambreABloc(Long idChambre, Long idBloc);

    List<Chambre> getChambresParNomUniversite(String nomUniversite);

    List<Chambre> getChambresParBlocEtType(long idBloc, TypeChambre typeC);

    List<Chambre> getChambresNonReserveParNomUniversiteEtTypeChambre(String nomUniversite, TypeChambre type);

    java.util.List<Object[]> countChambresParTypeDansUniversite(String nomUniversite);

    java.util.List<Chambre> getChambresDisponiblesSansReservationValide();
}