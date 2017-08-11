package se.sead.utils.errorlog;

public class IgnoredItemErrorLog extends SingleMessageErrorLog {

    public IgnoredItemErrorLog(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "IGNORED: " + super.getMessage();
    }
}
