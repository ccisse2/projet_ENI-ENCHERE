package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import fr.eni.projet.eniencheres.bo.Enchere;

import java.util.Optional;

public interface EnchereService {
    void creerEnchere(Enchere enchere);
    void supprimerEnchere(Enchere enchere);
    Optional<Enchere> rechercherEnchereParArticleId(long articleId);
    void modifierEnchere(Enchere enchere);

    void supprimerEnchereParArticle(Long utilisateurId);
}
