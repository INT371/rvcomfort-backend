package sit.it.rvcomfort.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sit.it.rvcomfort.exception.list.DuplicateDataException;
import sit.it.rvcomfort.exception.list.NotFoundException;
import sit.it.rvcomfort.exception.response.ExceptionResponse;
import sit.it.rvcomfort.util.TimeUtils;

@ControllerAdvice
public class ExceptionHandlling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> exceptionsHandle(NotFoundException ex, WebRequest request){

        ExceptionResponse response = new ExceptionResponse(ex.getErrorCode(), ex.getMessage(), TimeUtils.now());
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<Object> exceptionsHandle(DuplicateDataException ex, WebRequest request){

        ExceptionResponse response = new ExceptionResponse(ex.getErrorCode(), ex.getMessage(), TimeUtils.now());
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

}
