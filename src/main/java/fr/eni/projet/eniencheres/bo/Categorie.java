package fr.eni.projet.eniencheres.bo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "CATEGORIES")
public class Categorie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no_categorie")
    private Long id;

    @Column(nullable = false, length = 30)
    private String libelle;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<ArticleAVendre> articles;

    public Categorie() {
    }
    public Categorie(long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Categorie(long id, String libelle, List<ArticleAVendre> articles) {
        this.id = id;
        this.libelle = libelle;
        this.articles = articles;
    }

    @Override
    public String
    toString() {
        final StringBuilder sb = new StringBuilder("Categorie{");
        sb.append("id=").append(id);
        sb.append(", libelle='").append(libelle).append('\'');
        sb.append(", articles=").append(articles);
        sb.append('}');
        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<ArticleAVendre> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleAVendre> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categorie categorie = (Categorie) o;
        return id == categorie.id && Objects.equals(libelle, categorie.libelle) && Objects.equals(articles, categorie.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, articles);
    }
}
