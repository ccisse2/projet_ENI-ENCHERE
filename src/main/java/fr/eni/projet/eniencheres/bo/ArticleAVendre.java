package fr.eni.projet.eniencheres.bo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ARTICLES_A_VENDRE")
public class ArticleAVendre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no_article")
    private Long id;

    @Column(nullable = false, length = 30)
    private String nomArticle;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateDebutEncheres;

    @Column(nullable = false)
    private LocalDateTime dateFinEncheres;

    @Column(nullable = false)
    private int statutEnchere;

    @Column(nullable = false)
    private int prixInitial;

    @Column(nullable = false)
    private int prixVente;

    @Column(name = "photo", nullable = true)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateurs vendeur;

    @ManyToOne
    @JoinColumn(name = "no_adresse_retrait", nullable = false)
    private Adresse adresseRetrait;

    @ManyToOne
    @JoinColumn(name = "no_categorie", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Enchere> encheres;

    public ArticleAVendre() {
    }

    public ArticleAVendre(Long id, String nomArticle, String description, LocalDateTime dateDebutEncheres,
                          LocalDateTime dateFinEncheres, int statutEnchere, int prixInitial, int prixVente,
                          String imagePath, Utilisateurs vendeur, Adresse adresseRetrait, Categorie categorie,
                          List<Enchere> encheres) {
        this.id = id;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.statutEnchere = statutEnchere;
        this.prixInitial = prixInitial;
        this.prixVente = prixVente;
        this.imagePath = imagePath;
        this.vendeur = vendeur;
        this.adresseRetrait = adresseRetrait;
        this.categorie = categorie;
        this.encheres = encheres;
    }

    public ArticleAVendre(String nomArticle, String description, LocalDateTime dateDebutEncheres,
                          LocalDateTime dateFinEncheres, int statutEnchere, int prixInitial, int prixVente,
                          String imagePath, Utilisateurs vendeur, Adresse adresseRetrait, Categorie categorie,
                          List<Enchere> encheres) {
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.statutEnchere = statutEnchere;
        this.prixInitial = prixInitial;
        this.prixVente = prixVente;
        this.imagePath = imagePath;
        this.vendeur = vendeur;
        this.adresseRetrait = adresseRetrait;
        this.categorie = categorie;
        this.encheres = encheres;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArticleAVendre{");
        sb.append("id=").append(id);
        sb.append(", nomArticle='").append(nomArticle).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", dateDebutEncheres=").append(dateDebutEncheres);
        sb.append(", dateFinEncheres=").append(dateFinEncheres);
        sb.append(", statutEnchere=").append(statutEnchere);
        sb.append(", prixInitial=").append(prixInitial);
        sb.append(", prixVente=").append(prixVente);
        sb.append(", imagePath='").append(imagePath).append('\'');
        sb.append(", vendeur=").append(vendeur);
        sb.append(", adresseRetrait=").append(adresseRetrait);
        sb.append(", categorie=").append(categorie);
        sb.append(", encheres=").append(encheres);
        sb.append('}');
        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateDebutEncheres() {
        return dateDebutEncheres;
    }

    public void setDateDebutEncheres(LocalDateTime dateDebutEncheres) {
        this.dateDebutEncheres = dateDebutEncheres;
    }

    public LocalDateTime getDateFinEncheres() {
        return dateFinEncheres;
    }

    public void setDateFinEncheres(LocalDateTime dateFinEncheres) {
        this.dateFinEncheres = dateFinEncheres;
    }

    public int getStatutEnchere() {
        return statutEnchere;
    }

    public void setStatutEnchere(int statut) {
        this.statutEnchere = statut;
    }

    public int getPrixInitial() {
        return prixInitial;
    }

    public void setPrixInitial(int prixInitial) {
        this.prixInitial = prixInitial;
    }

    public int getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(int prixVente) {
        this.prixVente = prixVente;
    }

    public Utilisateurs getVendeur() {
        return vendeur;
    }

    public void setVendeur(Utilisateurs vendeur) {
        this.vendeur = vendeur;
    }

    public Adresse getAdresseRetrait() {
        return adresseRetrait;
    }

    public void setAdresseRetrait(Adresse adresseRetrait) {
        this.adresseRetrait = adresseRetrait;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public List<Enchere> getEncheres() {
        return encheres;
    }

    public void setEncheres(List<Enchere> encheres) {
        this.encheres = encheres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleAVendre that = (ArticleAVendre) o;
        return statutEnchere == that.statutEnchere && prixInitial == that.prixInitial && prixVente == that.prixVente && Objects.equals(id, that.id) && Objects.equals(nomArticle, that.nomArticle) && Objects.equals(description, that.description) && Objects.equals(dateDebutEncheres, that.dateDebutEncheres) && Objects.equals(dateFinEncheres, that.dateFinEncheres) && Objects.equals(imagePath, that.imagePath) && Objects.equals(vendeur, that.vendeur) && Objects.equals(adresseRetrait, that.adresseRetrait) && Objects.equals(categorie, that.categorie) && Objects.equals(encheres, that.encheres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomArticle, description, dateDebutEncheres, dateFinEncheres, statutEnchere, prixInitial, prixVente, imagePath, vendeur, adresseRetrait, categorie, encheres);
    }
}
