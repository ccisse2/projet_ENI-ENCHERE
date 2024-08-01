package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import fr.eni.projet.eniencheres.bo.Enchere;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import fr.eni.projet.eniencheres.dal.EnchereDao;
import fr.eni.projet.eniencheres.dal.UtilisateursDao;
import fr.eni.projet.eniencheres.exceptions.BusinessCode;
import fr.eni.projet.eniencheres.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class EnchereServiceImpl implements EnchereService {
    private final EnchereDao enchereDao;
   private final UtilisateursDao utilisateursDao;

    @Autowired
    public EnchereServiceImpl(EnchereDao enchereDao,  UtilisateursDao utilisateursDao) {
        this.enchereDao = enchereDao;
        this.utilisateursDao = utilisateursDao;

    }

    @Override
    public void creerEnchere(Enchere enchere) {
        enchereDao.create(enchere);
    }

    @Override
    public void supprimerEnchere(Enchere enchere) {
        enchereDao.delete(enchere);
    }

    @Override
    public Optional<Enchere> rechercherEnchereParArticleId(long articleId) {
        return enchereDao.findByArticleId(articleId);
    }

    @Override
    public void modifierEnchere(Enchere enchere) {
        // Validation de l'enchere avant mis à jour
        BusinessException be = new BusinessException();
        boolean isValid = true;
        isValid &= validerEnchere(enchere, be);
        isValid &= validerArticle(enchere.getArticle(), be);
        isValid &= validerUtilisateur(enchere.getAcheteur(), be);
        isValid &= validerMontantEnchere(enchere, be);
        isValid &= validerDateEnchere(enchere, be);
        isValid &= validerCreditsUtilisateur(enchere, be);
        if (isValid) {
            // Recréditer l'ancien enchérisseur
            Optional<Enchere> enchereExistante = rechercherEnchereParArticleId(enchere.getArticle().getId());
            if (enchereExistante.isPresent()) {
                Enchere ancienneEnchere = enchereExistante.get();
                Utilisateurs ancienAcheteur = ancienneEnchere.getAcheteur();
                if (ancienAcheteur != null) {
                    ancienAcheteur.setCredit(ancienAcheteur.getCredit().add(BigDecimal.valueOf(ancienneEnchere.getMontantEnchere())));
                    utilisateursDao.update(ancienAcheteur);
                }
            }

            // Débiter le crédit du nouvel enchérisseur
            Utilisateurs acheteur = enchere.getAcheteur();
            acheteur.setCredit(acheteur.getCredit().subtract(BigDecimal.valueOf(enchere.getMontantEnchere())));
            utilisateursDao.update(acheteur);

            // Mettre à jour l'enchère
            enchereDao.update(enchere);
        } else {
            throw be;
        }
    }

    @Override
    public void supprimerEnchereParArticle(Long utilisateurId) {
        enchereDao.supprimerEncheresParArticle(utilisateurId);
    }

    private boolean validerCreditsUtilisateur(Enchere enchere, BusinessException be) {
        if (enchere.getAcheteur().getCredit().compareTo(BigDecimal.valueOf(enchere.getMontantEnchere())) < 0) {
            be.addError(BusinessCode.VALIDATION_ENCHERE_UTILISATEUR);
            return false;
        }
        return true;
    }

    private boolean validerDateEnchere(Enchere enchere, BusinessException be) {
        if (enchere.getDateEnchere().isAfter(enchere.getArticle().getDateFinEncheres())) {
            be.addError(BusinessCode.VALIDATION_ENCHERE_DATE_ENCHERE);
            return false;
        }
        return true;
    }

    private boolean validerMontantEnchere(Enchere enchere, BusinessException be) {
        Optional<Enchere> optionalEnchere = rechercherEnchereParArticleId(enchere.getArticle().getId());

        // Vérifie si le montant de l'enchère est positif
        if (enchere.getMontantEnchere() <= 0) {
            be.addError(BusinessCode.VALIDATION_ENCHERE_MONTANT);
            return false;
        }

        // Vérifie si une enchère existe déjà pour cet article et compare les montants
        if (optionalEnchere.isPresent()) {
            Enchere enchereExistante = optionalEnchere.get();
            if (enchere.getMontantEnchere() <= enchereExistante.getMontantEnchere()) {
                be.addError(BusinessCode.VALIDATION_ENCHERE_MONTANT);
                return false;
            }
        }

        return true;
    }

    private boolean validerUtilisateur(Utilisateurs acheteur, BusinessException be) {
        if (acheteur == null) {
            be.addError(BusinessCode.VALIDATION_ENCHERE_UTILISATEUR_INVALIDE);
            return false;
        }
        return true;
    }

    private boolean validerEnchere(Enchere enchere, BusinessException be) {
        if (enchere == null) {
            be.addError(BusinessCode.VALIDATION_ENCHERE_INVALIDE);
            return false;
        }
        return true;
    }

    private boolean validerArticle(ArticleAVendre article, BusinessException be) {
        if (article == null) {
            be.addError(BusinessCode.VALIDATION_ARTICLE_INVALIDE);
            return false;
        }
        return true;
    }
}
