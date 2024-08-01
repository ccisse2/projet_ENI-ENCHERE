package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Categorie;
import fr.eni.projet.eniencheres.dal.CategorieDao;
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

class CategorieServiceImplTest {

    @Mock
    private CategorieDao categorieDao;

    @InjectMocks
    private CategorieServiceImpl categorieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerCategorie() {
        Categorie categorie = new Categorie();
        categorie.setLibelle("TestCategorie");

        doNothing().when(categorieDao).create(any(Categorie.class));

        categorieService.creerCategorie(categorie);

        verify(categorieDao, times(1)).create(categorie);
    }

    @Test
    void chercherCategorie() {
        Categorie categorie = new Categorie();
        categorie.setId(1L);
        categorie.setLibelle("TestCategorie");

        when(categorieDao.read(1L)).thenReturn(categorie);

        Categorie result = categorieService.chercherCategorie(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TestCategorie", result.getLibelle());
        verify(categorieDao, times(1)).read(1L);
    }

    @Test
    void modifierCategorie() {
        Categorie categorie = new Categorie();
        categorie.setId(1L);
        categorie.setLibelle("UpdatedCategorie");

        doNothing().when(categorieDao).update(any(Categorie.class));

        categorieService.modifierCategorie(categorie);

        verify(categorieDao, times(1)).update(categorie);
    }

    @Test
    void supprimerCategorie() {
        doNothing().when(categorieDao).delete(1L);

        categorieService.supprimerCategorie(1L);

        verify(categorieDao, times(1)).delete(1L);
    }

    @Test
    void recupererToutesLesCategories() {
        Categorie categorie1 = new Categorie();
        categorie1.setId(1L);
        categorie1.setLibelle("Categorie1");

        Categorie categorie2 = new Categorie();
        categorie2.setId(2L);
        categorie2.setLibelle("Categorie2");

        List<Categorie> categories = Arrays.asList(categorie1, categorie2);

        when(categorieDao.findAll()).thenReturn(categories);

        List<Categorie> result = categorieService.recupererToutesLesCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categorieDao, times(1)).findAll();
    }
}
