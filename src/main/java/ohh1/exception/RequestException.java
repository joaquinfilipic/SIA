package ohh1.exception;

import org.springframework.http.HttpStatus;

/**
 * General application exception thrown whenever a not OK response must be returned.
 */
public class RequestException extends RuntimeException {

	/**
     * HTTP status of the response.
     */
    private final HttpStatus status;

    /**
     * Body of the response.
     */
    private final Object body;

    public RequestException(HttpStatus status, Object body) {
        super();
        this.status = status;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Object getBody() {
        return body;
    }
}
