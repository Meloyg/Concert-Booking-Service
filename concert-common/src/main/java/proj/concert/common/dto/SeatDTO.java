package proj.concert.common.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

/**
 * DTO class to represent seats at the concert venue.
 * <p>
 * A SeatDTO describes a seat in terms of:
 * label    the seat label
 * price    the price
 */
public class SeatDTO {

    private String label;
    private BigDecimal price;

    public SeatDTO() {
    }

    public SeatDTO(String label, BigDecimal price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SeatDTO seatDTO = (SeatDTO) o;

        return new EqualsBuilder()
                .append(label, seatDTO.label)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(label)
                .toHashCode();
    }
}
