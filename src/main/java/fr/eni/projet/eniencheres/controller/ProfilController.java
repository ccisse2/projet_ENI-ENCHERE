package fr.eni.projet.eniencheres.controller;

import fr.eni.projet.eniencheres.bll.AdresseService;
import fr.eni.projet.eniencheres.bll.UtilisateursService;
import fr.eni.projet.eniencheres.bo.Adresse;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profil")
@SessionAttributes("utilisateur")
public class ProfilController {

    private final UtilisateursService utilisateursService;
    private final AdresseService adresseService;

    @Autowired
    public ProfilController(UtilisateursService utilisateursService, AdresseService adresseService) {
        this.utilisateursService = utilisateursService;
        this.adresseService = adresseService;
    }

    @ModelAttribute("utilisateur")
    public Utilisateurs membreEnSession() {
        return new Utilisateurs();
    }


    @GetMapping
    public String afficherProfil(HttpSession session, Model model, @ModelAttribute("utilisateur")
    Utilisateurs utilisateur) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = auth.getName();

        Utilisateurs aCharger = utilisateursService.charger(pseudo);
        Adresse adresse = adresseService.chercherAdresseParUtilisateur(aCharger.getId());
        aCharger.setAdresse(adresse);
        if (aCharger != null) {
           utilisateur.setId(aCharger.getId());
           utilisateur.setNom(aCharger.getNom());
           utilisateur.setPrenom(aCharger.getPrenom());
           utilisateur.setPseudo(aCharger.getPseudo());
           utilisateur.setAdmin(aCharger.isAdmin());
           utilisateur.setEmail(aCharger.getEmail());
           utilisateur.setTelephone(aCharger.getTelephone());
           utilisateur.setCredit(aCharger.getCredit());
           utilisateur.setAdresse(aCharger.getAdresse());
           utilisateur.setArticlesVendus(aCharger.getArticlesVendus());
           utilisateur.setEncheres(aCharger.getEncheres());

            session.setAttribute("membreEnSession", utilisateur);
        }
        model.addAttribute("membreEnSession", utilisateur);
        model.addAttribute("action", "/profil");
        return "view-profil";
    }

    @PostMapping
    public String modifierProfil(@ModelAttribute("utilisateur") Utilisateurs utilisateur, RedirectAttributes redirectAttributes) {
        utilisateursService.mettreAJourUtilisateur(utilisateur);
        redirectAttributes.addFlashAttribute("success", "Votre profil a été modifié avec succès.");
        return "redirect:/profil";
    }

    @GetMapping("/change-password")
    public String afficherChangerMotDePasseForm(Model model) {

        return "view-change-password";
    }

    @PostMapping("/change-password")
    public String modifierMotDePasse(@RequestParam String ancienMotDePasse, @RequestParam String nouveauMotDePasse,
                                     @RequestParam String confirmerMotDePasse, Authentication authentication, Model model,
                                     RedirectAttributes redirectAttributes) {
        String pseudo = authentication.getName();
        Utilisateurs utilisateur = utilisateursService.charger(pseudo);

        if (!utilisateursService.verifierMotDePasse(ancienMotDePasse, utilisateur.getMotDePasse())) {
            model.addAttribute("error", "L'ancien mot de passe est incorrect.");
            return "view-change-password";
        }

        if (!nouveauMotDePasse.equals(confirmerMotDePasse)) {
            model.addAttribute("error", "Les nouveaux mots de passe ne correspondent pas.");
            return "view-change-password";
        }

        utilisateursService.mettreAJourMotDePasse(pseudo, nouveauMotDePasse);
        redirectAttributes.addFlashAttribute("success", "Votre mot de passe a été modifié avec succès.");
        return "redirect:/profil";
    }

    @GetMapping("/supprimer")
    public String afficherConfirmationSuppression() {
        return "view-confirmation-suppression";
    }


    @PostMapping("/supprimer")
    public String supprimerCompte(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        String pseudo = authentication.getName();
        utilisateursService.supprimerUtilisateur(pseudo);

        // Déconnexion de l'utilisateur
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/";
    }

    @GetMapping("/reset-password")
    public String afficherFormulaireDemandeResetMotDePasse() {
        return "view-reset-password-request";
    }

    @PostMapping("/reset-password")
    public String demanderResetMotDePasse(@RequestParam String email, Model model) {
        try {
            utilisateursService.créerTokenDeRéinitialisation(email);
            model.addAttribute("message", "Un lien de réinitialisation de mot de passe a été envoyé à votre adresse email.");
        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", "Aucun utilisateur trouvé avec cet email.");
        }
        return "view-reset-password-request";
    }

    @GetMapping("/reset-password/{token}")
    public String afficherFormulaireNouveauMotDePasse(@PathVariable String token, Model model) {
        if (utilisateursService.vérifierToken(token)) {
            model.addAttribute("token", token);
            return "view-new-password";
        } else {
            model.addAttribute("error", "Le lien de réinitialisation est invalide ou a expiré.");
            return "view-reset-password-request";
        }
    }

    @PostMapping("/reset-password/{token}")
    public String réinitialiserMotDePasse(@PathVariable String token, @RequestParam String nouveauMotDePasse,
                                          @RequestParam String confirmerMotDePasse, Model model) {
        if (!nouveauMotDePasse.equals(confirmerMotDePasse)) {
            model.addAttribute("error", "Les nouveaux mots de passe ne correspondent pas.");
            return "view-new-password";
        }

        try {
            utilisateursService.réinitialiserMotDePasse(token, nouveauMotDePasse);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Le lien de réinitialisation est invalide ou a expiré.");
            return "view-new-password";
        }
    }

}
