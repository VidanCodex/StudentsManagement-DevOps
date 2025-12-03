package com.example.TP_Foyer.Controller;

import com.example.TP_Foyer.DTO.FoyerDTO;
import com.example.TP_Foyer.Entity.Foyer;
import com.example.TP_Foyer.Mapper.FoyerMapper;
import com.example.TP_Foyer.Service.IFoyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foyer")
public class FoyerController {
    private final IFoyerService iFoyerService;
    private final FoyerMapper foyerMapper;

    public FoyerController(IFoyerService iFoyerService, FoyerMapper foyerMapper) {
        this.iFoyerService = iFoyerService;
        this.foyerMapper = foyerMapper;
    }

    @PostMapping("/create")
    public FoyerDTO create(@RequestBody FoyerDTO foyerDTO) {
        Foyer foyer = foyerMapper.toEntity(foyerDTO);
        Foyer createdFoyer = iFoyerService.create(foyer);
        return foyerMapper.toDto(createdFoyer);
    }

    @PostMapping("/ajouterEtAffecterAUniversite/{idUniversite}")
    public Foyer ajouterFoyerEtAffecterAUniversite(
            @RequestBody Foyer foyer,
            @PathVariable long idUniversite) {
        return iFoyerService.ajouterFoyerEtAffecterAUniversite(foyer, idUniversite);
    }

    @PutMapping("/update/{id}")
    public Foyer update(@PathVariable Long id, @RequestBody Foyer foyer) {
        return iFoyerService.update(id, foyer);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        iFoyerService.delete(id);
    }

    @GetMapping("/getById/{id}")
    public Foyer getById(@PathVariable Long id) {
        return iFoyerService.getById(id);
    }

    @GetMapping("/getAll")
    public List<Foyer> getAll() {
        return iFoyerService.getAll();
    }
}
