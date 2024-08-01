package fr.eni.projet.eniencheres.bo;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "ENCHERES")
public class Enchere implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private EnchereId id;

    @Column(nullable = false)
    private LocalDateTime dateEnchere;

    @Column(nullable = false)
    private int montantEnchere;

    @MapsId("idUtilisateur")
    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateurs acheteur;

    @MapsId("noArticle")
    @ManyToOne
    @JoinColumn(name = "no_article", nullable = false)
    private ArticleAVendre article;
    public Enchere() {
    }

    public Enchere(LocalDateTime dateEnchere, int montantEnchere, Utilisateurs acheteur, ArticleAVendre article) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.acheteur = acheteur;
        this.article = article;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Enchere{");
        sb.append("dateEnchere=").append(dateEnchere);
        sb.append(", montantEnchere=").append(montantEnchere);
        sb.append(", acheteur=").append(acheteur);
        sb.append(", article=").append(article);
        sb.append('}');
        return sb.toString();
    }

    public LocalDateTime getDateEnchere() {
        return dateEnchere;
    }

    public void setDateEnchere(LocalDateTime dateEnchere) {
        this.dateEnchere = dateEnchere;
    }

    public EnchereId getId() {
        return id;
    }

    public int getMontantEnchere() {
        return montantEnchere;
    }

    public void setMontantEnchere(int montantEnchere) {
        this.montantEnchere = montantEnchere;
    }

    public Utilisateurs getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(Utilisateurs acheteur) {
        this.acheteur = acheteur;
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
        Enchere enchere = (Enchere) o;
        return montantEnchere == enchere.montantEnchere && Objects.equals(dateEnchere, enchere.dateEnchere) && Objects.equals(acheteur, enchere.acheteur) && Objects.equals(article, enchere.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateEnchere, montantEnchere, acheteur, article);
    }
}
