package sit.it.rvcomfort.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservedDate implements Serializable {

    @JsonProperty("check_in_date")
    private ZonedDateTime checkInDate;

    @JsonProperty("check_out_date")
    private ZonedDateTime checkOutDate;

}
