package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Foyer;
import java.util.List;

public interface IFoyerService {
    Foyer create(Foyer foyer);
    Foyer update(Long id, Foyer foyer);
    void delete(Long id);
    List<Foyer> getAll();
    Foyer getById(Long id);
    Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite);
    Foyer getFoyerAvecMaxChambres();
}
