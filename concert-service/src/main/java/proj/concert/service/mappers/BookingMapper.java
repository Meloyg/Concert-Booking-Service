package proj.concert.service.mappers;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;

import java.util.*;
import java.util.stream.*;


/**
 * Mapper class for converting between Booking and BookingDTO objects.
 */
public class BookingMapper {
    
    public static BookingDTO toDTO(Booking booking) {
        return new BookingDTO(booking.getConcertId(), booking.getDate(),
                booking.getSeats()
                       .stream()
                       .map(SeatMapper::toDTO)
                       .collect(Collectors.toList()));
    }

    public static List<BookingDTO> listToDTO(List<Booking> bookingList) {
        ArrayList<BookingDTO> result = new ArrayList<>();

        for (Booking b : bookingList) {
            result.add(BookingMapper.toDTO(b));
        }

        return result;
    }

}
