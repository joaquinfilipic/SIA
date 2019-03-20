package ohh1.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handles application-wide thrown exceptions.
 */
@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles a RequestException thrown anywhere in the application by setting
     * an HTTP status code and the response body in JSON format.
     *
     * @param ex      thrown exception to be handled
     * @param request HTTP request
     * @return HTTP response
     * @throws JsonProcessingException if the body couldn't be encoded in JSON format
     */
    @ExceptionHandler({RequestException.class})
    public ResponseEntity<Object> handleResourceException(RequestException ex, WebRequest request)
            throws JsonProcessingException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(
                ex,
                getJSONString(ex.getBody()),
                headers,
                ex.getStatus(),
                request
        );
    }

    /**
     * Returns the object encoded in JSON format or
     * an empty string if the encoding failed.
     *
     * @param object object to be encoded
     * @return JSON string of the encoded object
     */
    private String getJSONString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
