package operators;

/**
 * Eccezione personalizzata per livello di accesso non valido
 */
public class InvalidAccessLevelException extends Exception {

    public InvalidAccessLevelException(String message) {
        super(message); // passa il messaggio alla superclasse Exception
    }
}
