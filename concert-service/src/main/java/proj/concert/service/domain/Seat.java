package proj.concert.service.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "SEAT")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String label;
    private boolean isBooked;
    private LocalDateTime date;
    private BigDecimal price;

    @Version
    private Long version;

    public Seat() {}

    public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal price) {
        this.label = label;
        this.isBooked = isBooked;
        this.date = date;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return isBooked == seat.isBooked &&
                Objects.equals(id, seat.id) &&
                Objects.equals(label, seat.label) &&
                Objects.equals(date, seat.date) &&
                Objects.equals(price, seat.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, isBooked, date, price);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", isBooked=" + isBooked +
                ", date=" + date +
                ", price=" + price +
                '}';
    }

}
