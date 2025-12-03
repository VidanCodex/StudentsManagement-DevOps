package com.example.TP_Foyer.Mapper;

import com.example.TP_Foyer.DTO.ReservationDTO;
import com.example.TP_Foyer.Entity.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    Reservation toEntity(ReservationDTO dto);

    ReservationDTO toDto(Reservation reservation);
}