package com.example.TP_Foyer.Service;

import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Repository.BlocRepository;
import com.example.TP_Foyer.Repository.ChambreRepository;
import com.example.TP_Foyer.Repository.FoyerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BlocService implements IBlocService {
    private final BlocRepository blocRepository;
    private final FoyerRepository foyerRepository;
    private final ChambreRepository chambreRepository;

    public BlocService(BlocRepository blocRepository, FoyerRepository foyerRepository, ChambreRepository chambreRepository) {
        this.blocRepository = blocRepository;
        this.foyerRepository = foyerRepository;
        this.chambreRepository = chambreRepository;
    }

    @Override
    public Bloc create(Bloc bloc) {
        return blocRepository.save(bloc);
    }

    @Override
    public Bloc update(Long id, Bloc bloc) {
        bloc.setIdBloc(id);
        return blocRepository.save(bloc);
    }

    @Override
    public void delete(Long id) {
        blocRepository.deleteById(id);
    }

    @Override
    public List<Bloc> getAll() {
        return blocRepository.findAll();
    }

    @Override
    public Bloc getById(Long id) {
        return blocRepository.findById(id).orElseThrow(() -> new RuntimeException("Bloc not found"));
    }

    @Override
    public Bloc affecterBlocAFoyer(Long idBloc, Long idFoyer) {
        Bloc bloc = blocRepository.findById(idBloc)
                .orElseThrow(() -> new RuntimeException("Bloc not found"));

        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer not found"));

        bloc.setFoyer(foyer);
        return blocRepository.save(bloc);
    }

    @Override
    public Bloc affecterChambresABloc(List<Long> numChambre, long idBloc) {
        Bloc bloc = blocRepository.findById(idBloc)
                .orElseThrow(() -> new RuntimeException("Bloc not found with id: " + idBloc));

        List<Chambre> chambres = chambreRepository.findByNumeroChambreIn(numChambre);

        if (chambres.isEmpty()) {
            throw new RuntimeException("No chambres found with the provided numbers");
        }

        for (Chambre chambre : chambres) {
            chambre.setBloc(bloc);
        }

        chambreRepository.saveAll(chambres);
        return blocRepository.findById(idBloc).orElse(bloc);
    }
}
