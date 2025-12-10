package com.example.TP_Foyer.Repository;

import com.example.TP_Foyer.Entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Optional<Etudiant> findByCin(Long cin);

    @Query("select distinct e from Etudiant e join e.reservations r where r.estValide = true and FUNCTION('YEAR', r.anneeUniversitaire) = :annee")
    List<Etudiant> findEtudiantsAvecReservationValideParAnnee(@Param("annee") int annee);

    @Query("select e from Etudiant e left join e.reservations r where r is null")
    List<Etudiant> findEtudiantsSansReservations();

}
