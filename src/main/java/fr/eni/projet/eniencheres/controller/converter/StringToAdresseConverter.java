package fr.eni.projet.eniencheres.controller.converter;

import fr.eni.projet.eniencheres.bll.AdresseService;
import fr.eni.projet.eniencheres.bo.Adresse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAdresseConverter implements Converter<String, Adresse> {
	private final AdresseService adresseService;

	public StringToAdresseConverter(AdresseService adresseService) {
		this.adresseService = adresseService;
	}

	@Override
	public Adresse convert(String id) {
		Long theId = Long.parseLong(id);
		return adresseService.chercherAdresseParId(theId); // or adresseService.chercherAdresseParUtilisateur(theId) depending on your requirement
	}
}
