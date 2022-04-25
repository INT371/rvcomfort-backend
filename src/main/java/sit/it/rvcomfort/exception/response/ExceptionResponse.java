package sit.it.rvcomfort.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    public static enum ERROR_CODE {
        // General exception 1xxxx

        // Room exception 2xxxx
        ROOM_INVALID_ATTROBUTE(20000),
        ROOM_NOT_FOUND(20001),

        // RoomType exception 3xxxx
        ROOM_TYPE_INVALID_ATTRIBUTE(30000),
        ROOM_TYPE_NOT_FOUND(30001)


        ;

        private int value;

        ERROR_CODE(int value) {
            this.value = value;
        }
    }

    private ERROR_CODE errorCode;
    private String message;
    private ZonedDateTime dateTime;

}
