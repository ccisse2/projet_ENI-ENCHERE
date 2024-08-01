package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Categorie;
import fr.eni.projet.eniencheres.dal.CategorieDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieServiceImpl implements CategorieService {

    private CategorieDao categorieDao;

    @Autowired
    public CategorieServiceImpl(CategorieDao categorieDao) {
        this.categorieDao = categorieDao;
    }

    @Override
    public void creerCategorie(Categorie categorie) {
        categorieDao.create(categorie);
    }

    @Override
    public Categorie chercherCategorie(Long id) {
        return categorieDao.read(id);
    }

    @Override
    public void modifierCategorie(Categorie categorie) {
        categorieDao.update(categorie);
    }

    @Override
    public void supprimerCategorie(Long id) {
        categorieDao.delete(id);
    }

    @Override
    public List<Categorie> recupererToutesLesCategories() {
        return categorieDao.findAll();
    }
}
