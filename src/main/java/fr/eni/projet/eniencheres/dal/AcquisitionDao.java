package fr.eni.projet.eniencheres.dal;

import fr.eni.projet.eniencheres.bo.Acquisition;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AcquisitionDao extends JpaRepository<Acquisition, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Acquisition a WHERE a.utilisateur.id = :utilisateurId")
    void deleteByUtilisateurId(@Param("utilisateurId") Long utilisateurId);

    List<Acquisition> findByUtilisateur(Utilisateurs utilisateur);
}
