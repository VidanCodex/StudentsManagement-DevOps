package com.example.TP_Foyer.Repository;

import com.example.TP_Foyer.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    Optional<Reservation> findByEtudiantsCinAndEstValide(Long cin, Boolean estValide);
}
