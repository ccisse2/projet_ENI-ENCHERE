package fr.eni.projet.eniencheres.configuration;

import fr.eni.projet.eniencheres.controller.converter.StringToAdresseConverter;
import fr.eni.projet.eniencheres.controller.converter.StringToCategorieConverter;
import fr.eni.projet.eniencheres.controller.converter.LocalDateTimeFormatter;
import fr.eni.projet.eniencheres.controller.converter.StringToUtilisateurConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final StringToAdresseConverter stringToAdresseConverter;
    private final StringToCategorieConverter stringToCategorieConverter;
    private final LocalDateTimeFormatter localDateTimeFormatter;
    private final StringToUtilisateurConverter stringToUtilisateurConverter;


    public WebConfig(StringToAdresseConverter stringToAdresseConverter, StringToCategorieConverter stringToCategorieConverter, LocalDateTimeFormatter localDateTimeFormatter, StringToUtilisateurConverter stringToUtilisateurConverter) {
        this.stringToAdresseConverter = stringToAdresseConverter;
        this.stringToCategorieConverter = stringToCategorieConverter;
        this.localDateTimeFormatter = localDateTimeFormatter;
        this.stringToUtilisateurConverter = stringToUtilisateurConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToAdresseConverter);
        registry.addConverter(stringToCategorieConverter);
        registry.addFormatter(localDateTimeFormatter);
        registry.addConverter(stringToUtilisateurConverter);
    }
    @Bean
    LocaleResolver localeResolver() {
        System.out.println("localeResolver");
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("fr"));
        return slr;
    }
    @Bean
    LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}

