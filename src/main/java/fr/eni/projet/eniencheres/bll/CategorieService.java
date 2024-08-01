package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Categorie;

import java.util.List;

public interface CategorieService {
    void creerCategorie(Categorie categorie);

    Categorie chercherCategorie(Long id);

    void modifierCategorie(Categorie categorie);

    void supprimerCategorie(Long id);

    List<Categorie> recupererToutesLesCategories();
}
