<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${signedIn == false}">
        <nav class="nav flex-row justify-sb align-center">
            <h1 class="nav-brand">Concert Booking System</h1>
            <form action="./Login" method="post" class="flex-row justify-end align-center">
                <label for="txtUsername">Username: </label>
                <input type="text" class="textbox" id="txtUsername" name="username" placeholder="Your username">
                <span style="width: 20px"></span>
                <label for="txtPassword">Password: </label>
                <input type="password" class="textbox" id="txtPassword" name="password" placeholder="Your password">
                <span style="width: 20px"></span>
                <button type="submit">Sign in</button>
            </form>
        </nav>
    </c:when>
    <c:otherwise>
        <nav class="nav flex-row justify-sb align-center">
            <h1 class="nav-brand">Concert Booking System</h1>
            <form action="./Logout" method="get" class="flex-row justify-end align-center">
                <button type="submit">Sign out</button>
            </form>
        </nav>
    </c:otherwise>
</c:choose>