package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Adresse;
import fr.eni.projet.eniencheres.bo.Utilisateurs;

import java.util.List;

public interface UtilisateursService {

    void creerUtilisateur(Utilisateurs utilisateur);

    Utilisateurs recupererUtilisateurParPseudo(String pseudo);

    void mettreAJourUtilisateur(Utilisateurs utilisateur);

    void mettreAJourMotDePasse(String pseudo, String nouveauMotDePasse);

    void supprimerUtilisateur(String pseudo);

    void supprimerUtilisateur(Long utilisateurId);

    List<Utilisateurs> recupererTousLesUtilisateurs();

    void creerAdresse(Adresse adresse);

    Utilisateurs charger(String pseudo);

    boolean verifierMotDePasse(String ancienMotDePasse, String motDePasseEncode);

    void anonymiserUtilisateur(Long utilisateurId);

    void desactiverUtilisateur(Long id) throws Exception;

    void annulerEncheresEtArticles(String pseudo);

    void creerTokenDeReinitialisation(String email);

    boolean verifierToken(String token);

    void reinitialiserMotDePasse(String token, String nouveauMotDePasse);

    void creerUtilisateurTemporaire(Utilisateurs utilisateur, String token);

    void validerEmailUtilisateurTemporaire(String token);
}
