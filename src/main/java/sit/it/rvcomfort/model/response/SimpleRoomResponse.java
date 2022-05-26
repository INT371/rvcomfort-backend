package sit.it.rvcomfort.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sit.it.rvcomfort.model.BaseResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRoomResponse implements BaseResponse {

    @JsonProperty("room_id")
    private Integer roomId;

    @JsonProperty("room_name")
    private String roomName;
}
