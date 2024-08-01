package fr.eni.projet.eniencheres.controller;

import fr.eni.projet.eniencheres.bll.*;
import fr.eni.projet.eniencheres.bo.*;
import fr.eni.projet.eniencheres.exceptions.BusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/article")
@SessionAttributes({"articleAVendre", "membreEnSession", "enchere"})
public class ControllerArticle {
    private final ArticleAVendreService articleAVendreService;
    private final AdresseService adresseService;
    private final UtilisateursService utilisateursService;
    private final CategorieService categorieService;
    private final EnchereService enchereService;

    @Autowired
    public ControllerArticle(ArticleAVendreService articleAVendreService, AdresseService adresseService,
                             UtilisateursService utilisateursService, CategorieService categorieService, EnchereService enenchereService, EnchereService enchereService) {
        this.articleAVendreService = articleAVendreService;
        this.adresseService = adresseService;
        this.utilisateursService = utilisateursService;
        this.categorieService = categorieService;
        this.enchereService = enchereService;
    }

    @ModelAttribute("articleAVendre")
    public ArticleAVendre initArticle() {
        return new ArticleAVendre();
    }

    @ModelAttribute("enchere")
    public Enchere initEnchere() {
        return new Enchere();
    }

    @GetMapping("/creer")
    public String afficherFormulaireCreationArticle(HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = auth.getName();
        Utilisateurs utilisateur = utilisateursService.charger(pseudo);

        // Récupérer l'adresse par défaut du vendeur
        Adresse adresseParDefaut = adresseService.chercherAdresseParUtilisateur(utilisateur.getId());

        // Vérifiez que l'adresse par défaut à un ID valide
        // Récupérer toutes les adresses ENI
        List<Adresse> adressesEni = adresseService.chercherToutesLesAdressesEni();
        List<Categorie> categories = categorieService.recupererToutesLesCategories();

        // Ajouter l'adresse par défaut du vendeur en haut de la liste
        adressesEni.add(0, adresseParDefaut);

        model.addAttribute("adresses", adressesEni);
        model.addAttribute("categories", categories);
        model.addAttribute("articleAVendre", new ArticleAVendre());
        return "view-creer-article";
    }


    @PostMapping("/creer")
    public String creerArticle(@ModelAttribute("articleAVendre") ArticleAVendre articleAVendre,
                               @ModelAttribute("membreEnSession") Utilisateurs membreEnSession,
                               BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "view-creer-article";
        }
        articleAVendre.setVendeur(membreEnSession);
        articleAVendreService.creerArticleAVendre(articleAVendre);
        Long articleId = articleAVendre.getId();
        redirectAttributes.addFlashAttribute("articleId", articleId);
        System.out.println("Article créé avec succès." + articleAVendre);
        return "redirect:/article/" + articleId + "/upload-image";
    }

    @GetMapping("/{id}/upload-image")
    public String afficherFormulaireUploadImage(@PathVariable("id") Long articleId, Model model) {
        model.addAttribute("articleId", articleId);
        return "view-upload-image";
    }


    @PostMapping("/{id}/upload-image")
    public String uploadImage(@RequestParam("image") MultipartFile image,
                              @PathVariable("id") Long articleId,
                              RedirectAttributes redirectAttributes) {
        System.out.println("Image uploaded: " + articleId);
        ArticleAVendre article = articleAVendreService.getArticleAVendreById(articleId);

        if (article == null) {
            redirectAttributes.addFlashAttribute("error", "Article introuvable.");
            return "redirect:/article/" + articleId + "/upload-image";
        }

        if (!image.isEmpty()) {
            System.out.println("Image size: " + image.getSize() + " bytes");
            try {
                String uploadsDir = "src/main/resources/static/images/";
                Path uploadsPath = Paths.get(uploadsDir);
                if (!Files.exists(uploadsPath)) {
                    Files.createDirectories(uploadsPath);
                    System.out.println("Directory created: " + uploadsPath.toString());
                }
                String filename = image.getOriginalFilename();
                Path path = uploadsPath.resolve(filename);
                Files.write(path, image.getBytes());

                System.out.println("Image saved to " + path.toString());

                // Stockez le chemin relatif dans la base de données
                String relativePath = "/images/" + filename;
                article.setImagePath(relativePath);
                articleAVendreService.modifierArticleAVendre(article);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Erreur lors de l'upload de l'image : " + e.getMessage());
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
                return "redirect:/article/" + articleId + "/upload-image";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "L'image est vide.");
            return "redirect:/article/" + articleId + "/upload-image";
        }
        System.out.println("Image uploaded successfully.");
        redirectAttributes.addFlashAttribute("success", "Image uploadée avec succès.");
        return "redirect:/";
    }



    @GetMapping("details/{id}")
    public String afficherDetailsArticle(@PathVariable Long id, Model model) {
        ArticleAVendre articleAVendre = articleAVendreService.getArticleAVendreById(id);
        model.addAttribute("article", articleAVendre);
        return "view-detail-article";
    }


    @PostMapping("/encherir")
    public String encherir(@RequestParam long id, @RequestParam int montantEnchere,
                           @ModelAttribute("enchere") Enchere enchrere, BindingResult result,
                           Authentication authentication, Model model) {

        ArticleAVendre article = articleAVendreService.getArticleAVendreById(id);
        String pseudo = authentication.getName();
        Utilisateurs utilisateur = utilisateursService.charger(pseudo);

        try {
            // Appelez la méthode pour valider et modifier l'enchère
            Enchere enchere = new Enchere(LocalDateTime.now(), montantEnchere, utilisateur, article);
            enchereService.modifierEnchere(enchere);
            model.addAttribute("enchere", enchere);
            model.addAttribute("success", "Votre enchère a bien été prise en compte.");

        } catch (BusinessException be) {
            model.addAttribute("article", article);
            model.addAttribute("errors", be.getClefsExternalisations());
            be.getClefsExternalisations().forEach(key -> {
                ObjectError error = new ObjectError("globalError", key);
                result.addError(error);
            });
            return "view-detail-article";
        }

        model.addAttribute("article", article);
        return "view-detail-article";
    }


    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ArticleAVendre article = articleAVendreService.getArticleAVendreById(id);
        model.addAttribute("articleAVendre", article);
        model.addAttribute("categories", categorieService.recupererToutesLesCategories());
        model.addAttribute("adresses", adresseService.chercherToutesLesAdressesEni());
        return "view-edit-article";
    }

    @PostMapping("/{id}/edit")
    public String modifierArticle(@Valid @ModelAttribute("articleAVendre") ArticleAVendre articleAVendre,
                                  @ModelAttribute("membreEnSession") Utilisateurs membreEnSession,
                                  BindingResult result,
                                  Model model, RedirectAttributes redirectAttributes) {
        System.out.println("Début modification article");
        if (result.hasErrors()) {
            model.addAttribute("article", articleAVendre);
            System.out.println("Erreurs dans le formulaire");
            return "view-edit-article";
        }
        Utilisateurs utilisateur = utilisateursService.charger(membreEnSession.getPseudo());

        if (utilisateur == null) {
            System.out.println("Utilisateur introuvable");
            result.rejectValue("vendeur", "error.articleAVendre", "Utilisateur introuvable.");
            model.addAttribute("article", articleAVendre);
            return "view-edit-article";
        }
       /* if (articleAVendre.getDateDebutEncheres() != null || articleAVendre.getDateDebutEncheres().isBefore(LocalDateTime.now())) {
            System.out.println("Date de début d'enchère dépassée");
            result.rejectValue("dateDebutEncheres", "error.articleAVendre",
                    "L'article ne peut pas être modifié après la date de début d'enchère.");
            model.addAttribute("article", articleAVendre);
            return "view-edit-article";
        }*/
        System.out.println("Article modifié avec succès");
        articleAVendre.setVendeur(utilisateur);
        articleAVendreService.modifierArticleAVendre(articleAVendre);
        redirectAttributes.addFlashAttribute("articleId", articleAVendre.getId());
        return "view-upload-image";
    }

    @GetMapping("/{id}/delete")
    public String supprimerArticle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ArticleAVendre article = articleAVendreService.getArticleAVendreById(id);

        if (article == null) {
            redirectAttributes.addFlashAttribute("error", "L'article n'existe plus en base de donnée.");
            return "redirect:/";
        }

        LocalDateTime dateDebutEncheres = article.getDateDebutEncheres();

        if (dateDebutEncheres != null && dateDebutEncheres.isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error",
                    "L'article ne peut pas être supprimé après la date de début d'enchère.");
            return "redirect:/article/details/" + id;
        }

        articleAVendreService.supprimerArticleAVendre(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/retrait")
    public String confirmerRetrait(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ArticleAVendre article = articleAVendreService.getArticleAVendreById(id);
        if (article == null) {
            redirectAttributes.addFlashAttribute("error", "L'article n'existe plus.");
            return "redirect:/";
        }

        // Vérifier si la date de fin de l'enchère est passée
        if (LocalDateTime.now().isBefore(article.getDateFinEncheres())) {
            redirectAttributes.addFlashAttribute("error", "L'enchère n'est pas encore terminée.");
            return "redirect:/article/details/" + id;
        }

        // Logique pour créditer les points au vendeur
        Utilisateurs vendeur = article.getVendeur();
        int points = article.getPrixVente();
        vendeur.setCredit(vendeur.getCredit().add(BigDecimal.valueOf(points)));
        utilisateursService.mettreAJourUtilisateur(vendeur);

        // Ajouter un message de succès et rediriger vers la page d'accueil
        redirectAttributes.addFlashAttribute("success", "Les points ont été crédités avec succès.");
        return "redirect:/";
    }
}
