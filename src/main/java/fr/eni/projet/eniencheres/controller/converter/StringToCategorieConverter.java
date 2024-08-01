package fr.eni.projet.eniencheres.controller.converter;

import fr.eni.projet.eniencheres.bll.CategorieService;
import fr.eni.projet.eniencheres.bo.Categorie;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCategorieConverter implements Converter<String, Categorie> {
    private final CategorieService categorieService;

    public StringToCategorieConverter(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @Override
    public Categorie convert(String id) {
        Long theId = Long.parseLong(id);
        return categorieService.chercherCategorie(theId);
    }
}