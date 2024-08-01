package fr.eni.projet.eniencheres.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, Model model, RedirectAttributes redirectAttributes) {
        logger.error("General exception occurred: ", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Une erreur inattendue s'est produite. Merci de contacter le support si cela persiste.");
        return modelAndView;
    }


    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex, Model model) {
        logger.error("Runtime exception occurred: ", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Un problème est survenu. Merci de contacter le support si cela persiste.");
        return modelAndView;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        logger.error("IllegalArgumentException exception occurred: ", ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "La requête a échoué en raison d'une entrée invalide.");
        return modelAndView;
    }
}
