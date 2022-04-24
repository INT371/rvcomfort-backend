package sit.it.rvcomfort.model.request.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest implements Serializable {

    @NotBlank
    @JsonProperty("room_name")
    private String roomName;

    @NotNull
    @JsonProperty("type_id")
    private Integer typeId;

}
