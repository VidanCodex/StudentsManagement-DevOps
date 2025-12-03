package com.example.TP_Foyer.Mapper;

import com.example.TP_Foyer.DTO.UniversiteDTO;
import com.example.TP_Foyer.Entity.Universite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniversiteMapper {
    UniversiteDTO toDto(Universite universite);
    Universite toEntity(UniversiteDTO universiteDTO);
}