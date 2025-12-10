package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Etudiant;
import java.util.List;

public interface IEtudiantService {
    Etudiant create(Etudiant etudiant);
    Etudiant update(Long id, Etudiant etudiant);
    void delete(Long id);
    List<Etudiant> getAll();
    Etudiant getById(Long id);
    List<Etudiant> getEtudiantsAvecReservationValideParAnnee(int annee);
    List<Etudiant> getEtudiantsSansReservations();
}
