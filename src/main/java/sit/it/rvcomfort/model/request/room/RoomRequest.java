package sit.it.rvcomfort.model.request.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sit.it.rvcomfort.model.entity.RoomType;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest implements Serializable {

    @JsonProperty("room_name")
    private String roomName;

    @JsonProperty("room_type")
    private RoomType roomType;

}
