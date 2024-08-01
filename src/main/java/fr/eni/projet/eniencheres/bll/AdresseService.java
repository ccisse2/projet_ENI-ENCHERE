package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Adresse;

import java.util.List;

public interface AdresseService {
    void creerAdresse(Adresse adresse);

    Adresse chercherAdresseParId(long id);

    Adresse chercherAdresseParUtilisateur(long id);

    List<Adresse> chercherToutesLesAdressesEni();

    void modifierAdresse(Adresse adresse);

    void supprimerAdresse(long id);
}