package sit.it.rvcomfort.model.request.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultipleRoomTypeRequest implements Serializable {

    @JsonProperty("type_name")
    @NotBlank
    private String typeName;

    private String description;

    @NotNull
    private BigDecimal price;

    @JsonProperty("max_capacity")
    @NotNull
    private Integer maxCapacity;

    private String policy;

    @Valid
    @NotNull
    private List<RoomRequest> rooms;

}
