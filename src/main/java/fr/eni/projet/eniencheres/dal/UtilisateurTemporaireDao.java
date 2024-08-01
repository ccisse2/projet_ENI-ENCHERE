package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.UtilisateurTemporaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurTemporaireDao extends JpaRepository<UtilisateurTemporaire, Long> {
    UtilisateurTemporaire findByTokenVerification(String token);
}
