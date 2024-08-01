package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.ArticleAVendre;

import java.util.List;

public interface ArticleAVendreDao {
    // CRUD operations
    void  create(ArticleAVendre article);

    ArticleAVendre read(long id);

    void update(ArticleAVendre article);

    void delete(long id);

    List<ArticleAVendre> FIND_ALL_VENTES(Long utilisateurId);

    List<ArticleAVendre> findAll();

    List<ArticleAVendre> findByCategorieAndQuery(Long categorieId, String query);

    List<ArticleAVendre> findByQuery(String query);

    List<ArticleAVendre> findEncheresOuvertes(Long utilisateurId);

    List<ArticleAVendre> findEncheresEnCours(Long utilisateurId);

    List<ArticleAVendre> findEncheresRemportees(Long utilisateurId);

    List<ArticleAVendre> findVentesEnCours(Long utilisateurId);

    List<ArticleAVendre> findVentesNonDebutees(Long utilisateurId);

    List<ArticleAVendre> findVentesTerminees(Long utilisateurId);

    void supprimerArticlesParUtilisateur(Long utilisateurId);
}
