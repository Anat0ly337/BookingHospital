package by.booking.hospital.demo.util;

import by.booking.hospital.demo.dto.BookingView;
import java.util.List;
/*
 *mini cache for scheduling cases, that dont touch database each other
 */
public class MiniCache {
    public static List<BookingView> CACHE;
}
