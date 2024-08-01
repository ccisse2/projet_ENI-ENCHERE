package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Adresse;
import fr.eni.projet.eniencheres.dal.AdresseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdresseServiceImpl implements AdresseService {

    private AdresseDao adresseDao;

    @Autowired
    public AdresseServiceImpl(AdresseDao adresseDao) {
        this.adresseDao = adresseDao;
    }

    @Override
    public void creerAdresse(Adresse adresse) {
        adresseDao.create(adresse);
    }

    @Override
    public Adresse chercherAdresseParId(long id) {
        return adresseDao.read(id);
    }

    @Override
    public Adresse chercherAdresseParUtilisateur(long id) {
        return adresseDao.readByUtilisateur(id);
    }

    @Override
    public List<Adresse> chercherToutesLesAdressesEni() {
        return adresseDao.readAllEni();
    }

    @Override
    public void modifierAdresse(Adresse adresse) {
        adresseDao.update(adresse);
    }

    @Override
    public void supprimerAdresse(long id) {
        adresseDao.delete(id);
    }
}
