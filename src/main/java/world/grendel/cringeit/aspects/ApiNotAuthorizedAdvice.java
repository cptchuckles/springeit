package world.grendel.cringeit.aspects;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import world.grendel.cringeit.exceptions.ApiNotAuthorizedException;

/**
 * ApiNotAuthorizedAdvice
 */
@ControllerAdvice
public class ApiNotAuthorizedAdvice {
    @ResponseBody
    @ExceptionHandler(ApiNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String apiNotAuthorizedHandler(ApiNotAuthorizedException ex) {
        return ex.getMessage();
    }
}
