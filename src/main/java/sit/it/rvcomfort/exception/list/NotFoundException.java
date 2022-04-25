package sit.it.rvcomfort.exception.list;

import lombok.Data;
import sit.it.rvcomfort.exception.response.ExceptionResponse.ERROR_CODE;

@Data
public class NotFoundException extends RuntimeException {

    ERROR_CODE errorCode;

    public NotFoundException(ERROR_CODE errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
