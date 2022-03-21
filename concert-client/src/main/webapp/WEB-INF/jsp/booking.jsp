<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Seat Booking</title>
    <link rel="stylesheet" href="./css/seatbooking.css">
    <link rel="stylesheet" href="./css/site.css">
    <link rel="stylesheet" href="./css/modal.css">
    <%--<script type="module" src="./js/seatbooking.js"></script>--%>

    <script type="module">
        import {SeatData} from "./js/seat-data-module.js";
        import {SeatUI} from "./js/seat-ui-module.js";
        import {initSeatBookingPage} from "./js/seatbooking.js";

        window.addEventListener("load", () => {

            <%-- All args for these JS functions are injected by JSP. --%>
            const seats = initSeatBookingPage(${concert.id}, "${concertDateString}");
            SeatUI.toggleBookedByLabel(seats, ${bookedSeatLabels});

        });
    </script>
    <style>
        .container {
            width: 95%;
        }
    </style>
</head>
<body>

<%@include file="navbar.jsp" %>

<div class="container flex-row justify-sb">

    <div class="seat-ui-container">

        <h1 class="seat-title">Select your seats</h1>
        <h2 class="seat-subtitle">(click as many as you like):</h2>

        <div class="seat-map"></div>

        <h3 class="seat-totals">Selected: <span id="selected-seat-labels">None</span> ($<span id="selected-seat-price">0.00</span>)
        </h3>

        <div id="seats-loading" class="loading">
            <img src="./images/loading.gif">
            <p>Loading...</p>
        </div>

        <button class="seat-button align-self-end" id="button-book">Book!</button>

    </div>

    <div class="right-sidebar flex-col align-center justify-center">
        <img class="shadow" src="./images/${concert.imageName}" width="500px">
        <h1>${concert.title}</h1>
        <p class="description">${concert.blurb}</p>
    </div>

</div>
<div id="modals-here"></div>
</body>
</html>