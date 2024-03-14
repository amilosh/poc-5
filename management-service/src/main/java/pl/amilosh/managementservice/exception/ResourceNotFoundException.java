package pl.amilosh.managementservice.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String format, Object... args) {
        super(String.format(format, args));
    }
}
