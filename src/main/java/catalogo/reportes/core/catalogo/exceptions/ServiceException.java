package catalogo.reportes.core.catalogo.exceptions;

public class ServiceException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 6238063933362747784L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
}
