package com.example.TP_Foyer.Mapper;

import com.example.TP_Foyer.DTO.EtudiantDTO;
import com.example.TP_Foyer.Entity.Etudiant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EtudiantMapper {
    EtudiantDTO toDto(Etudiant etudiant);
    Etudiant toEntity(EtudiantDTO etudiantDTO);
}