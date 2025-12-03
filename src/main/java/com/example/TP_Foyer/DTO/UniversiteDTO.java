package com.example.TP_Foyer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UniversiteDTO {
    private String nomUniversite;
    private String adresse;

    public String getNomUniversite() {
        return nomUniversite;
    }

    public void setNomUniversite(String nomUniversite) {
        this.nomUniversite = nomUniversite;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    // No-args constructor
    public UniversiteDTO() {
    }

    // All-args constructor
    public UniversiteDTO(String nomUniversite, String adresse) {
        this.nomUniversite = nomUniversite;
        this.adresse = adresse;
    }
}