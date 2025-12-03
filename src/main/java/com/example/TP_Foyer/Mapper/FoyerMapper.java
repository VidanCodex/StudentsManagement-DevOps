package com.example.TP_Foyer.Mapper;

import com.example.TP_Foyer.DTO.FoyerDTO;
import com.example.TP_Foyer.Entity.Foyer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoyerMapper {
    FoyerDTO toDto(Foyer foyer);
    Foyer toEntity(FoyerDTO foyerDTO);
}
