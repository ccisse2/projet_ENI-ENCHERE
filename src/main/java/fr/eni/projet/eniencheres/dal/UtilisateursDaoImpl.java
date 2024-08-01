package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.Adresse;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
public class UtilisateursDaoImpl implements UtilisateursDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AdresseDao adresseDao;

    @Autowired
    public UtilisateursDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, AdresseDao adresseDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.adresseDao = adresseDao;
    }

    private static final String INSERT_USER = "INSERT INTO utilisateurs (pseudo, nom, prenom, email, telephone, " +
            "mot_de_passe, no_adresse) " +
            "VALUES (:pseudo, :nom, :prenom, :email, :telephone, :motDePasse, :adresse_id)";
    private static final String SELECT_USER_BY_PSEUDO = "SELECT * FROM utilisateurs WHERE pseudo = :pseudo";
    private static final String UPDATE_USER = "UPDATE utilisateurs SET  pseudo = :pseudo, nom = :nom, prenom = :prenom, email = :email, " +
            "telephone = :telephone, credit = :credit, actif = :actif, reset_password_token = :resetPasswordToken, token_expiry_date = :expiryDate" +
            " WHERE id = :id ";

    private static final String SELECT_USER_BY_ID = "SELECT * FROM utilisateurs WHERE id = :id";
    private static final String SELECT_USER_BY_EMAIL= "SELECT * FROM utilisateurs WHERE email = :email";
    private static final String DELETE_USER_BY_PSEUDO = "DELETE FROM utilisateurs WHERE pseudo = :pseudo";
    private static final String DELETE_USER_BY_ID = "DELETE FROM utilisateurs WHERE id = :id";
    private static final String SELECT_ALL_USERS = "SELECT * FROM utilisateurs";
    private static final String SELECT_ADRESSE_BY_USER_ID = "SELECT a.no_adresse, a.rue, a.code_postal, a.ville, a.adresse_eni " +
            "FROM adresses a " +
            "JOIN utilisateurs u ON a.no_adresse = u.no_adresse " +
            "WHERE u.id = :id";
    private static final String UPDATE_MOT_DE_PASSE = "UPDATE utilisateurs SET mot_de_passe = :motDePasse WHERE pseudo = :pseudo";




    @Override
    public void create(Utilisateurs utilisateurs) {
        Adresse adresse = utilisateurs.getAdresse();

        // Créer l'adresse d'abord
        adresseDao.create(adresse);

        // Utiliser l'ID généré pour l'adresse
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", utilisateurs.getPseudo());
        namedParameters.addValue("nom", utilisateurs.getNom());
        namedParameters.addValue("prenom", utilisateurs.getPrenom());
        namedParameters.addValue("email", utilisateurs.getEmail());
        namedParameters.addValue("telephone", utilisateurs.getTelephone());
        namedParameters.addValue("motDePasse", utilisateurs.getMotDePasse());
        namedParameters.addValue("adresse_id", adresse.getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_USER, namedParameters, keyHolder);

        // Optionnel : récupérer l'identifiant généré pour l'utilisateur
        utilisateurs.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public Utilisateurs readByPseudo(String pseudo) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", pseudo);

        Utilisateurs utilisateur = jdbcTemplate.queryForObject(SELECT_USER_BY_PSEUDO, namedParameters, new UtilisateurMapper());
        if (utilisateur != null) {
            Adresse adresse = adresseDao.readByUtilisateur(utilisateur.getId());
            utilisateur.setAdresse(adresse);
            return utilisateur;
        }
        return null;
    }

    @Override
    public Utilisateurs readById(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        Utilisateurs utilisateur = jdbcTemplate.queryForObject(SELECT_USER_BY_ID, namedParameters, new UtilisateurMapper());
        if (utilisateur != null) {
            Adresse adresse = adresseDao.readByUtilisateur(utilisateur.getId());
            utilisateur.setAdresse(adresse);
            return utilisateur;
        }
        return null;
    }

    @Override
    public Adresse readByUtilisateur(long id){
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        return jdbcTemplate.queryForObject(SELECT_ADRESSE_BY_USER_ID, namedParameters, new AdresseMapper());
    }

    @Override
    public void update(Utilisateurs utilisateurs) {
        // Mettre à jour l'adresse d'abord
        adresseDao.update(utilisateurs.getAdresse());

        // Mettre à jour l'utilisateur
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", utilisateurs.getPseudo());
        namedParameters.addValue("nom", utilisateurs.getNom());
        namedParameters.addValue("prenom", utilisateurs.getPrenom());
        namedParameters.addValue("email", utilisateurs.getEmail());
        namedParameters.addValue("telephone", utilisateurs.getTelephone());
        namedParameters.addValue("motDePasse", utilisateurs.getMotDePasse());
        namedParameters.addValue("credit", utilisateurs.getCredit());
        namedParameters.addValue("actif", utilisateurs.isActif());
        namedParameters.addValue("resetPasswordToken", utilisateurs.getResetPasswordToken());
        if (utilisateurs.getTokenExpiryDate() != null) {
            Timestamp expiryTimestamp = Timestamp.valueOf(utilisateurs.getTokenExpiryDate());
            namedParameters.addValue("expiryDate", expiryTimestamp);
            System.out.println("Setting expiry date: " + expiryTimestamp);
        } else {
            namedParameters.addValue("expiryDate", null);
            System.out.println("Expiry date is null");
        }
        namedParameters.addValue("id", utilisateurs.getId());

        jdbcTemplate.update(UPDATE_USER, namedParameters);
    }


    @Override
    public void updateMotDePasse(String pseudo, String motDePasse) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", pseudo);
        namedParameters.addValue("motDePasse", motDePasse);
        jdbcTemplate.update(UPDATE_MOT_DE_PASSE, namedParameters);
    }

    @Override
    public void delete(String pseudo) {
        Utilisateurs utilisateurs = readByPseudo(pseudo);
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", pseudo);

        jdbcTemplate.update(DELETE_USER_BY_PSEUDO, namedParameters);

        // Supprimer l'adresse
        adresseDao.delete(utilisateurs.getAdresse().getId());
    }


    @Override
    public void deleteById(Long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);

        jdbcTemplate.update(DELETE_USER_BY_ID, namedParameters);

        // Supprimer l'adresse
        adresseDao.delete(readById(id).getAdresse().getId());
    }

    @Override
    public Utilisateurs findByEmail(String email) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("email", email);

        return jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL, namedParameters, new UtilisateurMapper());
    }

    @Override
    public List<Utilisateurs> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS, new UtilisateurMapper());
    }

    @Override
    public Utilisateurs findByToken(String token) {
        String sql = "SELECT * FROM utilisateurs WHERE reset_password_token = :token";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("token", token);

        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, new UtilisateurMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void save(Utilisateurs utilisateur) {
        String sql = "UPDATE utilisateurs SET reset_password_token = :token, token_expiry_date = :expiryDate " +
                "WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("token", utilisateur.getResetPasswordToken());
        namedParameters.addValue("expiryDate", utilisateur.getTokenExpiryDate());
        namedParameters.addValue("id", utilisateur.getId());

        jdbcTemplate.update(sql, namedParameters);
    }

    private class UtilisateurMapper implements RowMapper<Utilisateurs> {
        @Override
        public Utilisateurs mapRow(ResultSet rs, int rowNum) throws SQLException {
            Utilisateurs utilisateurs = new Utilisateurs();
            utilisateurs.setId(rs.getLong("id"));
            utilisateurs.setPseudo(rs.getString("pseudo"));
            utilisateurs.setNom(rs.getString("nom"));
            utilisateurs.setPrenom(rs.getString("prenom"));
            utilisateurs.setEmail(rs.getString("email"));
            utilisateurs.setTelephone(rs.getString("telephone"));
            utilisateurs.setMotDePasse(rs.getString("mot_de_passe"));
            utilisateurs.setCredit(rs.getBigDecimal("credit"));
            utilisateurs.setAdmin(rs.getBoolean("administrateur"));
            utilisateurs.setActif(rs.getBoolean("actif"));
            utilisateurs.setResetPasswordToken(rs.getString("reset_password_token"));
            Adresse adresse = adresseDao.read(rs.getLong("no_adresse"));
            utilisateurs.setAdresse(adresse);
            return utilisateurs;
        }
    }

    public static class AdresseMapper implements RowMapper<Adresse> {
        @Override
        public Adresse mapRow(ResultSet rs, int rowNum) throws SQLException {
            Adresse adresse = new Adresse();
            adresse.setId(rs.getLong("no_adresse"));
            adresse.setRue(rs.getString("rue"));
            adresse.setCodePostal(rs.getString("code_postal"));
            adresse.setVille(rs.getString("ville"));
            adresse.setAdresseEni(rs.getBoolean("adresse_eni"));
            return adresse;
        }
    }

}

