package sit.it.rvcomfort.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    @Getter
    public static enum ERROR_CODE {
        // General exception 0xxxx
        INTERNAL_SERVER_ERROR(00000),

        // User exception 1xxxx
        USER_NOT_FOUND(10001),
        DUPLICATE_USERNAME(10002),
        DUPLICATE_EMAIL(10003),
        DUPLICATE_FULL_NAME(10004),

        // Room exception 2xxxx
        ROOM_INVALID_ATTRIBUTE(20000),
        ROOM_NOT_FOUND(20001),
        DUPLICATE_ROOM_NAME(20002),

        // RoomType exception 3xxxx
        ROOM_TYPE_INVALID_ATTRIBUTE(30000),
        ROOM_TYPE_NOT_FOUND(30001),
        DUPLICATE_ROOM_TYPE(30002),

        // Reservation exception 4xxxx
        RESERVATION_INVALID_ATTRIBUTE(40000),
        RESERVATION_NOT_FOUND(40001),
        RESERVATION_DATE_TOO_CLOSE(40002),
        OVERLAPPED_RESERVATION_TIME(40003),

        ;


        private int value;

        ERROR_CODE(int value) {
            this.value = value;
        }
    }

    private ERROR_CODE errorCode;
    private int errorCodeValue;
    private String message;
    private ZonedDateTime dateTime;

}
