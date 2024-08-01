package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bll.EnchereService;
import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import fr.eni.projet.eniencheres.bo.Categorie;
import fr.eni.projet.eniencheres.bo.Adresse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ArticleAVendreDaoImpl implements ArticleAVendreDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UtilisateursDao utilisateursDao;
    private final CategorieDao categorieDao;
    private final AdresseDao adresseDao;

    private static final String INSERT_ARTICLE = "INSERT INTO articles_a_vendre (nom_article, description, date_debut_encheres, " +
            "date_fin_encheres, statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait) " +
            "VALUES (:nomArticle, :description, :dateDebutEncheres, :dateFinEncheres, :statutEnchere, :prixInitial, :prixVente, :idUtilisateur, :noCategorie, :noAdresseRetrait)";

    private static final String SELECT_ARTICLE_BY_ID = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, " +
            "statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait, photo " +
            "FROM articles_a_vendre WHERE no_article = :id";

    private static final String UPDATE_ARTICLE = "UPDATE articles_a_vendre SET nom_article = :nomArticle, description = :description, " +
            "date_debut_encheres = :dateDebutEncheres, date_fin_encheres = :dateFinEncheres, statut_enchere = :statutEnchere, " +
            "prix_initial = :prixInitial, prix_vente = :prixVente, photo = :photo, id_utilisateur = :idUtilisateur, no_categorie = :noCategorie, " +
            "no_adresse_retrait = :noAdresseRetrait WHERE no_article = :id";

    private static final String DELETE_ARTICLE = "DELETE FROM articles_a_vendre WHERE no_article = :id";

    private static final String SELECT_ALL_ARTICLES = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, " +
            "statut_enchere, prix_initial, prix_vente, id_utilisateur, no_categorie, no_adresse_retrait, photo " +
            "FROM articles_a_vendre";

    private static final String FIND_BY_CATEGORIE_AND_QUERY = "SELECT * FROM articles_a_vendre WHERE no_categorie = :categorieId AND nom_article LIKE :query";

    private static final String FIND_BY_QUERY = "SELECT * FROM articles_a_vendre WHERE nom_article LIKE :query";

    private static final String FIND_ENCHERES_OUVERTES = "SELECT * FROM ARTICLES_A_VENDRE " +
            "WHERE date_debut_encheres <= GETDATE() " +
            "AND date_fin_encheres >= GETDATE() " +
            "AND statut_enchere = 1 ";

    private static final String FIND_ENCHERES_EN_COURS = "SELECT * FROM ARTICLES_A_VENDRE a JOIN ENCHERES e ON a.no_article = e.no_article WHERE e.id_utilisateur = :utilisateurId AND a.statut_enchere = 1";

    private static final String FIND_ENCHERES_REMPORTEES = "SELECT * FROM ARTICLES_A_VENDRE a JOIN ENCHERES e ON a.no_article = e.no_article WHERE e.id_utilisateur = :utilisateurId AND a.statut_enchere = 2";

    private static final String FIND_VENTES_EN_COURS = "SELECT * FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :utilisateurId AND statut_enchere = 1";

    private static final String FIND_ALL_VENTES = "SELECT * FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :utilisateurId";

    private static final String FIND_VENTES_NON_DEBUTEES = "SELECT * FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :utilisateurId AND statut_enchere = 0";

    private static final String FIND_VENTES_TERMINEES = "SELECT * FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :utilisateurId AND statut_enchere = 2";

    private static final String DELETE_ARTICLES_BY_USER  = "DELETE FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :utilisateurId";

    @Autowired
    public ArticleAVendreDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, UtilisateursDao utilisateursDao,
                                 CategorieDao categorieDao, AdresseDao adresseDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilisateursDao = utilisateursDao;
        this.categorieDao = categorieDao;
        this.adresseDao = adresseDao;
    }


    @Override
    public void create(ArticleAVendre article) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("nomArticle", article.getNomArticle());
        namedParameters.addValue("description", article.getDescription());
        namedParameters.addValue("dateDebutEncheres", article.getDateDebutEncheres());
        namedParameters.addValue("dateFinEncheres", article.getDateFinEncheres());
        namedParameters.addValue("statutEnchere", article.getStatutEnchere());
        namedParameters.addValue("prixInitial", article.getPrixInitial());
        namedParameters.addValue("prixVente", article.getPrixVente());
        namedParameters.addValue("idUtilisateur", article.getVendeur().getId());
        namedParameters.addValue("noCategorie", article.getCategorie().getId());
        namedParameters.addValue("noAdresseRetrait", article.getAdresseRetrait().getId());

        jdbcTemplate.update(INSERT_ARTICLE, namedParameters, keyHolder);
        article.setId(keyHolder.getKey().longValue());
    }

    @Override
    public ArticleAVendre read(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        return jdbcTemplate.queryForObject(SELECT_ARTICLE_BY_ID, namedParameters, new ArticleMapper());
    }

    @Override
    public void update(ArticleAVendre article) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("nomArticle", article.getNomArticle());
        namedParameters.addValue("description", article.getDescription());
        namedParameters.addValue("dateDebutEncheres", article.getDateDebutEncheres());
        namedParameters.addValue("dateFinEncheres", article.getDateFinEncheres());
        namedParameters.addValue("statutEnchere", article.getStatutEnchere());
        namedParameters.addValue("prixInitial", article.getPrixInitial());
        namedParameters.addValue("prixVente", article.getPrixVente());
        namedParameters.addValue("photo", article.getImagePath());
        namedParameters.addValue("idUtilisateur", article.getVendeur().getId());
        namedParameters.addValue("noCategorie", article.getCategorie().getId());
        namedParameters.addValue("noAdresseRetrait", article.getAdresseRetrait().getId());
        namedParameters.addValue("id", article.getId());

        jdbcTemplate.update(UPDATE_ARTICLE, namedParameters);
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        jdbcTemplate.update(DELETE_ARTICLE, namedParameters);
    }

    @Override
    public List<ArticleAVendre> FIND_ALL_VENTES(Long utilisateurId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("utilisateurId", utilisateurId);
        return jdbcTemplate.query(FIND_ALL_VENTES, params, new ArticleMapper());
    }

    @Override
    public List<ArticleAVendre> findAll() {
        return jdbcTemplate.query(SELECT_ALL_ARTICLES, new ArticleMapper());
    }

    @Override
    public List<ArticleAVendre> findByCategorieAndQuery(Long categorieId, String query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categorieId", categorieId);
        params.addValue("query", "%" + query + "%");
        return jdbcTemplate.query(FIND_BY_CATEGORIE_AND_QUERY, params, new ArticleMapper());
    }

    @Override
    public List<ArticleAVendre> findByQuery(String query) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("query", "%" + query + "%");
        return jdbcTemplate.query(FIND_BY_QUERY, params, new ArticleMapper());
    }


    @Override
    public List<ArticleAVendre> findEncheresOuvertes(Long utilisateurId) {

        return jdbcTemplate.query(FIND_ENCHERES_OUVERTES, new ArticleMapper());
    }

    @Override
    public List<ArticleAVendre> findEncheresEnCours(Long utilisateurId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("utilisateurId", utilisateurId);
        return jdbcTemplate.query(FIND_ENCHERES_EN_COURS, params, new ArticleMapper());
    }

    @Override
    public List<ArticleAVendre> findEncheresRemportees(Long utilisateurId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("utilisateurId", utilisateurId);
        return jdbcTemplate.query(FIND_ENCHERES_REMPORTEES, params, new ArticleMapper());
    }

    @Override
    public List<ArticleAVendre> findVentesEnCours(Long utilisateurId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("utilisateurId", utilisateurId);
        return jdbcTemplate.query(FIND_VENTES_EN_COURS, params, new ArticleMapper());
    }

    @Override
    public List<ArticleAVendre> findVentesNonDebutees(Long utilisateurId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("utilisateurId", utilisateurId);
        return jdbcTemplate.query(FIND_VENTES_NON_DEBUTEES, params, new ArticleMapper());
    }

    @Override
    public List<ArticleAVendre> findVentesTerminees(Long utilisateurId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("utilisateurId", utilisateurId);
        return jdbcTemplate.query(FIND_VENTES_TERMINEES, params, new ArticleMapper());
    }

    @Override
    public void supprimerArticlesParUtilisateur(Long utilisateurId) {
        // Supprimer les enchères associées aux articles de l'utilisateur
       // enchereService.supprimerEnchereParArticle(utilisateurId);

        // Supprimer les articles de l'utilisateur
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("utilisateurId", utilisateurId);

        jdbcTemplate.update(DELETE_ARTICLES_BY_USER, namedParameters);
    }



    private class ArticleMapper implements RowMapper<ArticleAVendre> {
        @Override
        public ArticleAVendre mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArticleAVendre article = new ArticleAVendre();
            article.setId(rs.getLong("no_article"));
            article.setNomArticle(rs.getString("nom_article"));
            article.setDescription(rs.getString("description"));
            article.setDateDebutEncheres(rs.getTimestamp("date_debut_encheres").toLocalDateTime());;
            article.setDateFinEncheres(rs.getTimestamp("date_fin_encheres").toLocalDateTime());
            article.setImagePath(rs.getString("photo"));
            article.setStatutEnchere(rs.getInt("statut_enchere"));
            article.setPrixInitial(rs.getInt("prix_initial"));
            article.setPrixVente(rs.getInt("prix_vente"));

            // Associer le vendeur
            Utilisateurs vendeur = utilisateursDao.readById(rs.getLong("id_utilisateur"));
            article.setVendeur(vendeur);

            // Associer la catégorie
            Categorie categorie = categorieDao.read(rs.getLong("no_categorie"));
            article.setCategorie(categorie);

            // Associer l'adresse de retrait
            Adresse adresseRetrait = adresseDao.read(rs.getLong("no_adresse_retrait"));
            article.setAdresseRetrait(adresseRetrait);

            return article;
        }
    }
}
