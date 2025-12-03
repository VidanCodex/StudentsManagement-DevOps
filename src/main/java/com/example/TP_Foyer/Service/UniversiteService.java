package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Entity.Universite;
import com.example.TP_Foyer.Repository.FoyerRepository;
import com.example.TP_Foyer.Repository.UniversiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UniversiteService implements IUniversiteService {
    private final UniversiteRepository universiteRepository;
    private final FoyerRepository foyerRepository;

    public UniversiteService(UniversiteRepository universiteRepository, FoyerRepository foyerRepository) {
        this.universiteRepository = universiteRepository;
        this.foyerRepository = foyerRepository;
    }

    @Override
    public Universite create(Universite universite) {
        return universiteRepository.save(universite);
    }

    @Override
    public Universite update(Long id, Universite universite) {
        universite.setIdUniversite(id);
        return universiteRepository.save(universite);
    }

    @Override
    public void delete(Long id) {
        universiteRepository.deleteById(id);
    }

    @Override
    public List<Universite> getAll() {
        return universiteRepository.findAll();
    }

    @Override
    public Universite getById(Long id) {
        return universiteRepository.findById(id).orElseThrow(() -> new RuntimeException("Universite not found"));
    }

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        Universite universite = universiteRepository.findByNomUniversite(nomUniversite)
                .orElseThrow(() -> new RuntimeException("Universite not found with name: " + nomUniversite));

        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer not found with id: " + idFoyer));

        universite.setFoyer(foyer);
        foyer.setUniversite(universite);

        return universiteRepository.save(universite);
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Universite not found with id: " + idUniversite));

        if (universite.getFoyer() != null) {
            Foyer foyer = universite.getFoyer();
            universite.setFoyer(null);
            foyer.setUniversite(null);
        }

        return universiteRepository.save(universite);
    }
}
