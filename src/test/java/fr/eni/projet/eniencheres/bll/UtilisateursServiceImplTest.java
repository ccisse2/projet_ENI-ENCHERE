package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Utilisateurs;
import fr.eni.projet.eniencheres.dal.UtilisateursDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UtilisateursServiceImplTest {

    @Mock
    private UtilisateursDao utilisateursDao;

    @InjectMocks
    private UtilisateursServiceImpl utilisateursService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerUtilisateur() {
        Utilisateurs utilisateur = new Utilisateurs();
        utilisateur.setPseudo("testuser");

        doNothing().when(utilisateursDao).create(any(Utilisateurs.class));

        utilisateursService.creerUtilisateur(utilisateur);

        verify(utilisateursDao, times(1)).create(utilisateur);
    }

    @Test
    void recupererUtilisateurParPseudo() {
        Utilisateurs utilisateur = new Utilisateurs();
        utilisateur.setPseudo("testuser");

        when(utilisateursDao.readByPseudo("testuser")).thenReturn(utilisateur);

        Utilisateurs result = utilisateursService.recupererUtilisateurParPseudo("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getPseudo());
        verify(utilisateursDao, times(1)).readByPseudo("testuser");
    }

    @Test
    void mettreAJourUtilisateur() {
        Utilisateurs utilisateur = new Utilisateurs();
        utilisateur.setPseudo("testuser");

        doNothing().when(utilisateursDao).update(any(Utilisateurs.class));

        utilisateursService.mettreAJourUtilisateur(utilisateur);

        verify(utilisateursDao, times(1)).update(utilisateur);
    }

    @Test
    void supprimerUtilisateur() {
        doNothing().when(utilisateursDao).delete("testuser");

        utilisateursService.supprimerUtilisateur("testuser");

        verify(utilisateursDao, times(1)).delete("testuser");
    }

    @Test
    void recupererTousLesUtilisateurs() {
        Utilisateurs utilisateur1 = new Utilisateurs();
        utilisateur1.setPseudo("user1");

        Utilisateurs utilisateur2 = new Utilisateurs();
        utilisateur2.setPseudo("user2");

        List<Utilisateurs> utilisateursList = Arrays.asList(utilisateur1, utilisateur2);

        when(utilisateursDao.findAll()).thenReturn(utilisateursList);

        List<Utilisateurs> result = utilisateursService.recupererTousLesUtilisateurs();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(utilisateursDao, times(1)).findAll();
    }
}
