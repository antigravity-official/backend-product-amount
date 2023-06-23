package antigravity.exceptions;

public class OmittedRequireFieldException extends RuntimeException{

    public OmittedRequireFieldException(String msg, Throwable t) {
        super(msg, t);
    }

    public OmittedRequireFieldException(String msg) {
        super(msg);
    }

    public OmittedRequireFieldException() {
    }

}
