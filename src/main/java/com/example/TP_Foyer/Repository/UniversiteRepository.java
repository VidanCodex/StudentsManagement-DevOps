package com.example.TP_Foyer.Repository;

import com.example.TP_Foyer.Entity.Universite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversiteRepository extends JpaRepository<Universite, Long> {
    Optional<Universite> findByNomUniversite(String nomUniversite);}
