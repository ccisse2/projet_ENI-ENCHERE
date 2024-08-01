package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.Categorie;

import java.util.List;

public interface CategorieDao {
    // CRUD operations
    void create(Categorie categorie);

    Categorie read(long id);

    void update(Categorie categorie);

    void delete(long id);

    List<Categorie> findAll();
}
