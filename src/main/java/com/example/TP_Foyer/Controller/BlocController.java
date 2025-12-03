package com.example.TP_Foyer.Controller;

import com.example.TP_Foyer.DTO.BlocDTO;
import com.example.TP_Foyer.Entity.Bloc;
import com.example.TP_Foyer.Mapper.BlocMapper;
import com.example.TP_Foyer.Service.IBlocService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bloc")
public class BlocController {
    private final IBlocService iBlocService;
    private final BlocMapper blocMapper;

    public BlocController(IBlocService iBlocService, BlocMapper blocMapper) {
        this.iBlocService = iBlocService;
        this.blocMapper = blocMapper;
    }

    @PostMapping("/create")
    public BlocDTO create(@RequestBody BlocDTO blocDTO) {
        Bloc bloc = blocMapper.toEntity(blocDTO);
        Bloc createdBloc = iBlocService.create(bloc);
        return blocMapper.toDto(createdBloc);
    }

    @PostMapping("/{idBloc}/affecterFoyer/{idFoyer}")
    public Bloc affecterBlocAFoyer(
            @PathVariable Long idBloc,
            @PathVariable Long idFoyer) {
        return iBlocService.affecterBlocAFoyer(idBloc, idFoyer);
    }

    @PostMapping("/{idBloc}/affecterChambres")
    public Bloc affecterChambresABloc(
            @PathVariable long idBloc,
            @RequestBody List<Long> numChambre) {
        return iBlocService.affecterChambresABloc(numChambre, idBloc);
    }

    @PutMapping("/update/{id}")
    public Bloc update(@PathVariable Long id, @RequestBody Bloc bloc) {

        return iBlocService.update(id, bloc);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        iBlocService.delete(id);
    }

    @GetMapping("/getById/{id}")
    public Bloc getById(@PathVariable Long id) {
        return iBlocService.getById(id);
    }

    @GetMapping("/getAll")
    public List<Bloc> getAll() {
        return iBlocService.getAll();
    }
}
