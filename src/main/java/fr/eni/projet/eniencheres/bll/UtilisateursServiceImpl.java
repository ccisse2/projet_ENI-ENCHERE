package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.*;
import fr.eni.projet.eniencheres.dal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UtilisateursServiceImpl implements UtilisateursService {
    private static final Logger logger = LoggerFactory.getLogger(UtilisateursServiceImpl.class);

    private final UtilisateursDao utilisateurDao;
    private final AdresseDao adresseDao;
    private final PasswordEncoder passwordEncoder;
    private final ArticleAVendreDao articleAVendreDao;
    private final EnchereDao enchereDao;
    private final AcquisitionService acquisitionsService;
    private final EmailService emailService;
    private final UtilisateurTemporaireDao utilisateurTemporaireDao;


    @Autowired
    public UtilisateursServiceImpl(UtilisateursDao utilisateurDao, AdresseDao adresseDao, PasswordEncoder passwordEncoder, ArticleAVendreDao articleAVendreDao, EnchereDao enchereDao, AcquisitionService acquisitionsService, EmailService emailService, UtilisateurTemporaireDao utilisateurTemporaireDao) {
        this.utilisateurDao = utilisateurDao;
        this.adresseDao = adresseDao;
        this.passwordEncoder = passwordEncoder;
        this.articleAVendreDao = articleAVendreDao;
        this.enchereDao = enchereDao;
        this.acquisitionsService = acquisitionsService;
        this.emailService = emailService;
        this.utilisateurTemporaireDao = utilisateurTemporaireDao;
    }


    @Override
    public void creerUtilisateur(Utilisateurs utilisateur) {
        try {
        String encodedPassword = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(encodedPassword);
        utilisateurDao.create(utilisateur);
            logger.info("Utilisateur créé avec succès: {}", utilisateur.getPseudo());
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur: {}", utilisateur.getPseudo(), e);
        }
    }

    @Override
    public Utilisateurs recupererUtilisateurParPseudo(String pseudo) {
        Utilisateurs utilisateur = utilisateurDao.readByPseudo(pseudo);
        if (utilisateur == null) {
            logger.error("Utilisateur non trouvé pour le pseudo: {}", pseudo);
        } else {
            logger.info("Utilisateur récupéré: {}", pseudo);
        }
        return utilisateur;
    }

    @Override
    public void mettreAJourUtilisateur(Utilisateurs utilisateur) {
        try{
            utilisateurDao.update(utilisateur);
            logger.info("les informations de l'utilisateur on bien été mis a jour: {}", utilisateur.getPseudo());
        }catch (Exception e){
            logger.error("Erreur lors de la mise à jour des informations de l'utilisateur: {}", utilisateur.getPseudo(), e);
        }

    }

    @Override
    public void mettreAJourMotDePasse(String pseudo, String nouveauMotDePasse) {
        try {
            String encodedPassword = passwordEncoder.encode(nouveauMotDePasse);
            utilisateurDao.updateMotDePasse(pseudo, encodedPassword);
            logger.info("Mot de passe mis à jour pour l'utilisateur: {}", pseudo);
        }catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du mot de passe de l'utilisateur: {}", pseudo, e);
        }

    }

    @Override
    public void supprimerUtilisateur(String pseudo) {
        try {
            Utilisateurs utilisateur = utilisateurDao.readByPseudo(pseudo);
            if (utilisateur != null) {
                utilisateurDao.deleteById(utilisateur.getId());
                logger.info("Utilisateur supprimé avec succès: {}", pseudo);
            } else {
                logger.error("Utilisateur non trouvé pour la suppression: {}", pseudo);
            }
        }catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'utilisateur: {}", pseudo, e);
        }

    }

    @Override
    public void supprimerUtilisateur(Long utilisateurId) {
        try {
            enchereDao.annulerEncheresParUtilisateur(utilisateurId);
            enchereDao.supprimerEncheresParArticle(utilisateurId);

            // Supprimer toutes les acquisitions liées
            acquisitionsService.supprimerAcquisitionsParUtilisateur(utilisateurId);

            // Supprimer tous les articles liés
            articleAVendreDao.supprimerArticlesParUtilisateur(utilisateurId);

            // Supprimer l'utilisateur
            utilisateurDao.deleteById(utilisateurId);
            logger.info("Utilisateur supprimé avec succès (ID: {})", utilisateurId);
        }catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'utilisateur (ID: {})", utilisateurId, e);
        }

    }


    @Override
    public List<Utilisateurs> recupererTousLesUtilisateurs() {
        try {
            List<Utilisateurs> utilisateurs = utilisateurDao.findAll();
            logger.info("Récupération de tous les utilisateurs");
            return utilisateurs;
        }catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les utilisateurs", e);
            return null;
        }
    }

    @Override
    public void creerAdresse(Adresse adresse) {
        try{
            adresseDao.create(adresse);
        }catch (Exception e){
            logger.error("Erreur lors de la création de l'adresse: {}", adresse.getId(), e);
        }

    }

    @Override
    public Utilisateurs charger(String pseudo) {
        Utilisateurs utilisateur = utilisateurDao.readByPseudo(pseudo);
        if (utilisateur != null) {
            // Chargez toutes les informations supplémentaires nécessaires
            Adresse adresse = adresseDao.readByUtilisateur(utilisateur.getId());
            utilisateur.setAdresse(adresse);
            // Ajoutez les autres informations si nécessaire
        }
        return utilisateur;
    }


    @Override
    public boolean verifierMotDePasse(String ancienMotDePasse, String motDePasseEncode) {
        return passwordEncoder.matches(ancienMotDePasse, motDePasseEncode);
    }

    @Override
    public void anonymiserUtilisateur(Long utilisateurId) {
        Utilisateurs utilisateur = utilisateurDao.readById(utilisateurId);
        if (utilisateur !=null) {
            utilisateur.setPseudo("Anonyme" + utilisateurId);
            utilisateur.setNom("Anonyme");
            utilisateur.setPrenom("Anonyme");
            utilisateur.setEmail("anonyme" + utilisateurId + "@example.com");
            utilisateur.setTelephone(null);
            utilisateurDao.update(utilisateur);
        }
    }

    @Override
    public void desactiverUtilisateur(Long id) throws Exception {
        Utilisateurs utilisateur = utilisateurDao.readById(id);
        if (utilisateur != null) {
            utilisateur.setActif(false);
            utilisateurDao.update(utilisateur);

            // Annuler toutes les ventes de l'utilisateur
            List<ArticleAVendre> articles = articleAVendreDao.FIND_ALL_VENTES(utilisateur.getId());
            for (ArticleAVendre article : articles) {
                article.setStatutEnchere(100); // 100 : ANNULEE
                articleAVendreDao.update(article);
            }

            // Annuler toutes les enchères de l'utilisateur
            List<Enchere> encheres = enchereDao.findByUtilisateur(utilisateur.getId());
            for (Enchere enchere : encheres) {
                enchereDao.delete(enchere);
            }
        } else {
            throw new Exception("Utilisateur non trouvé");
        }
    }

    @Override
    public void annulerEncheresEtArticles(String pseudo) {
        Utilisateurs utilisateur = utilisateurDao.readByPseudo(pseudo);
        if (utilisateur != null) {
            // Annuler les enchères en cours
            enchereDao.annulerEncheresParUtilisateur(utilisateur.getId());

            // Supprimer les articles à vendre
            articleAVendreDao.supprimerArticlesParUtilisateur(utilisateur.getId());
        }
    }

    @Override
    public void créerTokenDeRéinitialisation(String email) {
        Utilisateurs utilisateur = utilisateurDao.findByEmail(email);
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Aucun utilisateur trouvé avec cet email.");
        }

        String token = UUID.randomUUID().toString();
        utilisateur.setResetPasswordToken(token);
        utilisateur.setTokenExpiryDate(LocalDateTime.now().plusHours(1));
        utilisateurDao.update(utilisateur);

        emailService.sendResetPasswordEmail(email, token);
    }

    @Override
    public boolean vérifierToken(String token) {
        Utilisateurs utilisateur = utilisateurDao.findByToken(token);
        if (utilisateur != null && utilisateur.getTokenExpiryDate() != null) {
            return utilisateur.getTokenExpiryDate().isAfter(LocalDateTime.now());
        }
        return false;
    }

    @Override
    public void réinitialiserMotDePasse(String token, String nouveauMotDePasse) {
        Utilisateurs utilisateur = utilisateurDao.findByToken(token);
        if (utilisateur == null || utilisateur.getTokenExpiryDate() == null || utilisateur.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token invalide ou expiré.");
        }

        utilisateur.setMotDePasse(nouveauMotDePasse);  // Assurez-vous de hacher le mot de passe
        utilisateur.setResetPasswordToken(null);
        utilisateur.setTokenExpiryDate(null);
        utilisateurDao.update(utilisateur);
    }

    @Override
    public void creerUtilisateurTemporaire(Utilisateurs utilisateur, String token) {
        UtilisateurTemporaire utilisateurTemporaire = new UtilisateurTemporaire();
        utilisateurTemporaire.setPseudo(utilisateur.getPseudo());
        utilisateurTemporaire.setNom(utilisateur.getNom());
        utilisateurTemporaire.setPrenom(utilisateur.getPrenom());
        utilisateurTemporaire.setEmail(utilisateur.getEmail());
        utilisateurTemporaire.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurTemporaire.setRue(utilisateur.getAdresse().getRue());
        utilisateurTemporaire.setCodePostal(utilisateur.getAdresse().getCodePostal());
        utilisateurTemporaire.setVille(utilisateur.getAdresse().getVille());
        utilisateurTemporaire.setDateCreation(LocalDateTime.now());
        utilisateurTemporaire.setTokenVerification(token);
        utilisateurTemporaire.setTokenExpirationDate(LocalDateTime.now().plusMinutes(30));

        utilisateurTemporaireDao.save(utilisateurTemporaire);
    }

    @Override
    public void validerEmailUtilisateurTemporaire(String token) {
        UtilisateurTemporaire utilisateurTemporaire = utilisateurTemporaireDao.findByTokenVerification(token);

        if (utilisateurTemporaire != null && utilisateurTemporaire.getTokenExpirationDate().isAfter(LocalDateTime.now())) {
            // Créez l'utilisateur principal
            Utilisateurs utilisateur = new Utilisateurs();
            utilisateur.setPseudo(utilisateurTemporaire.getPseudo());
            utilisateur.setNom(utilisateurTemporaire.getNom());
            utilisateur.setPrenom(utilisateurTemporaire.getPrenom());
            utilisateur.setEmail(utilisateurTemporaire.getEmail());
            utilisateur.setMotDePasse(utilisateurTemporaire.getMotDePasse());

            // Créez et associez l'adresse
            Adresse adresse = new Adresse();
            adresse.setRue(utilisateurTemporaire.getRue());
            adresse.setCodePostal(utilisateurTemporaire.getCodePostal());
            adresse.setVille(utilisateurTemporaire.getVille());
            adresseDao.create(adresse);

            utilisateur.setAdresse(adresse);

            // Enregistrez l'utilisateur dans la table principale
            utilisateurDao.create(utilisateur);

            // Supprimez l'utilisateur temporaire
            utilisateurTemporaireDao.delete(utilisateurTemporaire);
        } else {
            throw new IllegalArgumentException("Token invalide ou expiré.");
        }
    }
}
