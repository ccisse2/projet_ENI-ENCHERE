package fr.eni.projet.eniencheres.bo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "UTILISATEURS_TEMPORAIRE")
public class UtilisateurTemporaire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @Size(min = 5, max = 20, message = "{utilisateur.pseudo.size}")
    private String pseudo;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "{utilisateur.nom.notblank}")
    private String nom;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "{utilisateur.prenom.notblank}")
    private String prenom;

    @Column(nullable = false, length = 50)
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "{utilisateur.email.pattern}")
    private String email;

    @Column(nullable = false, length = 30)
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "{utilisateur.motDePasse.pattern}"
    )
    private String motDePasse;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "{utilisateur.adresse.rue.notblank}")
    private String rue;

    @Column(nullable = false, length = 10)
    @NotBlank(message = "{utilisateur.adresse.codePostal.notblank}")
    private String codePostal;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "{utilisateur.adresse.ville.notblank}")
    private String ville;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private String tokenVerification;

    @Column(nullable = false)
    private LocalDateTime tokenExpirationDate;
    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getTokenVerification() {
        return tokenVerification;
    }

    public void setTokenVerification(String tokenVerification) {
        this.tokenVerification = tokenVerification;
    }

    public LocalDateTime getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(LocalDateTime tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }
}
