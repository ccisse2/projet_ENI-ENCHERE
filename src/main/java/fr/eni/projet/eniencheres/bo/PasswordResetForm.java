package fr.eni.projet.eniencheres.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public class PasswordResetForm {

    @NotBlank(message = "{password.notBlank}")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
        message = "{utilisateur.motDePasse.pattern}"
    )
    private String nouveauMotDePasse;

    @NotBlank(message = "{password.notBlank}")
    private String confirmerMotDePasse;

    // Getters et setters
    
    public String getNouveauMotDePasse() {
        return nouveauMotDePasse;
    }

    public void setNouveauMotDePasse(String nouveauMotDePasse) {
        this.nouveauMotDePasse = nouveauMotDePasse;
    }

    public String getConfirmerMotDePasse() {
        return confirmerMotDePasse;
    }

    public void setConfirmerMotDePasse(String confirmerMotDePasse) {
        this.confirmerMotDePasse = confirmerMotDePasse;
    }
}
