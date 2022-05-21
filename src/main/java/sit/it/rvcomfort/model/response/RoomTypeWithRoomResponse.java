package sit.it.rvcomfort.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sit.it.rvcomfort.model.BaseResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse.RoomTypeImageResponse;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeWithRoomResponse implements BaseResponse {

    @JsonProperty("type_id")
    private Integer typeId;

    @JsonProperty("type_name")
    private String typeName;

    private String description;

    private BigDecimal price;

    @JsonProperty("max_capacity")
    private Integer maxCapacity;

    private String policy;

    private List<RoomTypeImageResponse> images;

    private List<SimpleRoomResponse> rooms;
}


