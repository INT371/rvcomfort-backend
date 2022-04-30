package sit.it.rvcomfort.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sit.it.rvcomfort.model.BaseResponse;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponse implements BaseResponse {

    private Integer id;

    private UserResponse user;

    private RoomResponse room;

    @JsonProperty("check_in_date")
    private ZonedDateTime checkInDate;

    @JsonProperty("check_out_date")
    private ZonedDateTime checkOutDate;

    @JsonProperty("reserved_name")
    private String reservedName;

    @JsonProperty("num_of_guest")
    private Integer numOfGuest;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    private String status;

}
