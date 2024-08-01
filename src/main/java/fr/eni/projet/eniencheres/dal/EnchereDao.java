package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.Enchere;

import java.util.List;
import java.util.Optional;

public interface EnchereDao {
    void create(Enchere enchere);

    void update(Enchere enchere);

    void delete(Enchere enchere);

    Optional<Enchere> findByArticleId(long articleId);

    List<Enchere> findByUtilisateur(long utilisateurId);

    void annulerEncheresParUtilisateur(long utilisateurId);

    void supprimerEncheresParArticle(Long utilisateurId);
}
