package sit.it.rvcomfort.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse implements Serializable {

    private Integer roomId;

    @JsonProperty("room_name")
    private String roomName;

    @JsonProperty("room_type")
    private RoomTypeResponse roomType;
}
