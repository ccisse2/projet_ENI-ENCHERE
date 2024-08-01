package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.Adresse;

import java.util.List;

public interface AdresseDao {
    void create(Adresse adresse);

    Adresse readByUtilisateur(long id);

    Adresse read(long id);

    List<Adresse> readAllEni();

    void update(Adresse adresse);

    void delete(long id);

}
