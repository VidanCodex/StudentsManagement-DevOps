package com.example.TP_Foyer.DTO;

import com.example.TP_Foyer.Utils.Enum.TypeChambre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ChambreDTO {
    private Long numeroChambre;
    private Long capacite;

    public Long getNumeroChambre() {
        return numeroChambre;
    }

    public void setNumeroChambre(Long numeroChambre) {
        this.numeroChambre = numeroChambre;
    }

    public Long getCapacite() {
        return capacite;
    }

    public void setCapacite(Long capacite) {
        this.capacite = capacite;
    }

    public TypeChambre getTypeC() {
        return typeC;
    }

    public void setTypeC(TypeChambre typeC) {
        this.typeC = typeC;
    }
    public ChambreDTO() {
    }

    public ChambreDTO(Long numeroChambre,long capacite, TypeChambre typeC) {
        this.numeroChambre = numeroChambre;
        this.capacite = capacite;
        this.typeC = typeC;
    }

    private TypeChambre typeC;
}
