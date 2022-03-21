package proj.concert.common.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request to make a booking.
 * concertId   the id of the concert to be booked
 * date        the date on which that concert is to be booked
 * seats       the seats which are requested for that concert on that date
 */
public class BookingRequestDTO {

    private long concertId;
    private LocalDateTime date;
    private List<String> seatLabels = new ArrayList<>();

    public BookingRequestDTO(){}

    public BookingRequestDTO(long concertId, LocalDateTime date) {
        this.concertId = concertId;
        this.date = date;
    }

    public BookingRequestDTO(long concertId, LocalDateTime date, List<String> seatLabels) {
        this.concertId = concertId;
        this.date = date;
        this.seatLabels = seatLabels;
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

    public List<String> getSeatLabels() {
        return seatLabels;
    }

    public void setSeatLabels(List<String> seatLabels) {
        this.seatLabels = seatLabels;
    }
}
