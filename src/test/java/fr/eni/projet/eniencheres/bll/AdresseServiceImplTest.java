package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Adresse;
import fr.eni.projet.eniencheres.dal.AdresseDao;
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

class AdresseServiceImplTest {

    @Mock
    private AdresseDao adresseDao;

    @InjectMocks
    private AdresseServiceImpl adresseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerAdresse() {
        Adresse adresse = new Adresse();
        adresse.setRue("123 Rue de Test");

        doNothing().when(adresseDao).create(any(Adresse.class));

        adresseService.creerAdresse(adresse);

        verify(adresseDao, times(1)).create(adresse);
    }

    @Test
    void chercherAdresseParId() {
        Adresse adresse = new Adresse();
        adresse.setId(1L);
        adresse.setRue("123 Rue de Test");

        when(adresseDao.read(1L)).thenReturn(adresse);

        Adresse result = adresseService.chercherAdresseParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("123 Rue de Test", result.getRue());
        verify(adresseDao, times(1)).read(1L);
    }

    @Test
    void modifierAdresse() {
        Adresse adresse = new Adresse();
        adresse.setId(1L);
        adresse.setRue("Updated Rue de Test");

        doNothing().when(adresseDao).update(any(Adresse.class));

        adresseService.modifierAdresse(adresse);

        verify(adresseDao, times(1)).update(adresse);
    }

    @Test
    void supprimerAdresse() {
        doNothing().when(adresseDao).delete(1L);

        adresseService.supprimerAdresse(1L);

        verify(adresseDao, times(1)).delete(1L);
    }
}
