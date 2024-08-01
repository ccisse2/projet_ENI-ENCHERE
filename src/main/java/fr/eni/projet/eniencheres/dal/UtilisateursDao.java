package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.Adresse;
import fr.eni.projet.eniencheres.bo.Utilisateurs;

import java.util.List;

public interface UtilisateursDao {
    void create(Utilisateurs utilisateurs);

    Utilisateurs readByPseudo(String pseudo);

    Utilisateurs readById(Long id);

    Adresse readByUtilisateur(long id);

    void update(Utilisateurs utilisateurs);

    void updateMotDePasse(String pseudo, String motDePasse);

    void delete(String pseudo);

    void deleteById(Long id);

    Utilisateurs findByEmail(String email);

    List<Utilisateurs> findAll();

    Utilisateurs findByToken(String token);

    void save(Utilisateurs utilisateur);
}
