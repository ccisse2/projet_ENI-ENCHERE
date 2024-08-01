package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Acquisition;
import fr.eni.projet.eniencheres.bo.Utilisateurs;

import java.util.List;

public interface AcquisitionService {
    List<Acquisition> trouverAcquisitionsParUtilisateur(Utilisateurs utilisateur);
    void ajouterAcquisition(Acquisition acquisition);

    void supprimerAcquisitionsParUtilisateur(Long utilisateurId);
}
