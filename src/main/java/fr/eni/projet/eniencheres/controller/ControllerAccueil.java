package fr.eni.projet.eniencheres.controller;

import fr.eni.projet.eniencheres.bll.AcquisitionServiceImpl;
import fr.eni.projet.eniencheres.bll.ArticleAVendreService;
import fr.eni.projet.eniencheres.bll.CategorieService;
import fr.eni.projet.eniencheres.bll.UtilisateursService;
import fr.eni.projet.eniencheres.bo.Acquisition;
import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import fr.eni.projet.eniencheres.bo.Categorie;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes({"articlesEnSession", "membreEnSession", "categorieEnSession"})
public class ControllerAccueil {

    private final ArticleAVendreService articlesService;
    private final CategorieService categorieService;
    private final AcquisitionServiceImpl acquisitionServiceImpl;
    private final UtilisateursService utilisateursService;

    @Autowired
    public ControllerAccueil(ArticleAVendreService articlesService, CategorieService categorieService, AcquisitionServiceImpl acquisitionServiceImpl, UtilisateursService utilisateursService) {
        this.articlesService = articlesService;
        this.categorieService = categorieService;
        this.acquisitionServiceImpl = acquisitionServiceImpl;
        this.utilisateursService = utilisateursService;
    }

    @ModelAttribute("articlesEnSession")
    public List<ArticleAVendre> chargerArticlesEnSession() {
        List<ArticleAVendre> articles = articlesService.toutLesArticlesAVendre();
        for (ArticleAVendre article : articles) {
            // Ajouter les informations du vendeur à chaque article
            article.setVendeur(articlesService.trouverVendeurParId(article.getVendeur().getId()));
            System.out.println("afficher les photos" + article.getImagePath());
        }

        return articles;
    }

    @ModelAttribute("categorieEnSession")
    public List<Categorie> chargerCategoriesEnSession() {
        return categorieService.recupererToutesLesCategories();
    }

    @ModelAttribute("membreEnSession")
    public Utilisateurs chargerMembreEnSession(Authentication authentication) {
        if (authentication != null) {
            String pseudo = authentication.getName();
            return utilisateursService.charger(pseudo);
        }
        return null;
    }

    @GetMapping
    public String afficherEncheresEnCours(Model model) {
        model.addAttribute("articlesEnSession", chargerArticlesEnSession());
        model.addAttribute("categorieEnSession", chargerCategoriesEnSession());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                         @RequestParam(value = "categorieId", required = false) Long categorieId,
                         @RequestParam(value = "typeRecherche", required = false) String typeRecherche,
                         @RequestParam(value = "typeAchat", required = false) String typeAchat,
                         @RequestParam(value = "typeVente", required = false) String typeVente,
                         @ModelAttribute("membreEnSession") Utilisateurs utilisateur,
                         Model model) {
        List<ArticleAVendre> articles;
        if (categorieId != null && categorieId > 0) {
            articles = articlesService.searchByCategorieAndQuery(categorieId, query);
        } else {
            articles = articlesService.searchByQuery(query);
        }

        if ("achats".equals(typeRecherche)) {
            if ("ouvertes".equals(typeAchat)) {
                articles = articlesService.chercherEncheresOuvertes(utilisateur.getId());
            } else if ("enCours".equals(typeAchat)) {
                articles = articlesService.chercherEncheresEnCours(utilisateur.getId());
            } else if ("remportees".equals(typeAchat)) {
                articles = articlesService.chercherEncheresRemportees(utilisateur.getId());
            }
        } else if ("ventes".equals(typeRecherche)) {
            if ("enCours".equals(typeVente)) {
                articles = articlesService.chercherVentesEnCours(utilisateur.getId());
            } else if ("nonDebutees".equals(typeVente)) {
                articles = articlesService.chercherVentesNonDebutees(utilisateur.getId());
            } else if ("terminees".equals(typeVente)) {
                articles = articlesService.chercherVentesTerminees(utilisateur.getId());
            }
        }

        model.addAttribute("articlesEnSession", articles);
        return "index";
    }

    @GetMapping("/mes-achats")
    public String afficherAchatsUtilisateur(Model model, Authentication authentication) {
        Utilisateurs utilisateur = utilisateursService.charger(authentication.getName());
        List<Acquisition> acquisitions = acquisitionServiceImpl.trouverAcquisitionsParUtilisateur(utilisateur);
        model.addAttribute("acquisitions", acquisitions);
        return "view-mes-achats";
    }
}


