import {DEFAULT_NUM_ROWS, DEFAULT_NUM_SEATS_PER_ROW, DEFAULT_PRICING_MODEL, SeatData} from "./seat-data-module.js";

export const SeatUI = {

    /**
     * Creates the booking UI for the default venue layout and the given seat data,
     * inside the given container element.
     */
    generateDefaultBookingUI(container, seatData, seatClickHandler = defaultHandleSeatClicked) {

        this.generateBookingUI(container, seatData,
            DEFAULT_NUM_SEATS_PER_ROW,
            DEFAULT_PRICING_MODEL,
            seatClickHandler
        );

    },

    /**
     * Creates the booking UI for the given venue layout and the given seat data,
     * inside the given container element.
     */
    generateBookingUI(container, seatData, seatsPerRow, pricingModel, seatClickHandler = defaultHandleSeatClicked) {

        let seatIndex = 0;
        for (let pmIndex = 0; pmIndex < pricingModel.length; pmIndex++) {

            const pricingBand = pricingModel[pmIndex];

            createPricingBandHTML(container, seatData, pricingBand, seatsPerRow, seatIndex, seatClickHandler);

            seatIndex += (seatsPerRow * pricingBand.numRows);

        }

    },

    toggleHeldByLabel(seats, ...labels) {
        const matchingSeats = SeatData.getSeatsByLabel(seats, ...labels);
        this.toggleHeld(...matchingSeats);
    },

    toggleHeld(...seats) {
        seats.forEach(seat => {
            seat.isHeld = !seat.isHeld;
            if (seat.seatDiv) {
                seat.seatDiv.classList.toggle("held");
            }
        });
    },

    toggleBookedByLabel(seats, ...labels) {
        const matchingSeats = SeatData.getSeatsByLabel(seats, ...labels);
        this.toggleBooked(...matchingSeats);
    },

    toggleBooked(...seats) {
        seats.forEach(seat => {
            seat.isBooked = !seat.isBooked;
            if (seat.seatDiv) {
                seat.seatDiv.classList.toggle("booked");
            }
        });
    },
};

/**
 * Creates HTML elements corresponding to all the seats in the given pricing band, and adds
 * them to the container.
 */
function createPricingBandHTML(container, seatData, pricingBand, seatsPerRow, startingSeatIndex, seatClickHandler) {

    const bandDiv = document.createElement("div");
    bandDiv.classList.add("band");
    bandDiv.classList.add(pricingBand.class);

    const bandLabelP = document.createElement("p");
    bandLabelP.classList.add("band-label");
    bandLabelP.innerText = pricingBand.name;
    bandDiv.appendChild(bandLabelP);

    const bandSeatsDiv = document.createElement("div");
    bandSeatsDiv.classList.add("band-seats");
    bandDiv.appendChild(bandSeatsDiv);

    // Create seats
    let seatIndex = startingSeatIndex;
    for (let row = 0; row < pricingBand.numRows; row++) {

        // HACK: Pull row label out of first seat in that row
        const rowLabel = seatData[seatIndex].label.charAt(0);

        createRowHTML(bandSeatsDiv, seatData, rowLabel, seatsPerRow, seatIndex, seatClickHandler);

        seatIndex += seatsPerRow;
    }

    container.appendChild(bandDiv);

}

/**
 * Creates HTML elements corresponding to a row of seats, and adds them to the container.
 */
function createRowHTML(container, seatData, rowLabel, numSeats, startingSeatIndex, seatClickHandler) {

    const rowLabelDiv = document.createElement("div");
    rowLabelDiv.classList.add("row-label");
    rowLabelDiv.innerHTML = `<p>${rowLabel}</p>`;
    container.appendChild(rowLabelDiv);

    for (let seatIndex = startingSeatIndex; seatIndex < startingSeatIndex + numSeats; seatIndex++) {

        const seat = seatData[seatIndex];
        const seatDiv = document.createElement("div");
        seatDiv.classList.add("seat");
        seatDiv.innerHTML = `<p>${seat.label}</p>`;

        // Add "held" / "booked" classes if required
        if (seat.isBooked) {
            seatDiv.classList.add("booked");
        }
        if (seat.isHeld) {
            seatDiv.classList.add("held");
        }

        seatDiv.seat = seat; // So each div knows the actual seat object.
        seat.seatDiv = seatDiv; // So each seat knows its UI element.

        seatDiv.addEventListener("click", seatClickHandler);
        container.appendChild(seatDiv);

    }
}
