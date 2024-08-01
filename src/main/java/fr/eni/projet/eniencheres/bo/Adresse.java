package fr.eni.projet.eniencheres.bo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ADRESSES")
public class Adresse implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no_adresse")
    private Long id;

    @Column(nullable = false, length = 100)
    private String rue;

    @Column(nullable = false, length = 10)
    private String codePostal;

    @Column(nullable = false, length = 50)
    private String ville;

    @Column(nullable = false)
    private boolean adresseEni;
    public Adresse() {
    }

    public Adresse(String rue, String codePostal, String ville) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public Adresse(long id, String rue, String codePostal, String ville, boolean adresseEni) {
        this.id = id;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.adresseEni = adresseEni;
    }

    public Adresse(long id, String rue, String codePostal, String ville) {
        this.id = id;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Adresse{");
        sb.append("id=").append(id);
        sb.append(", rue='").append(rue).append('\'');
        sb.append(", codePostal='").append(codePostal).append('\'');
        sb.append(", ville='").append(ville).append('\'');
        sb.append(", adresseEni=").append(adresseEni);
        sb.append('}');
        return sb.toString();
    }

    public boolean isAdresseEni() {
        return adresseEni;
    }

    public void setAdresseEni(boolean adresseEni) {
        this.adresseEni = adresseEni;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adresse adresse = (Adresse) o;
        return id == adresse.id && Objects.equals(rue, adresse.rue) && Objects.equals(codePostal, adresse.codePostal) && Objects.equals(ville, adresse.ville);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rue, codePostal, ville);
    }

}
