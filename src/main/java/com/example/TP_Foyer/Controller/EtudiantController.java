package com.example.TP_Foyer.Controller;

import com.example.TP_Foyer.DTO.EtudiantDTO;
import com.example.TP_Foyer.Entity.Etudiant;
import com.example.TP_Foyer.Mapper.EtudiantMapper;
import com.example.TP_Foyer.Service.IEtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/etudiant")
public class EtudiantController {
    private final IEtudiantService iEtudiantService;
    private final EtudiantMapper etudiantMapper;

    public EtudiantController(IEtudiantService iEtudiantService, EtudiantMapper etudiantMapper) {
        this.iEtudiantService = iEtudiantService;
        this.etudiantMapper = etudiantMapper;
    }

    @PostMapping("/create")
    public EtudiantDTO create(@RequestBody EtudiantDTO etudiantDTO) {
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        Etudiant createdEtudiant = iEtudiantService.create(etudiant);
        return etudiantMapper.toDto(createdEtudiant);
    }

    @PutMapping("/update/{id}")
    public Etudiant update(@PathVariable Long id, @RequestBody Etudiant etudiant) {
        return iEtudiantService.update(id, etudiant);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        iEtudiantService.delete(id);
    }

    @GetMapping("/getById/{id}")
    public Etudiant getById(@PathVariable Long id) {
        return iEtudiantService.getById(id);
    }

    @GetMapping("/getAll")
    public List<Etudiant> getAll() {
        return iEtudiantService.getAll();
    }
}
