package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Etudiant;
import com.example.TP_Foyer.Repository.EtudiantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EtudiantService implements IEtudiantService {
    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    @Override
    public Etudiant create(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    @Override
    public Etudiant update(Long id, Etudiant etudiant) {
        etudiant.setIdEtudiant(id);
        return etudiantRepository.save(etudiant);
    }

    @Override
    public void delete(Long id) {
        etudiantRepository.deleteById(id);
    }

    @Override
    public List<Etudiant> getAll() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant getById(Long id) {
        return etudiantRepository.findById(id).orElseThrow(() -> new RuntimeException("Etudiant not found"));
    }
}
