package fr.eni.projet.eniencheres.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CustomErrorController implements ErrorController  {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        String errorMessage = "Erreur inconnue";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 400:
                    errorMessage = "Erreur 400 - Requête incorrecte";
                    break;
                case 401:
                    errorMessage = "Erreur 401 - Non autorisé";
                    break;
                case 403:
                    errorMessage = "Erreur 403 - Interdit";
                    break;
                case 404:
                    errorMessage = "Erreur 404 - Non trouvé";
                    break;
                case 500:
                    errorMessage = "Erreur 500 - Erreur interne du serveur";
                    break;
                default:
                    errorMessage = "Erreur " + statusCode;
                    break;
            }
        }

        model.addAttribute("error", errorMessage);
        return "error";
    }
    public String getErrorPath() {
        return "/error";
    }
}
