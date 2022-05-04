package sit.it.rvcomfort.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sit.it.rvcomfort.exception.list.BusinessException;
import sit.it.rvcomfort.exception.list.DuplicateDataException;
import sit.it.rvcomfort.exception.list.InvalidAttributeException;
import sit.it.rvcomfort.exception.list.NotFoundException;
import sit.it.rvcomfort.exception.response.ExceptionResponse;
import sit.it.rvcomfort.util.TimeUtils;

import static sit.it.rvcomfort.exception.response.ExceptionResponse.ERROR_CODE.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ExceptionHandlling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> exceptionsHandle(NotFoundException ex, WebRequest request){

        ExceptionResponse response = new ExceptionResponse(ex.getErrorCode(), ex.getErrorCode().getValue(), ex.getMessage(), TimeUtils.now());
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<Object> exceptionsHandle(DuplicateDataException ex, WebRequest request){

        ExceptionResponse response = new ExceptionResponse(ex.getErrorCode(), ex.getErrorCode().getValue(), ex.getMessage(), TimeUtils.now());
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(InvalidAttributeException.class)
    public ResponseEntity<Object> exceptionsHandle(InvalidAttributeException ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(ex.getErrorCode(), ex.getErrorCode().getValue(), ex.getMessage(), TimeUtils.now());
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> exceptionsHandle(BusinessException ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(ex.getErrorCode(), ex.getErrorCode().getValue(), ex.getMessage(), TimeUtils.now());
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionsHandle(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getValue(), ex.getMessage(), TimeUtils.now());
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
