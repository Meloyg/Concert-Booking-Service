import {FetchAPI} from "./fetch-api.js";
import {Modal} from "./modal.js";
import {Utils} from "./utils.js";

window.addEventListener("load", () => {

    // DIVs where content will be added. Vars declared up here so as to not excessively call querySelector.
    const concertsListDiv = document.querySelector("#concerts-list");
    const concertTitleH1 = document.querySelector("#concert-title");
    const concertImageImg = document.querySelector("#concert-image");
    const concertBlurbP = document.querySelector("#concert-blurb");
    const concertPerformersDiv = document.querySelector("#concert-performers");
    const concertDatesTable = document.querySelector("#concert-dates");

    const modalsDiv = document.querySelector("#modals-here");

    loadConcertSummaries();

    async function loadConcertSummaries() {
        const concertsResponse = await FetchAPI.getConcertSummaries();
        const concertsJson = await concertsResponse.json();

        concertsJson.forEach(displaySummaryImageFor);

        loadFullConcert(concertsJson[0]);
    }

    function displaySummaryImageFor(summary) {

        const img = document.createElement("img");
        img.src = `./images/${summary.imageName}`;
        img.style.width = "375px";
        img.style.height = "125px";

        img.addEventListener("click", loadFullConcert.bind(img, summary));

        concertsListDiv.appendChild(img);

    }

    async function loadFullConcert(summary) {

        // console.log(summary);

        const concertResponse = await FetchAPI.getConcert(summary.id);
        const concertJson = await concertResponse.json();
        // console.log(concertJson);

        displayFullConcert(concertJson);
    }

    function displayFullConcert(concert) {

        concertTitleH1.innerText = concert.title;
        concertImageImg.src = `./images/${concert.imageName}`;
        concertBlurbP.innerText = concert.blurb;

        displayConcertPerformers(concert.performers);
        displayConcertDates(concert);
    }

    function displayConcertPerformers(performers) {
        Utils.clear(concertPerformersDiv);
        Utils.clear(modalsDiv);
        performers.forEach(performer => {

            const div = document.createElement("div");

            const img = document.createElement("img");
            img.src = `./images/${performer.imageName}`;
            img.classList.add("d-block");
            img.style.width = "100px";
            img.style.height = "100px";
            div.appendChild(img);

            const p = document.createElement("p")
            p.classList.add("text-center");
            p.innerText = performer.name;
            div.appendChild(p);

            concertPerformersDiv.appendChild(div);

            addPerformerModal(performer, img);
        });
    }

    function displayConcertDates(concert) {
        Utils.clear(concertDatesTable);

        concert.dates.forEach(date => {


            const tr = document.createElement("tr");

            const th = document.createElement("th");
            const theDate = new Date(date);
            const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
            th.innerText = theDate.toLocaleDateString("en-UK", options);
            tr.appendChild(th);

            const td = document.createElement("td");

            const button = document.createElement("button");
            button.innerText = "Book!";
            button.addEventListener("click", navigateToBookingPage.bind(button, concert.id, date));
            td.appendChild(button)

            tr.appendChild(td);

            concertDatesTable.appendChild(tr);

        });
    }

    function addPerformerModal(performer, modalShowButton) {
        // console.log("Add modal for performer", performer, modalShowButton);

        // Add modal element
        const modal = document.createElement("div");
        modal.classList.add("modal");
        modal.innerHTML =
            `<div class="modal-content">
                <span class="close-modal">&times;</span>
                <div class="flex-col align-center">
                    <h1>${performer.name}</h1>
                    <img class="d-block shadow" src="./images/${performer.imageName}">
                    <p class="description">${performer.blurb}</p>
                    <button>Ok!</button>
                </div>
            </div>`;
        modalsDiv.appendChild(modal);

        // Hook it up.
        Modal.enableModal(modal, modalShowButton);
        const img = modal.querySelector("img");
        img.style.width = "320px";
        img.style.height = "320px";
        modal.querySelector("button").addEventListener("click", modal.closeModal);
    }

    function navigateToBookingPage(concertId, date) {
        // console.log("nav to booking page", concertId, date);
        if (!Utils.getCookie("auth")) {
            Modal.displayAlertBox(modalsDiv, "Log in!", "Please log in at the top of the page before you try to book!");
        }

        else {
            window.location.href = `./BookSeats?concertId=${concertId}&date=${date}`;
        }

    }

});