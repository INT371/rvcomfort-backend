package sit.it.rvcomfort.exception.list;

import lombok.Data;
import sit.it.rvcomfort.exception.response.ExceptionResponse.ERROR_CODE;

@Data
public class InvalidAttributeException extends RuntimeException {

    ERROR_CODE errorCode;

    public InvalidAttributeException(ERROR_CODE errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
