package sit.it.rvcomfort.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sit.it.rvcomfort.model.BaseResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse implements BaseResponse {

    private String name;

    private String url;

}
