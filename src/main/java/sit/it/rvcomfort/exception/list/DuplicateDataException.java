package sit.it.rvcomfort.exception.list;

import lombok.Data;
import sit.it.rvcomfort.exception.response.ExceptionResponse.ERROR_CODE;

@Data
public class DuplicateDataException extends RuntimeException {

    ERROR_CODE errorCode;

    public DuplicateDataException(ERROR_CODE errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
