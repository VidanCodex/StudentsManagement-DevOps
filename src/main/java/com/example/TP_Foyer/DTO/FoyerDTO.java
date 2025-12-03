package com.example.TP_Foyer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class FoyerDTO {
    private String nomFoyer;
    private Long capaciteFoyer;

    public String getNomFoyer() {
        return nomFoyer;
    }

    public void setNomFoyer(String nomFoyer) {
        this.nomFoyer = nomFoyer;
    }

    public Long getCapaciteFoyer() {
        return capaciteFoyer;
    }

    public void setCapaciteFoyer(Long capaciteFoyer) {
        this.capaciteFoyer = capaciteFoyer;
    }
    public FoyerDTO() {
    }

    public FoyerDTO(String nomFoyer, Long capaciteFoyer) {
        this.nomFoyer = nomFoyer;
        this.capaciteFoyer = capaciteFoyer;
    }
}
