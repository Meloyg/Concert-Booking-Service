const WEB_URI = "/webservice/services/concert-service";

export const FetchAPI = {

    getConcertSummaries() {
        return fetch(`${WEB_URI}/concerts/summaries`);
    },

    getConcerts() {
        return fetch(`${WEB_URI}/concerts`);
    },

    getConcert(id) {
        return fetch(`${WEB_URI}/concerts/${id}`);
    },

    makeBookingRequest(bookingRequest) {
        return this.post(`${WEB_URI}/bookings`, bookingRequest);
    },

    post(url, body) {
        return fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            cache: "no-cache",
            credentials: "same-origin",
            body: JSON.stringify(body)
        });
    }

};