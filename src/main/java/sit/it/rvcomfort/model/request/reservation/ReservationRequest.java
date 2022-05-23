package sit.it.rvcomfort.model.request.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sit.it.rvcomfort.model.BaseRequest;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest implements BaseRequest {

    @NotNull
    @JsonProperty("user_id")
    private Integer userId;

    @NotNull
    @JsonProperty("type_id")
    private Integer typeId;

    @JsonProperty("check_in_date")
    private ZonedDateTime checkInDate;

    @JsonProperty("check_out_date")
    private ZonedDateTime checkOutDate;

    @JsonProperty("reserved_name")
    private String reservedName;

    @JsonProperty("num_of_guest")
    private Integer numOfGuest;

}
