package fr.eni.projet.eniencheres.bo;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EnchereId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_utilisateur")
    private Long idUtilisateur;

    @Column(name = "no_article")
    private Long noArticle;

    public EnchereId() {
    }

    public EnchereId(Long idUtilisateur, Long noArticle) {
        this.idUtilisateur = idUtilisateur;
        this.noArticle = noArticle;
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Long getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(Long noArticle) {
        this.noArticle = noArticle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnchereId enchereId = (EnchereId) o;
        return Objects.equals(idUtilisateur, enchereId.idUtilisateur) && Objects.equals(noArticle, enchereId.noArticle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUtilisateur, noArticle);
    }
}

