package com.example.TP_Foyer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class BlocDTO {
    private String nomBloc;
    private Long capaciteBloc;

    public String getNomBloc() {
        return nomBloc;
    }

    public void setNomBloc(String nomBloc) {
        this.nomBloc = nomBloc;
    }

    public Long getCapaciteBloc() {
        return capaciteBloc;
    }

    public void setCapaciteBloc(Long capaciteBloc) {
        this.capaciteBloc = capaciteBloc;
    }
    public BlocDTO() {
    }

    public BlocDTO(String nomBloc, Long capaciteBloc) {
        this.nomBloc = nomBloc;
        this.capaciteBloc = capaciteBloc;
    }
}
