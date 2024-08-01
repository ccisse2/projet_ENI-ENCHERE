package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bll.ArticleAVendreService;
import fr.eni.projet.eniencheres.bo.Enchere;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Repository
public class EnchereDaoImpl implements EnchereDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UtilisateursDao utilisateursDao;
    private final ArticleAVendreService articleAVendreservice;


    private static final String INSERT_ENCHERE = "INSERT INTO encheres (id_utilisateur, no_article, montant_enchere, date_enchere) " +
            "VALUES ((SELECT id FROM utilisateurs WHERE pseudo = :pseudo), :articleId, :montantEnchere, :dateEnchere)";

    private static final String UPDATE_ENCHERE = "UPDATE encheres SET montant_enchere = :montantEnchere, date_enchere = :dateEnchere " +
            "WHERE id_utilisateur = (SELECT id FROM utilisateurs WHERE pseudo = :pseudo) AND no_article = :articleId";

    private static final String DELETE_ENCHERE = "DELETE FROM encheres WHERE id_utilisateur = " +
            "(SELECT id FROM utilisateurs WHERE pseudo = :pseudo) AND no_article = :articleId";

    private static final String SELECT_ENCHERE_BY_ARTICLE = "SELECT id_utilisateur, no_article, montant_enchere, date_enchere " +
            "FROM encheres WHERE no_article = :articleId";

    private static final String FIND_BY_UTILISATEUR = "SELECT id_utilisateur, no_article, montant_enchere, date_enchere " +
            "FROM encheres WHERE id_utilisateur = :utilisateurId";

    private static final String DELETE_ENCHERES_BY_USER = "DELETE FROM ENCHERES WHERE id_utilisateur = :utilisateurId";

    private static final String DELETE_ENCHERES_BY_ARTICLE = "DELETE FROM ENCHERES WHERE no_article IN (SELECT no_article FROM ARTICLES_A_VENDRE WHERE id_utilisateur = :utilisateurId)";


    @Autowired
    public EnchereDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, UtilisateursDao utilisateursDao,
                          ArticleAVendreService articleAVendreservice) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilisateursDao = utilisateursDao;
        this.articleAVendreservice = articleAVendreservice;
    }

    @Override
    public void create(Enchere enchere) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", enchere.getAcheteur().getPseudo());
        namedParameters.addValue("articleId", enchere.getArticle().getId());
        namedParameters.addValue("montantEnchere", enchere.getMontantEnchere());
        namedParameters.addValue("dateEnchere", enchere.getDateEnchere());

        jdbcTemplate.update(INSERT_ENCHERE, namedParameters);
    }

    @Override
    public void update(Enchere enchere) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", enchere.getAcheteur().getPseudo());
        namedParameters.addValue("articleId", enchere.getArticle().getId());
        namedParameters.addValue("montantEnchere", enchere.getMontantEnchere());
        namedParameters.addValue("dateEnchere", enchere.getDateEnchere());

        jdbcTemplate.update(UPDATE_ENCHERE, namedParameters);
    }

    @Override
    public void delete(Enchere enchere) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", enchere.getAcheteur().getPseudo());
        namedParameters.addValue("articleId", enchere.getArticle().getId());

        jdbcTemplate.update(DELETE_ENCHERE, namedParameters);
    }

    @Override
    public Optional<Enchere> findByArticleId(long articleId) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("articleId", articleId);

            Enchere enchere = jdbcTemplate.queryForObject(SELECT_ENCHERE_BY_ARTICLE, namedParameters, new EnchereMapper());
            System.out.println("Enchere trouvée : " + enchere);
            return Optional.ofNullable(enchere);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    @Override
    public List<Enchere> findByUtilisateur(long utilisateurId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("utilisateurId", utilisateurId);


        return jdbcTemplate.query(FIND_BY_UTILISATEUR, namedParameters, new EnchereMapper());
    }

    private class EnchereMapper implements RowMapper<Enchere> {
        @Override
        public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
            Enchere enchere = new Enchere();
            enchere.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
            enchere.setMontantEnchere(rs.getInt("montant_enchere"));

            // Associer l'utilisateur
            Utilisateurs acheteur = utilisateursDao.readById(rs.getLong("id_utilisateur"));
            enchere.setAcheteur(acheteur);

            // Associer l'article
            ArticleAVendre article = articleAVendreservice.getArticleAVendreById((long) rs.getInt("no_article"));
            enchere.setArticle(article);

            return enchere;
        }
    }

    @Override
    public void annulerEncheresParUtilisateur(long utilisateurId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("utilisateurId", utilisateurId);

        jdbcTemplate.update(DELETE_ENCHERES_BY_USER, namedParameters);
    }

    @Override
    public void supprimerEncheresParArticle(Long utilisateurId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("utilisateurId", utilisateurId);

        jdbcTemplate.update(DELETE_ENCHERES_BY_ARTICLE, namedParameters);
    }
}
