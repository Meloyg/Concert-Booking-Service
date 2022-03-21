package proj.concert.common.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a completed booking.
 * concertId   the id of the concert which was booked
 * date        the date on which that concert was booked
 * seats       the seats which were booked for that concert on that date
 */
public class BookingDTO {

    private long concertId;
    private LocalDateTime date;
    private List<SeatDTO> seats = new ArrayList<>();

    public BookingDTO() {
    }

    public BookingDTO(long concertId, LocalDateTime date, List<SeatDTO> seats) {
        this.concertId = concertId;
        this.date = date;
        this.seats = seats;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }
}
