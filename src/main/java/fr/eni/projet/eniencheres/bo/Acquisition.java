package fr.eni.projet.eniencheres.bo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ACQUISITIONS")
public class Acquisition implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acquisition")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateurs utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_article", nullable = false)
    private ArticleAVendre article;

    public Acquisition() {
    }

    public Acquisition(Utilisateurs utilisateur, ArticleAVendre article) {
        this.utilisateur = utilisateur;
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

    public ArticleAVendre getArticle() {
        return article;
    }

    public void setArticle(ArticleAVendre article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acquisition that = (Acquisition) o;
        return Objects.equals(id, that.id) && Objects.equals(utilisateur, that.utilisateur) && Objects.equals(article, that.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, utilisateur, article);
    }
}
