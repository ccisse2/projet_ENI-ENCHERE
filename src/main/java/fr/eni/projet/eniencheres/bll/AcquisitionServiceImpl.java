package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Acquisition;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import fr.eni.projet.eniencheres.dal.AcquisitionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcquisitionServiceImpl implements AcquisitionService  {

    private final AcquisitionDao acquisitionDao;

    @Autowired
    public AcquisitionServiceImpl(AcquisitionDao acquisitionDao) {
        this.acquisitionDao = acquisitionDao;
    }

    @Override
    public void ajouterAcquisition(Acquisition acquisition) {
        acquisitionDao.save(acquisition);
    }

    @Override
    public List<Acquisition> trouverAcquisitionsParUtilisateur(Utilisateurs utilisateur) {
        return acquisitionDao.findByUtilisateur(utilisateur);
    }

    @Override
    public void supprimerAcquisitionsParUtilisateur(Long utilisateurId) {
        acquisitionDao.deleteByUtilisateurId(utilisateurId);
    }
}

