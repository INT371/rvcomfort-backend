package sit.it.rvcomfort.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {

    private static final String timezone = "Asia/Bangkok";

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of(timezone));
    }

}
