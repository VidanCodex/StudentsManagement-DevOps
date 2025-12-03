package com.example.TP_Foyer.Controller;

import com.example.TP_Foyer.DTO.UniversiteDTO;
import com.example.TP_Foyer.Entity.Universite;
import com.example.TP_Foyer.Mapper.UniversiteMapper;
import com.example.TP_Foyer.Service.IUniversiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/universite")
public class UniversiteController {
    private final IUniversiteService iUniversiteService;
    private final UniversiteMapper universiteMapper;

    public UniversiteController(IUniversiteService iUniversiteService, UniversiteMapper universiteMapper) {
        this.iUniversiteService = iUniversiteService;
        this.universiteMapper = universiteMapper;
    }

    @PostMapping("/create")
    public UniversiteDTO create(@RequestBody UniversiteDTO universiteDTO) {
        Universite universite = universiteMapper.toEntity(universiteDTO);
        Universite createdUniversite = iUniversiteService.create(universite);
        return universiteMapper.toDto(createdUniversite);
    }

    @PostMapping("/affecterFoyer/{idFoyer}")
    public Universite affecterFoyerAUniversite(
            @PathVariable long idFoyer,
            @RequestParam String nomUniversite) {
        return iUniversiteService.affecterFoyerAUniversite(idFoyer, nomUniversite);
    }

    @PostMapping("/desaffecterFoyer/{idUniversite}")
    public Universite desaffecterFoyerAUniversite(@PathVariable long idUniversite) {
        return iUniversiteService.desaffecterFoyerAUniversite(idUniversite);
    }

    @PutMapping("/update/{id}")
    public Universite update(@PathVariable Long id, @RequestBody Universite universite) {
        return iUniversiteService.update(id, universite);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        iUniversiteService.delete(id);
    }

    @GetMapping("/getById/{id}")
    public Universite getById(@PathVariable Long id) {
        return iUniversiteService.getById(id);
    }

    @GetMapping("/getAll")
    public List<Universite> getAll() {
        return iUniversiteService.getAll();
    }
}
