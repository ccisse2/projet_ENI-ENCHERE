package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import fr.eni.projet.eniencheres.dal.ArticleAVendreDao;
import fr.eni.projet.eniencheres.dal.UtilisateursDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleAVendreServiceImpl implements ArticleAVendreService{

    private final ArticleAVendreDao articleDao;
    private final UtilisateursDao utilisateurDao;

    @Autowired
    public ArticleAVendreServiceImpl(ArticleAVendreDao articleDao, UtilisateursDao utilisateurDao) {
        this.articleDao = articleDao;
        this.utilisateurDao = utilisateurDao;
    }

    @Override
    public void creerArticleAVendre(ArticleAVendre articleAVendre) {
        articleDao.create(articleAVendre);
    }

    @Override
    public ArticleAVendre getArticleAVendreById(Long id) {
        return articleDao.read(id);
    }

    @Override
    public void modifierArticleAVendre(ArticleAVendre articleAVendre) {
        articleDao.update(articleAVendre);
    }

    @Override
    public void supprimerArticleAVendre(Long id) {
        articleDao.delete(id);
    }

    @Override
    public List<ArticleAVendre> toutLesArticlesAVendre() {
        return articleDao.findAll();
    }

    @Override
    public Utilisateurs trouverVendeurParId(Long id) {
        return utilisateurDao.readById(id);
    }

    @Override
    public List<ArticleAVendre> searchByCategorieAndQuery(Long categorieId, String query) {
        return articleDao.findByCategorieAndQuery(categorieId, query);
    }

    @Override
    public List<ArticleAVendre> searchByQuery(String query) {
        return articleDao.findByQuery(query);
    }

    @Override
    public List<ArticleAVendre> chercherEncheresOuvertes(Long utilisateurId) {
        return articleDao.findEncheresOuvertes(utilisateurId);
    }

    @Override
    public List<ArticleAVendre> chercherEncheresEnCours(Long utilisateurId) {
        return articleDao.findEncheresEnCours(utilisateurId);
    }

    @Override
    public List<ArticleAVendre> chercherEncheresRemportees(Long utilisateurId) {
        return articleDao.findEncheresRemportees(utilisateurId);
    }

    @Override
    public List<ArticleAVendre> chercherVentesEnCours(Long utilisateurId) {
        return articleDao.findVentesEnCours(utilisateurId);
    }

    @Override
    public List<ArticleAVendre> chercherVentesNonDebutees(Long utilisateurId) {
        return articleDao.findVentesNonDebutees(utilisateurId);
    }

    @Override
    public List<ArticleAVendre> chercherVentesTerminees(Long utilisateurId) {
        return articleDao.findVentesTerminees(utilisateurId);
    }

    @Override
    public void mettreAJourRessourcesPourUtilisateur(Long utilisateurId) {
        List<ArticleAVendre> articles = articleDao.FIND_ALL_VENTES(utilisateurId);
        for (ArticleAVendre article : articles) {
            // Logique pour mettre à jour ou terminer les enchères associées à cet utilisateur
            article.setStatutEnchere(3); // Par exemple, marquer comme "livré" ou "terminé"
            articleDao.update(article);
        }
    }

}
