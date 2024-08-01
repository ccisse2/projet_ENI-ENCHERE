package fr.eni.projet.eniencheres.controller.converter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    private final DateTimeFormatter formatter;

    public LocalDateTimeFormatter() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    }

    @Override
    public LocalDateTime parse(String text, Locale locale) {
        return LocalDateTime.parse(text, formatter);
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return formatter.format(object);
    }
}
