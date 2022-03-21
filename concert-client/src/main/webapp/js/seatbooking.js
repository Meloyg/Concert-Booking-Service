import {SeatData} from "./seat-data-module.js";
import {SeatUI} from "./seat-ui-module.js";
import {FetchAPI} from "./fetch-api.js";
import {Modal} from "./modal.js";

export function initSeatBookingPage(concertId, concertDate) {
    // Create the seats datastructure
    const seats = SeatData.createDefaultUnbookedSeatData();

    // Create the UI
    const container = document.querySelector(".seat-map");
    SeatUI.generateDefaultBookingUI(container, seats, handleSeatClicked);

    // Remove the loading bar
    document.querySelector("#seats-loading").style.display = "none";

    // Setup "buy" button
    document.querySelector("#button-book").addEventListener("click", handleBookButtonClicked);

    /**
     * Whenever a seat is clicked, if that seat isn't already booked, toggle its "held" status.
     */
    function handleSeatClicked() {
        const seatDiv = this;
        const seat = seatDiv.seat;

        if (!seat.isBooked) {
            SeatUI.toggleHeld(seat);

            displayTotals();
        }
    }

    /**
     * Updates the #selected-seat-price and #selected-seat-labels spans with the correct info.
     */
    function displayTotals() {
        const totalPrice = SeatData.getTotalHeldPrice(seats);
        const heldSeatLabels = SeatData.getHeldSeats(seats).map(seat => seat.label);

        const priceSpan = document.querySelector("#selected-seat-price");
        const labelSpan = document.querySelector("#selected-seat-labels");

        priceSpan.innerText = totalPrice.toFixed(2);
        labelSpan.innerText = heldSeatLabels.length > 0 ? heldSeatLabels.join(", ") : "None";
    }

    // When the "book" button is clicked, send a booking request.
    function handleBookButtonClicked() {

        const heldSeats = SeatData.getHeldSeats(seats);

        // Make booking request
        const bookingRequest = {
            concertId: concertId,
            date: concertDate,
            seatLabels: heldSeats.map(seat => seat.label)
        };

        makeBookingRequest(bookingRequest);
    }

    return seats;
}

async function makeBookingRequest(bookingRequest) {
    const modalsDiv = document.querySelector("#modals-here");
    try {

        const response = await FetchAPI.makeBookingRequest(bookingRequest);

        if (response.status === 201) {
            Modal.displayAlertBox(modalsDiv, "Booking Success!", "Congratulations, you're going to the concert!", goToConcerts);
        }
        else {
            Modal.displayAlertBox(modalsDiv, "Booking Error", `Booking error (status: ${response.status})`, goToConcerts);
        }

    } catch (err) {
        console.log(err);

        Modal.displayAlertBox(modalsDiv, "Booking Error", "Booking error (unknown)", goToConcerts);

    }

    function goToConcerts() {
        window.location.replace("./Concerts");
    }

}