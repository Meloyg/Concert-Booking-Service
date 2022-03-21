package proj.concert.service.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import proj.concert.service.domain.Seat;

/**
 * Utility class that models the layout of seats at the concert venue.
 */
public class TheatreLayout {

    // Data here should match up with that in seat-data-module.js. Ideally we wouldn't be hardcoding this...
    public static final int NUM_SEATS_PER_ROW = 12;
    public static final int NUM_ROWS = 10;
    public static final int NUM_SEATS_IN_THEATRE = NUM_SEATS_PER_ROW * NUM_ROWS;
    public static final PriceBand[] PRICE_BANDS = {
            new PriceBand("Platinum Seating", new BigDecimal(150), 5),
            new PriceBand("Gold Seating", new BigDecimal(120), 3),
            new PriceBand("Silver Seating", new BigDecimal(90), 2)
    };

    /**
     * A utility function that creates all required {@link Seat} objects for a concert on the given date.
     *
     * @param date the date
     * @return a list of Seat objects
     */
    public static Set<Seat> createSeatsFor(LocalDateTime date) {

        Set<Seat> seats = new HashSet<>();

        int rowsCreatedThisPricingBand = 0;
        int currentPricingIndex = 0;
        PriceBand currentPricing = PRICE_BANDS[currentPricingIndex];

        for (int rowNum = 0; rowNum < NUM_ROWS; rowNum++) {

            char rowLabel = (char) ('A' + rowNum); // String.fromCharCode("A".charCodeAt(0) + rowNum);

            for (int seatNum = 1; seatNum <= NUM_SEATS_PER_ROW; seatNum++) {

                String seatLabel = "" + rowLabel + seatNum;

                seats.add(new Seat(
                        seatLabel, false, date, currentPricing.price
                ));
            }

            rowsCreatedThisPricingBand++;
            if (rowsCreatedThisPricingBand == currentPricing.numRows) {
                currentPricingIndex++;
                currentPricing = currentPricingIndex < PRICE_BANDS.length ? PRICE_BANDS[currentPricingIndex] : null;
                rowsCreatedThisPricingBand = 0;
            }

        }

        return seats;

    }

    public static class PriceBand {
        public String name;
        public BigDecimal price;
        public int numRows;

        public PriceBand(String name, BigDecimal price, int numRows) {
            this.name = name;
            this.price = price;
            this.numRows = numRows;
        }
    }

}
