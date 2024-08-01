package fr.eni.projet.eniencheres.controller.converter;

import fr.eni.projet.eniencheres.bll.UtilisateursService;
import fr.eni.projet.eniencheres.bo.Utilisateurs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUtilisateurConverter implements Converter<String, Utilisateurs> {

    private final UtilisateursService utilisateursService;

    @Autowired
    public StringToUtilisateurConverter(UtilisateursService utilisateursService) {
        this.utilisateursService = utilisateursService;
    }

    @Override
    public Utilisateurs convert(String source) {
        return utilisateursService.charger(source);
    }
}
