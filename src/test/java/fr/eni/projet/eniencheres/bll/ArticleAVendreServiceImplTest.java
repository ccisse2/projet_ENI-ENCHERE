package fr.eni.projet.eniencheres.bll;

import fr.eni.projet.eniencheres.bo.ArticleAVendre;
import fr.eni.projet.eniencheres.dal.ArticleAVendreDao;
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

class ArticleAVendreServiceImplTest {

    @Mock
    private ArticleAVendreDao articleDao;

    @InjectMocks
    private ArticleAVendreServiceImpl articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creerArticleAVendre() {
        ArticleAVendre article = new ArticleAVendre();
        article.setNomArticle("Test Article");

        doNothing().when(articleDao).create(any(ArticleAVendre.class));

        articleService.creerArticleAVendre(article);

        verify(articleDao, times(1)).create(article);
    }

    @Test
    void getArticleAVendreById() {
        ArticleAVendre article = new ArticleAVendre();
        article.setId(1L);
        article.setNomArticle("Test Article");

        when(articleDao.read(1L)).thenReturn(article);

        ArticleAVendre result = articleService.getArticleAVendreById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Article", result.getNomArticle());
        verify(articleDao, times(1)).read(1L);
    }

    @Test
    void modifierArticleAVendre() {
        ArticleAVendre article = new ArticleAVendre();
        article.setId(1L);
        article.setNomArticle("Updated Article");

        doNothing().when(articleDao).update(any(ArticleAVendre.class));

        articleService.modifierArticleAVendre(article);

        verify(articleDao, times(1)).update(article);
    }

    @Test
    void supprimerArticleAVendre() {
        doNothing().when(articleDao).delete(1L);

        articleService.supprimerArticleAVendre(1L);

        verify(articleDao, times(1)).delete(1L);
    }

    @Test
    void toutLesArticlesAVendre() {
        ArticleAVendre article1 = new ArticleAVendre();
        article1.setId(1L);
        article1.setNomArticle("Article 1");

        ArticleAVendre article2 = new ArticleAVendre();
        article2.setId(2L);
        article2.setNomArticle("Article 2");

        List<ArticleAVendre> articles = Arrays.asList(article1, article2);

        when(articleDao.findAll()).thenReturn(articles);

        List<ArticleAVendre> result = articleService.toutLesArticlesAVendre();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(articleDao, times(1)).findAll();
    }
}
