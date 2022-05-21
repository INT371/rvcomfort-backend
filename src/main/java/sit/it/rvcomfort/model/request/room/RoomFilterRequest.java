package sit.it.rvcomfort.model.request.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sit.it.rvcomfort.model.BaseRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomFilterRequest implements BaseRequest {

    @JsonProperty("min_price")
    @Min(0)
    private BigDecimal minPrice;

    @JsonProperty("max_price")
    @Min(0)
    private BigDecimal maxPrice;

    @JsonProperty("num_of_person")
    private Integer numOfPerson;

    @JsonProperty("check_in_date")
    @NotNull
    private ZonedDateTime checkInDate;

    @JsonProperty("check_out_date")
    @NotNull
    private ZonedDateTime checkOutDate;

//    private String policy;

}
