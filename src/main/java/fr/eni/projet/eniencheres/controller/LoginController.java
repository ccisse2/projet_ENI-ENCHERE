package fr.eni.projet.eniencheres.controller;


import fr.eni.projet.eniencheres.bll.UtilisateursService;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/login")
@SessionAttributes("membreEnSession")
public class LoginController {

    private final UtilisateursService utilisateurService;

    @Autowired
    public LoginController(UtilisateursService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public String loginForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateurs());
        return "login";
    }

    @ModelAttribute("membreEnSession")
    public Utilisateurs membreEnSession() {
        return new Utilisateurs();
    }

    @GetMapping("/success")
    public String loginSuccess(HttpSession session, @ModelAttribute("membreEnSession") Utilisateurs membreEnSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = auth.getName();  // Récupère le pseudo de l'utilisateur authentifié

        Utilisateurs aCharger = utilisateurService.charger(pseudo);
        if (aCharger != null) {
            membreEnSession.setId(aCharger.getId());
            membreEnSession.setNom(aCharger.getNom());
            membreEnSession.setPrenom(aCharger.getPrenom());
            membreEnSession.setPseudo(aCharger.getPseudo());
            membreEnSession.setAdmin(aCharger.isAdmin());
            membreEnSession.setEmail(aCharger.getEmail());
            membreEnSession.setTelephone(aCharger.getTelephone());
            membreEnSession.setCredit(aCharger.getCredit());
            membreEnSession.setAdresse(aCharger.getAdresse());
            membreEnSession.setArticlesVendus(aCharger.getArticlesVendus());
        } else {
            membreEnSession.setId(0L);
            membreEnSession.setNom(null);
            membreEnSession.setPrenom(null);
            membreEnSession.setPseudo(null);
            membreEnSession.setAdmin(false);
        }
        session.setAttribute("membreEnSession", membreEnSession);
        return "redirect:/";
    }
    @GetMapping("/session-timeout")
    public String sessionTimeout() {
        return "view-session-expirer";
    }
}
