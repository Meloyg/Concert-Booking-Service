// In a real system, these wouldn't be hardcoded.
export const DEFAULT_NUM_ROWS = 10;
export const DEFAULT_NUM_SEATS_PER_ROW = 12;
export const DEFAULT_PRICING_MODEL = [
    {name: "Platinum Seating", class: "platinum", numRows: 2, price: 150},
    {name: "Gold Seating", class: "gold", numRows: 3, price: 120},
    {name: "Silver Seating", class: "silver", numRows: 5, price: 90}
];


export const SeatData = {

    getSeatsByLabel(seats, ...labels) {
        return seats.filter(seat => labels.includes(seat.label));
    },

    getHeldSeats(seats) {
        return seats.filter(seat => seat.isHeld);
    },

    getTotalHeldPrice(seats) {
        return this.getHeldSeats(seats).reduce((acc, seat) => acc + seat.price, 0);
    },

    /**
     * Creates an array of unbooked seats according to the default venue layout.
     */
    createDefaultUnbookedSeatData() {
        return this.createUnbookedSeatData(DEFAULT_NUM_ROWS, DEFAULT_NUM_SEATS_PER_ROW, DEFAULT_PRICING_MODEL);
    },

    /**
     * Creates an array of unbooked seats according to the given venue layout.
     */
    createUnbookedSeatData(numRows, numSeatsPerRow, pricingModel) {
        const seatData = [];

        let numSeatsCreated = 0;
        let rowsCreatedThisPricingBand = 0;
        let currentPricingIndex = 0;
        let currentPricing = pricingModel[currentPricingIndex];

        for (let rowNum = 0; rowNum < numRows; rowNum++) {

            const rowLabel = String.fromCharCode("A".charCodeAt(0) + rowNum);

            for (let seatNum = 1; seatNum <= numSeatsPerRow; seatNum++) {

                const seatLabel = `${rowLabel}${seatNum}`;

                seatData.push({
                    id: numSeatsCreated + 1,
                    label: seatLabel,
                    isBooked: false,
                    isHeld: false,
                    price: currentPricing.price
                });

            }

            rowsCreatedThisPricingBand++;
            if (rowsCreatedThisPricingBand === currentPricing.numRows) {
                currentPricingIndex++;
                currentPricing = pricingModel[currentPricingIndex];
                rowsCreatedThisPricingBand = 0;
            }

        }

        return seatData;
    }

};