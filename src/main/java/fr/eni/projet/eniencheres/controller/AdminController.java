package fr.eni.projet.eniencheres.controller;

import fr.eni.projet.eniencheres.bll.ArticleAVendreService;
import fr.eni.projet.eniencheres.bll.UtilisateursService;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UtilisateursService utilisateursService;
    private final ArticleAVendreService articleAVendreService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UtilisateursService utilisateursService, ArticleAVendreService articleAVendreService) {
        this.utilisateursService = utilisateursService;
        this.articleAVendreService = articleAVendreService;
    }

    @GetMapping("/utilisateurs")
    public String afficherListeUtilisateurs(Model model) {
        List<Utilisateurs> utilisateurs = utilisateursService.recupererTousLesUtilisateurs();
        model.addAttribute("utilisateurs", utilisateurs);
        return "view-gestion-utilisateur";
    }

    @PostMapping("/utilisateur/{id}/anonymiser")
    public String anonymiserUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            articleAVendreService.mettreAJourRessourcesPourUtilisateur(id);
            utilisateursService.anonymiserUtilisateur(id);
            redirectAttributes.addFlashAttribute("success", "Compte utilisateur anonymisé avec succès.");
        } catch (Exception e) {
            logger.error("Erreur lors de l'anonymisation du compte utilisateur.", e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'anonymisation du compte utilisateur.");
        }
        return "redirect:/admin/utilisateurs";
    }

    @PostMapping("/utilisateur/{pseudo}/supprimer")
    public String supprimerUtilisateur(@PathVariable String pseudo, RedirectAttributes redirectAttributes) {
        try {
            Utilisateurs utilisateur = utilisateursService.recupererUtilisateurParPseudo(pseudo);
            if (utilisateur != null) {
                utilisateursService.supprimerUtilisateur(utilisateur.getId());
                redirectAttributes.addFlashAttribute("success", "Compte utilisateur supprimé avec succès.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Utilisateur introuvable.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du compte utilisateur : " + e.getMessage());
        }
        return "redirect:/admin/utilisateurs";
    }


    @PostMapping("/utilisateur/{id}/desactiver")
    public String desactiverUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            utilisateursService.desactiverUtilisateur(id);
            redirectAttributes.addFlashAttribute("success", "Compte utilisateur désactivé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la désactivation du compte utilisateur.");
        }
        return "redirect:/admin/utilisateurs";
    }

}
