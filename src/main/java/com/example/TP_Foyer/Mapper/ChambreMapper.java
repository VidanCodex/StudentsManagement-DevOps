package com.example.TP_Foyer.Mapper;

import com.example.TP_Foyer.DTO.ChambreDTO;
import com.example.TP_Foyer.Entity.Chambre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChambreMapper {
    ChambreDTO toDto(Chambre chambre);
    Chambre toEntity(ChambreDTO chambreDTO);
}
