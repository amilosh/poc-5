package pl.amilosh.managementservice.exception;

public class ConversionException extends RuntimeException {

    public ConversionException() {
        super();
    }

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String format, Object... args) {
        super(String.format(format, args));
    }
}
