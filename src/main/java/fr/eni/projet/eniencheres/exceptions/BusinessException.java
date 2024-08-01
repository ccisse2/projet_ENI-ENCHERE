package fr.eni.projet.eniencheres.exceptions;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    // Liste des clefs d'externalisation
    private List<String> clefsExternalisations;

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public List<String> getClefsExternalisations() {
        return clefsExternalisations;
    }

    /**
     * Permet d'ajouter une clef d'erreur
     *
     * @param clef
     * @comportement initialise la liste si besoin
     */
    public void addError(String clef) {
        if (clefsExternalisations == null) {
            clefsExternalisations = new ArrayList<>();
        }
        clefsExternalisations.add(clef);
    }

    /**
     * @return permet de confirmer si des erreurs ont été chargées
     */
    public boolean isValid() {
        return clefsExternalisations == null || clefsExternalisations.isEmpty();
    }

}
