package com.example.TP_Foyer.Repository;

import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    Optional<Chambre> findByNumeroChambre(Long numeroChambre);
    List<Chambre> findByNumeroChambreIn(List<Long> numerosChambre);
    List<Chambre> findByBlocIdBloc(Long idBloc);
    List<Chambre> findByBlocIdBlocAndTypeC(Long idBloc, TypeChambre typeC);
    List<Chambre> findByBlocFoyerUniversiteNomUniversite(String nomUniversite);
    List<Chambre> findByBlocFoyerUniversiteNomUniversiteAndTypeC(String nomUniversite, TypeChambre typeC);
}
