package com.example.TP_Foyer.Controller;

import com.example.TP_Foyer.DTO.ChambreDTO;
import com.example.TP_Foyer.Entity.Chambre;
import com.example.TP_Foyer.Mapper.ChambreMapper;
import com.example.TP_Foyer.Service.IChambreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import java.util.List;

@RestController
@RequestMapping("/chambre")
public class ChambreController {
    private final IChambreService iChambreService;
    private final ChambreMapper chambreMapper;

    public ChambreController(IChambreService iChambreService, ChambreMapper chambreMapper) {
        this.iChambreService = iChambreService;
        this.chambreMapper = chambreMapper;
    }

    @PostMapping("/create")
    public ChambreDTO create(@RequestBody ChambreDTO chambreDTO) {
        Chambre chambre = chambreMapper.toEntity(chambreDTO);
        Chambre createdChambre = iChambreService.create(chambre);
        return chambreMapper.toDto(createdChambre);
    }

    @PostMapping("/{idChambre}/affecterBloc/{idBloc}")
    public Chambre affecterChambreABloc(
            @PathVariable Long idChambre,
            @PathVariable Long idBloc) {
        return iChambreService.affecterChambreABloc(idChambre, idBloc);
    }

    @PutMapping("/update/{id}")
    public Chambre update(@PathVariable Long id, @RequestBody Chambre chambre) {
        return iChambreService.update(id, chambre);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        iChambreService.delete(id);
    }

    @GetMapping("/getById/{id}")
    public Chambre getById(@PathVariable Long id) {
        return iChambreService.getById(id);
    }

    @GetMapping("/getAll")
    public List<Chambre> getAll() {
        return iChambreService.getAll();
    }

    @GetMapping("/parUniversite")
    public List<Chambre> getChambresParNomUniversite(@RequestParam String nomUniversite) {
        return iChambreService.getChambresParNomUniversite(nomUniversite);
    }

    @GetMapping("/parBlocEtType")
    public List<Chambre> getChambresParBlocEtType(
            @RequestParam long idBloc,
            @RequestParam TypeChambre typeC) {
        return iChambreService.getChambresParBlocEtType(idBloc, typeC);
    }

    @GetMapping("/nonReservees")
    public List<Chambre> getChambresNonReserveParNomUniversiteEtTypeChambre(
            @RequestParam String nomUniversite,
            @RequestParam TypeChambre type) {
        return iChambreService.getChambresNonReserveParNomUniversiteEtTypeChambre(nomUniversite, type);
    }
}
