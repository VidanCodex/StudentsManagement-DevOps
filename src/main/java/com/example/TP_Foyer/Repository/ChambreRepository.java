package com.example.TP_Foyer.Repository;

import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    Optional<Chambre> findByNumeroChambre(Long numeroChambre);
    List<Chambre> findByNumeroChambreIn(List<Long> numerosChambre);
    List<Chambre> findByBlocIdBloc(Long idBloc);
    List<Chambre> findByBlocIdBlocAndTypeC(Long idBloc, TypeChambre typeC);
    List<Chambre> findByBlocFoyerUniversiteNomUniversite(String nomUniversite);
    List<Chambre> findByBlocFoyerUniversiteNomUniversiteAndTypeC(String nomUniversite, TypeChambre typeC);

    @Query("select c.typeC, count(c) from Chambre c where c.bloc.foyer.universite.nomUniversite = :nomUni group by c.typeC")
    List<Object[]> countChambresParTypeDansUniversite(@Param("nomUni") String nomUniversite);

    @Query("select c from Chambre c where not exists (select r from Reservation r where r member of c.reservations and r.estValide = true)")
    List<Chambre> findChambresSansReservationValide();
}
