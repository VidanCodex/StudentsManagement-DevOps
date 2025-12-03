package com.example.TP_Foyer.Mapper;

import com.example.TP_Foyer.DTO.BlocDTO;
import com.example.TP_Foyer.Entity.Bloc;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlocMapper {
    BlocDTO toDto(Bloc bloc);
    Bloc toEntity(BlocDTO blocDTO);
}