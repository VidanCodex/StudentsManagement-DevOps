package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Entity.Universite;
import com.example.TP_Foyer.Repository.FoyerRepository;
import com.example.TP_Foyer.Repository.UniversiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoyerService implements IFoyerService {
    private final FoyerRepository foyerRepository;
    private final UniversiteRepository universiteRepository;

    public FoyerService(FoyerRepository foyerRepository, UniversiteRepository universiteRepository) {
        this.foyerRepository = foyerRepository;
        this.universiteRepository = universiteRepository;
    }

    @Override
    public Foyer create(Foyer foyer) {
        return foyerRepository.save(foyer);
    }

    @Override
    public Foyer update(Long id, Foyer foyer) {
        foyer.setIdFoyer(id);
        return foyerRepository.save(foyer);
    }

    @Override
    public void delete(Long id) {
        foyerRepository.deleteById(id);
    }

    @Override
    public List<Foyer> getAll() {
        return foyerRepository.findAll();
    }

    @Override
    public Foyer getById(Long id) {
        return foyerRepository.findById(id).orElseThrow(() -> new RuntimeException("Foyer not found"));
    }

    @Override
    @Transactional
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Universite not found with id: " + idUniversite));

        if (foyer.getBlocs() != null) {
            for (Bloc bloc : foyer.getBlocs()) {
                bloc.setFoyer(foyer);
            }
        }

        Foyer savedFoyer = foyerRepository.save(foyer);

        universite.setFoyer(savedFoyer);

        universiteRepository.save(universite);

        return foyerRepository.findById(savedFoyer.getIdFoyer()).orElse(savedFoyer);
    }

    @Override
    public Foyer getFoyerAvecMaxChambres() {
        java.util.List<Foyer> foyers = foyerRepository.findFoyersOrderByNombreChambresDesc();
        if (foyers.isEmpty()) {
            throw new RuntimeException("Aucun foyer trouvé");
        }
        return foyers.get(0);
    }
}
