package proj.concert.service.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime date;
    private Long concertId;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Seat> seatList;

    @ManyToOne
    private User user;

    public Booking(LocalDateTime date, Long concertId, List<Seat> seatList, User user) {
        this.date = date;
        this.concertId = concertId;
        this.seatList = seatList;
        this.user = user;
    }

    public Booking() {
        this(null, null, null, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getConcertId() {
        return concertId;
    }

    public void setConcertId(Long concertId) {
        this.concertId = concertId;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) &&
                Objects.equals(date, booking.date) &&
                Objects.equals(concertId, booking.concertId) &&
                Objects.equals(seatList, booking.seatList) &&
                Objects.equals(user, booking.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, concertId, seatList, user);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", date=" + date +
                ", concertId=" + concertId +
                ", seatList=" + seatList +
                ", user=" + user +
                '}';
    }
}
