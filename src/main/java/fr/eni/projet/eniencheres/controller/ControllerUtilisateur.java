package fr.eni.projet.eniencheres.controller;

import fr.eni.projet.eniencheres.bll.ArticleAVendreService;
import fr.eni.projet.eniencheres.bll.EmailService;
import fr.eni.projet.eniencheres.bll.UtilisateursService;
import fr.eni.projet.eniencheres.bo.Adresse;
import fr.eni.projet.eniencheres.bo.PasswordResetForm;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/utilisateurs")
public class ControllerUtilisateur {

    private final UtilisateursService utilisateursService;
    private final ArticleAVendreService articleAVendreService;
    private final EmailService emailService;

    @Autowired
    public ControllerUtilisateur(UtilisateursService utilisateursService, ArticleAVendreService articleAVendreService, EmailService emailService) {
        this.utilisateursService = utilisateursService;
        this.articleAVendreService = articleAVendreService;
        this.emailService = emailService;
    }

    @GetMapping("/register")
    public String creerUtilisateur(@Valid Model model) {
        model.addAttribute("utilisateur", new Utilisateurs());
        model.addAttribute("action", "/utilisateurs/register");
        return "view-register";
    }

    @PostMapping("/register")
    public String creerUtilisateur(@Valid @ModelAttribute("utilisateur") Utilisateurs utilisateur,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "view-register";
        }
        // Générer un token unique pour la vérification de l'email
        String token = UUID.randomUUID().toString();
        // Stocker les données de l'utilisateur dans une table temporaire
        utilisateursService.creerUtilisateurTemporaire(utilisateur, token);

        // Envoyer l'email de vérification
        String lienVerification = "http://localhost:8080/utilisateurs/verify?token=" + token;
        emailService.sendVerificationEmail(utilisateur.getEmail(), lienVerification);

        model.addAttribute("message", "Un email de vérification a été envoyé. Veuillez vérifier votre boîte de réception.");

        return "view-register-confirmation";
     /*   // Créer l'adresse
        Adresse adresse = new Adresse();
        adresse.setRue(utilisateur.getAdresse().getRue());
        adresse.setCodePostal(utilisateur.getAdresse().getCodePostal());
        adresse.setVille(utilisateur.getAdresse().getVille());
        utilisateursService.creerAdresse(adresse);

        // Associer l'adresse à l'utilisateur
        utilisateur.setAdresse(adresse);
        utilisateursService.creerUtilisateur(utilisateur);
        return "redirect:/";  */
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        try {
            utilisateursService.validerEmailUtilisateurTemporaire(token);
            redirectAttributes.addFlashAttribute("success", "Votre email a été vérifié avec succès. Vous pouvez maintenant vous connecter.");
            model.addAttribute("message", "Votre email a été vérifié avec succès. Vous pouvez maintenant vous connecter.");
            return "login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Le lien de vérification est invalide ou a expiré.");
            model.addAttribute("error", "Le lien de vérification est invalide ou a expiré.");
            return "view-register";
        }
    }

    @PostMapping("/{id}/anonymiser")
    public String anonymiserUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Mettre à jour les ressources associées
            articleAVendreService.mettreAJourRessourcesPourUtilisateur(id);
            // Anonymiser les données utilisateur
            utilisateursService.anonymiserUtilisateur(id);
            redirectAttributes.addFlashAttribute("success", "Compte utilisateur anonymisé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'anonymisation du compte utilisateur.");
        }
        return "redirect:/profil"; // Rediriger vers une page appropriée
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
    public String réinitialiserMotDePasse(@PathVariable String token,
                                          @Valid @ModelAttribute("passwordResetForm") PasswordResetForm passwordResetForm,
                                          BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "view-new-password";
        }
        if (!passwordResetForm.getNouveauMotDePasse().equals(passwordResetForm.getConfirmerMotDePasse())) {
            model.addAttribute("error", "Les nouveaux mots de passe ne correspondent pas.");
            return "view-new-password";
        }

        try {
            utilisateursService.réinitialiserMotDePasse(token, passwordResetForm.getNouveauMotDePasse());
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Le lien de réinitialisation est invalide ou a expiré.");
            return "view-new-password";
        }
    }
}
