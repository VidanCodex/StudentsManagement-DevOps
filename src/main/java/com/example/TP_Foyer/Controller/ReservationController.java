package com.example.TP_Foyer.Controller;

import com.example.TP_Foyer.DTO.ReservationDTO;
import com.example.TP_Foyer.Entity.Reservation;
import com.example.TP_Foyer.Mapper.ReservationMapper;
import com.example.TP_Foyer.Service.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final IReservationService iReservationService;
    private final ReservationMapper reservationMapper;

    public ReservationController(IReservationService iReservationService, ReservationMapper reservationMapper) {
        this.iReservationService = iReservationService;
        this.reservationMapper = reservationMapper;
    }

    @PostMapping("/create")
    public ReservationDTO create(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        Reservation createdReservation = iReservationService.create(reservation);
        return reservationMapper.toDto(createdReservation);
    }

    @PostMapping("/ajouter/{idBloc}")
    public Reservation ajouterReservation(
            @PathVariable long idBloc,
            @RequestParam long cinEtudiant) {
        return iReservationService.ajouterReservation(idBloc, cinEtudiant);
    }

    @PostMapping("/annuler")
    public Reservation annulerReservation(@RequestParam long cinEtudiant) {
        return iReservationService.annulerReservation(cinEtudiant);
    }

    @PutMapping("/update/{id}")
    public Reservation update(@PathVariable String id, @RequestBody Reservation reservation) {
        return iReservationService.update(id, reservation);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        iReservationService.delete(id);
    }

    @GetMapping("/getById/{id}")
    public Reservation getById(@PathVariable String id) {
        return iReservationService.getById(id);
    }

    @GetMapping("/getAll")
    public List<Reservation> getAll() {
        return iReservationService.getAll();
    }

    @GetMapping("/parAnneeEtUniversite")
    public List<Reservation> getReservationParAnneeUniversitaireEtNomUniversite(
            @RequestParam java.util.Date anneeUniversite,
            @RequestParam String nomUniversite) {
        return iReservationService.getReservationParAnneeUniversitaireEtNomUniversite(anneeUniversite, nomUniversite);
    }
}
