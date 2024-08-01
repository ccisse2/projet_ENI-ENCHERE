package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.Enchere;
import fr.eni.projet.eniencheres.dal.ArticleAVendreDao;
import fr.eni.projet.eniencheres.dal.EnchereDao;
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

class EnchereServiceImplTest {

    @Mock
    private EnchereDao enchereDao;

    @Mock
    private ArticleAVendreDao articleAVendreDao;

    @InjectMocks
    private EnchereServiceImpl enchereService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerEnchere() {
        Enchere enchere = new Enchere();
        enchere.setMontantEnchere(100);

        doNothing().when(enchereDao).create(any(Enchere.class));

        enchereService.creerEnchere(enchere);

        verify(enchereDao, times(1)).create(enchere);
    }

    @Test
    void supprimerEnchere() {
        Enchere enchere = new Enchere();
        enchere.setMontantEnchere(100);

        doNothing().when(enchereDao).delete(any(Enchere.class));

        enchereService.supprimerEnchere(enchere);

        verify(enchereDao, times(1)).delete(enchere);
    }

    @Test
    void rechercherEnchereParArticleId() {
        Enchere enchere = new Enchere();
        enchere.setMontantEnchere(100);

        when(enchereDao.findByArticleId(1)).thenReturn(enchere);

        Enchere result = enchereService.rechercherEnchereParArticleId(1);

        assertNotNull(result);
        assertEquals(100, result.getMontantEnchere());
        verify(enchereDao, times(1)).findByArticleId(1);
    }

    @Test
    void modifierEnchere() {
        Enchere enchere = new Enchere();
        enchere.setMontantEnchere(100);

        doNothing().when(enchereDao).update(any(Enchere.class));

        enchereService.modifierEnchere(enchere);

        verify(enchereDao, times(1)).update(enchere);
    }
}
