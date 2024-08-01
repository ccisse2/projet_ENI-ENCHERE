package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import fr.eni.projet.eniencheres.bo.Utilisateurs;

import java.util.List;

public interface ArticleAVendreService {
    void creerArticleAVendre(ArticleAVendre articleAVendre);

    ArticleAVendre getArticleAVendreById(Long id);

    void modifierArticleAVendre(ArticleAVendre articleAVendre);

    void supprimerArticleAVendre(Long id);

    List<ArticleAVendre> toutLesArticlesAVendre();

    Utilisateurs trouverVendeurParId(Long id);

    List<ArticleAVendre> searchByCategorieAndQuery(Long categorieId, String query);

    List<ArticleAVendre> searchByQuery(String query);

    List<ArticleAVendre> chercherEncheresOuvertes(Long utilisateurId);

    List<ArticleAVendre> chercherEncheresEnCours(Long utilisateurId);

    List<ArticleAVendre> chercherEncheresRemportees(Long utilisateurId);

    List<ArticleAVendre> chercherVentesEnCours(Long utilisateurId);

    List<ArticleAVendre> chercherVentesNonDebutees(Long utilisateurId);

    List<ArticleAVendre> chercherVentesTerminees(Long utilisateurId);

    void updateEncheresStatus();

    void mettreAJourRessourcesPourUtilisateur(Long utilisateurId);
}
