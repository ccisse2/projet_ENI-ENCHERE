package fr.eni.projet.eniencheres.configuration;

import fr.eni.projet.eniencheres.bll.AcquisitionServiceImpl;
import fr.eni.projet.eniencheres.bll.ArticleAVendreService;
import fr.eni.projet.eniencheres.bll.EnchereService;
import fr.eni.projet.eniencheres.bo.Acquisition;
import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import fr.eni.projet.eniencheres.bo.Enchere;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    private final ArticleAVendreService articleAVendreService;
    private final EnchereService enchereService;
    private final AcquisitionServiceImpl acquisitionServiceImpl;

    @Autowired
    public SchedulerConfig(ArticleAVendreService articleAVendreService, EnchereService enchereService, AcquisitionServiceImpl acquisitionServiceImpl) {
        this.articleAVendreService = articleAVendreService;
        this.enchereService = enchereService;
        this.acquisitionServiceImpl = acquisitionServiceImpl;
    }

    // Tâche planifiée pour mettre à jour les enchères à "en cours"
    @Scheduled(fixedRate = 60000) // Vérifier toutes les minutes
    public void updateEncheresStatus() {
        logger.info("Exécution de la méthode updateEncheresStatus...");
        List<ArticleAVendre> articles = articleAVendreService.toutLesArticlesAVendre();

        LocalDateTime now = LocalDateTime.now();
        logger.info("Date actuelle : {}", now);

        for (ArticleAVendre article : articles) {
            logger.info("Vérification de l'article : {} avec statut : {} et dates : {} - {}", article.getNomArticle(),
                    article.getStatutEnchere(), article.getDateDebutEncheres(), article.getDateFinEncheres());

            if (article.getDateDebutEncheres().isBefore(now) && article.getStatutEnchere() == 0) {
                article.setStatutEnchere(1);
                articleAVendreService.modifierArticleAVendre(article);
                logger.info("Article {} mis à jour à statut en cours.", article.getNomArticle());

            } else if (article.getDateFinEncheres().isBefore(now) && article.getStatutEnchere() == 1) {
                article.setStatutEnchere(2);
                articleAVendreService.modifierArticleAVendre(article);
                logger.info("Article {} mis à jour à statut clôturé.", article.getNomArticle());


                Optional<Enchere> optionalEnchere = enchereService.rechercherEnchereParArticleId(article.getId());

                if (optionalEnchere.isPresent()) {
                    Enchere enchereGagnante = optionalEnchere.get();
                    Acquisition acquisition = new Acquisition();
                    acquisition.setUtilisateur(enchereGagnante.getAcheteur());
                    acquisition.setArticle(article);
                    acquisitionServiceImpl.ajouterAcquisition(acquisition);
                    logger.info("Acquisition ajoutée pour l'utilisateur : {} et l'article : {}", enchereGagnante.getAcheteur().getPseudo(), article.getNomArticle());

                }
            }
        }
    }
}
