package se.sead.utils.errorlog;

import java.text.MessageFormat;

public class ExceptionErrorLog implements ErrorLog {

    private static final String MESSAGE_TEMPLATE = "Exception: {0} -- {1}";
    private static final String TRACE_TEMPLATE = "{0}: {1}";

    private Exception exception;

    public ExceptionErrorLog(Exception exception){
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format(
                MESSAGE_TEMPLATE,
                exception.getMessage(),
                getFirstSetInternalClassesTrace()
        );
    }

    private String getFirstSetInternalClassesTrace(){
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement traceElement :
                exception.getStackTrace()) {
                if(traceElement.getClassName().startsWith("se.sead")){
                    stackTrace.append(
                            MessageFormat.format(
                                    TRACE_TEMPLATE,
                                    traceElement.getClassName(),
                                    traceElement.getLineNumber()
                            )
                    );
                }
        }
        return stackTrace.toString();
    }
}
