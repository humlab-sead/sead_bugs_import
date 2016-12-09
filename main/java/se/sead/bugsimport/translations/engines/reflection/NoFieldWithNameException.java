package se.sead.bugsimport.translations.engines.reflection;

public class NoFieldWithNameException extends IllegalArgumentException {

    public NoFieldWithNameException() {
    }

    public NoFieldWithNameException(String s) {
        super(s);
    }

    public NoFieldWithNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFieldWithNameException(Throwable cause) {
        super(cause);
    }
}
