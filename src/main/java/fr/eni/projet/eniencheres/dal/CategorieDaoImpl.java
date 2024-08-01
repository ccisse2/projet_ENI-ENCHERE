package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.Categorie;
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
public class CategorieDaoImpl implements CategorieDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String INSERT_CATEGORIE = "INSERT INTO categories (libelle) VALUES (:libelle)";
    private static final String SELECT_CATEGORIE_BY_ID = "SELECT no_categorie, libelle FROM categories WHERE no_categorie = :id";
    private static final String UPDATE_CATEGORIE = "UPDATE categories SET libelle = :libelle WHERE no_categorie = :id";
    private static final String DELETE_CATEGORIE = "DELETE FROM categories WHERE no_categorie = :id";
    private static final String SELECT_ALL_CATEGORIES = "SELECT no_categorie, libelle FROM categories";

    @Override
    public void create(Categorie categorie) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("libelle", categorie.getLibelle());

        jdbcTemplate.update(INSERT_CATEGORIE, namedParameters, keyHolder);
        categorie.setId(keyHolder.getKey().longValue());
    }

    @Override
    public Categorie read(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        return jdbcTemplate.queryForObject(SELECT_CATEGORIE_BY_ID, namedParameters, new CategorieMapper());
    }

    @Override
    public void update(Categorie categorie) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("libelle", categorie.getLibelle());
        namedParameters.addValue("id", categorie.getId());

        jdbcTemplate.update(UPDATE_CATEGORIE, namedParameters);
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        jdbcTemplate.update(DELETE_CATEGORIE, namedParameters);
    }

    @Override
    public List<Categorie> findAll() {
        return jdbcTemplate.query(SELECT_ALL_CATEGORIES, new CategorieMapper());
    }

    private static final class CategorieMapper implements RowMapper<Categorie> {
        @Override
        public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {
            Categorie categorie = new Categorie();
            categorie.setId(rs.getLong("no_categorie"));
            categorie.setLibelle(rs.getString("libelle"));
            return categorie;
        }
    }
}
