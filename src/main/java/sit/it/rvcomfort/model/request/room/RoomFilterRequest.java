package sit.it.rvcomfort.model.request.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sit.it.rvcomfort.model.BaseRequest;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomFilterRequest implements BaseRequest {

    @JsonProperty("min_price")
    private BigDecimal minPrice;

    @JsonProperty("max_price")
    private BigDecimal maxPrice;

    @JsonProperty("min_capacity")
    private Integer minCapacity;

    @JsonProperty("max_capacity")
    private Integer maxCapacity;

    @JsonProperty("check_in_date")
    @NotNull
    private ZonedDateTime checkInDate;

    @JsonProperty("check_out_date")
    @NotNull
    private ZonedDateTime checkOutDate;

//    private String policy;

}
