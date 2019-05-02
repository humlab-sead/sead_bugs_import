package se.sead.utils.errorlog;

public class SingleMessageErrorLog implements ErrorLog {

    private String message;

    public SingleMessageErrorLog(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
