package proj.concert.common.dto;

/**
 * Represents a notification from the web service regarding the number of seats remaining in a concert on a date.
 * numSeatsRemaining   the number of seats remaining, matching the original subscription request.
 */
public class ConcertInfoNotificationDTO {

    private int numSeatsRemaining;

    public ConcertInfoNotificationDTO() {
    }

    public ConcertInfoNotificationDTO(int numSeatsRemaining) {
        this.numSeatsRemaining = numSeatsRemaining;
    }

    public int getNumSeatsRemaining() {
        return numSeatsRemaining;
    }

    public void setNumSeatsRemaining(int numSeatsRemaining) {
        this.numSeatsRemaining = numSeatsRemaining;
    }
}
