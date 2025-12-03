package com.example.TP_Foyer.Repository;

import com.example.TP_Foyer.Entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByCin(Long cin);
}
