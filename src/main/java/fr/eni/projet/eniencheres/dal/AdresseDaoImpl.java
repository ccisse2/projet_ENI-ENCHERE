package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.Adresse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class AdresseDaoImpl implements AdresseDao  {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String INSERT_ADRESSE = "INSERT INTO adresses (rue, code_postal, ville) " +
            "VALUES (:rue, :codePostal, :ville)";
    private static final String SELECT_ADRESSE_BY_ID = "SELECT no_adresse, rue, code_postal, ville FROM adresses WHERE no_adresse = :id";
    private static final String UPDATE_ADRESSE = "UPDATE adresses SET rue = :rue, code_postal = :codePostal, ville = :ville WHERE no_adresse = :id";
    private static final String DELETE_ADRESSE_BY_ID = "DELETE FROM adresses WHERE no_adresse = :id";
    private static final String SELECT_ADRESSE_BY_USER_ID = "SELECT a.no_adresse, rue, code_postal, ville FROM adresses a " +
            "INNER JOIN utilisateurs u ON a.no_adresse = u.no_adresse WHERE u.id = :id";
    private static final String SELECT_ADRESSE_ENI = "SELECT a.no_adresse, rue, code_postal, ville FROM adresses a WHERE a.adresse_eni = 'true'";
    public AdresseDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Adresse adresse) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("rue", adresse.getRue());
        namedParameters.addValue("codePostal", adresse.getCodePostal());
        namedParameters.addValue("ville", adresse.getVille());

        jdbcTemplate.update(INSERT_ADRESSE, namedParameters, keyHolder);

        adresse.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public Adresse readByUtilisateur(long id){
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        return jdbcTemplate.queryForObject(SELECT_ADRESSE_BY_USER_ID, namedParameters, new AdresseMapper());

    }
    @Override
    public Adresse read(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        return jdbcTemplate.queryForObject(SELECT_ADRESSE_BY_ID, namedParameters, new AdresseMapper());
    }

    @Override
    public List<Adresse> readAllEni(){
        return jdbcTemplate.query(SELECT_ADRESSE_ENI, new AdresseMapper());
    }

    @Override
    public void update(Adresse adresse) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", adresse.getId());
        namedParameters.addValue("rue", adresse.getRue());
        namedParameters.addValue("codePostal", adresse.getCodePostal());
        namedParameters.addValue("ville", adresse.getVille());

        jdbcTemplate.update(UPDATE_ADRESSE, namedParameters);
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        jdbcTemplate.update(DELETE_ADRESSE_BY_ID, namedParameters);
    }


    static class AdresseMapper implements RowMapper<Adresse> {

        @Override
        public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
            Adresse adresse = new Adresse();
            adresse.setId(rs.getLong("no_adresse"));
            adresse.setRue(rs.getString("rue"));
            adresse.setCodePostal(rs.getString("code_postal"));
            adresse.setVille(rs.getString("ville"));
            return adresse;
        }
    }
}
