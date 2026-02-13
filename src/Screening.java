import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Screening {

    private LocalDateTime localDateTime;
    Type ScreeningType;
    Map<String, SeatStatuses>  seatStatuses = new HashMap<>();
}
