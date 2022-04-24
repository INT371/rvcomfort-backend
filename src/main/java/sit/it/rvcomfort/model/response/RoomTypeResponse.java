package sit.it.rvcomfort.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeResponse implements Serializable {

    @JsonProperty("type_id")
    private Integer typeId;

    @JsonProperty("type_name")
    private String typeName;

    private String description;

    private BigDecimal price;

    @JsonProperty("max_capacity")
    private Integer maxCapacity;

    private String policy;

}
