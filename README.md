# Concert Booking Service

![Build Status](https://github.com/ygua699/Concert-Booking-Service/actions/workflows/autotest.yml/badge.svg)
[![GitHub last commit](https://img.shields.io/github/last-commit/ygua699/Concert-Booking-Service.svg?style=flat-square)](https://github.com/ygua699/Concert-Booking-Service/main)
[![GitHub issues](https://img.shields.io/github/issues/ygua699/Concert-Booking-Service.svg?style=flat-square)](https://github.com/ygua699/Concert-Booking-Service/issues)
[![GitHub stars](https://img.shields.io/github/stars/ygua699/Concert-Booking-Service.svg?style=flat-square)](https://github.com/ygua699/Concert-Booking-Service/stargazers)

A JAX-RS Web service for concert booking.

## System description

A booking service is required for a small venue, with a capacity of 120 seats. The venue's seats are classified into three price bands, Silver, Gold and Platinum. A concert may run on multiple dates, and each concert has at least one performer. Additionally, a performer may feature in multiple concerts.

The service is to allow clients to retrieve information about concerts and performers, to make and enquire about seats and reservations, and to authenticate users.

When making a reservation, clients may enquire about available seats. They should be informed of any unbooked seats, along with their pricing. Clients may select any number of seats to book. When making a reservation request, double-bookings are not permitted. Only one client should be allowed to book a given seat for a given concert on a given date. Clients may not make partial bookings - either _all_ of a client's selected seats are booked, or _none_ of them are.

Clients may view concert and performer information, and enquire about available seating, whether or not they are authenticated. When authenticated, clients gain the ability to make reservations, and to view their own reservations. Clients should never be able to view the reservation information for other guests.

In addition, clients should be able to subscribe to information regarding concert bookings. Specifically, they should be able to subscribe to "watch" one or more concerts / dates, and be notified when those concerts / dates are about to sell out.

A particular quality attribute that the service must satisfy is scalability. It is expected that the service will experience high load when concert tickets go on sale.

## Execution

#### Running the complete system

The only way to run the complete system is to package both the `client` and `service` projects into `WAR` files, and deploy them to a running servlet container such as Tomcat.

Fortunately, running Maven's `package` goal on the parent project will build both WARs as required. However, we will still need to set up a Tomcat (or similar) instance to run the application.

Whichever IDE you use, the first step will be to download a Tomcat installation, if you don't already have one. You can find them [here](https://tomcat.apache.org/download-80.cgi). Get version `8.5.x` as an archive (the installer isn't required), download and unzip it somewhere on your machine, and then follow the steps below according to your IDE.

## Resources

A multi-module Maven project, `project-concert`, is supplied and comprises 3 modules:

- `concert-client`. A module that contains resources for building a concert client application.

- `concert-common`. This module defines several entities that are shared by the `client` and `service` modules.

- `concert-service`. The `service` module is responsible for implementing the Web service and persistence aspects of the concert application.

The parent project's POM includes common dependencies and properties that are inherited by the 3 modules.

#### System architecture

The system as a whole is a _tiered_ architecture, in which a browser application, driven by a _web app_, communicates via HTTP with the _web service_ you will create for this project. A high-level overview can be seen in the below diagram:

![](./spec/system-architecture.png)

#### Client module

The client module contains a _web application_, built using technologies such as HTML5, CSS, JavaScript, JSP, and JSTL. This web application is capable of acting as a _client_ to the web service which you will create for this project, as shown in the diagram above. The client contains both browser-based and server-based code (written in JavaScript and Java, respectively) which communicate with the `service` layer over HTTP.

The client is intended to be a demonstration of the kind of web application that can be powered by the web service you will create. You will not need to modify the application, other than potentially altering the value of `Config.WEB_SERVICE_URI`, but the application can be used as part of your testing.

#### Common module

This module includes a number of packages:

- `proj.concert.common.dto`. This package contains complete implementations for 7 DTO classes. The `client` webapp above, and the integration tests (see below), are defined in terms of the DTO classes.
- `proj.concert.common.jackson`. This package includes the Jackson serializer and deserializer implementations for Java's `LocalDateTime` class.

- `proj.concert.common.types`. This package defines basic data types that are common to the `client` and `service` modules. The types comprise 2 enumerations: `Genre` (for performers), and `BookingStatus` (for querying available / unavailable seats).

#### Service module

The `service` module will contain your completed Web service for the project. It contains the following packages:

- `proj.concert.service.domain`. This package will contain the web service's completed domain model. Currently it includes two **incomplete** classes - `Concert` and `Seat`. These two classes must be completed, and any other domain classes you deem necessary should be added.

- `proj.concert.service.jaxrs`. This package contains the `LocalDateTimeParam` class. This class allows us to receive `LocalDateTime` instances as _query_ or _path_ parameters, which may prove useful when implementing your web service. For a detailed description, be sure to read the class's comments.

- `proj.concert.service.mapper`. This package is intended to contain mapper classes, which map between your domain and DTO classes.

- `proj.concert.service.services`. This package contains the `ConcertResource` class, which you must implement for this project. It also contains a `PersistenceManager` class which can be used to obtain `EntityManager` instances when required, a complete `ConcertApplication` class, and a `TestResource` class which implements a web service endpoint to reset the database. This is used for testing purposes.

- `proj.concert.service.util`. This package contains the `ConcertUtils` class, which can re-initialize the database, and the `TheatreLayout` class, which can be used to generate `Seat`s for a particular concert date.

Beyond packages and source files, the `service` module contains the JPA `persistence.xml` file, a database initialise script (`db-init.sql`) and a `log4j.properties` file (in `src/main/resources`). Finally, integration tests are included in the `src/test/java` folder. As usual, these tests may be run by executing Maven's `verify` goal on the parent project, and a 100% pass-rate (along with correct client functionality) is a good indication that your service implementation is correct.

The `persistence.xml` file includes a `javax.persistence.sql-load-script-source` element whose value is the supplied`db-init.sql` script. The effect of this element is to run the script when the `EntityManagerFactory` is created.`db-init.sql` file populates the database with concert and performer data.

### Strategy used to minimise the chance of concurrency errors

The optimistic concurrency control (OCC) strategy is used to minimise the chance of concurrency errors.

When dealing with several clients, it's likely that they'll try to book seats at the same time.
To prevent this from happening, the JPA implementation is configured to disable concurrent writing on commits.
When anything on the same table is written to, or even merely when the table is read, naive implementations will throw this error.
And it only throws this error when the same seat is accessed via optimistic concurrency.
This implies that multiple people may book seats at the same time instead of waiting for someone else to secure tickets for a different show or the same concert but in different seats.
This greatly improves seat booking throughput, especially when several users are attempting to book seats for the same concert.

### How the domain model is organised

We used lazy loading for most of the list attributes in our domain classes to improve performance by having less to fetch. `SUBSELECT` was applied to all those to reduce wasteful queries and avoid n + 1 problems.
However, dynamic eager fetch was used when we retrieve all booked seats for notifying subscribers, this would avoid having to go into the database again when we retrieve seats immediately after.
Inline eager fetch would avoid having Cartesian product when seats are not immediately assessed after bookings are fetched.

Cascade type was only specified in the `performers` list in `Concert` class, because in a real world situation, performer and item performed would change frequently even close to the date of event.
Therefore, changes to the performers of a concert would need to be altered often, setting this list to persist cascade type would allow `performers` to be propagated when updating `Concert` instances.
`Seats` and `Booking` was left cascaded because seats should hold the status `Unbooked` when detached from all `Booking` instances.

### Scalability

Sub-select fetch mode when loading collections has one-to-many or many-to-many associations, the request only request single round to database, which is minimised the number of DB access.

Another scalable is statelessness. The service side implements the thread-per-request model, asynchronous communication between server and client. (Such as Subscription).
This allowed arbitrary replicated to different machines and on different places.

#### App usage

Using the application, browsing to `/Concerts` or simply `/` (within the webapp's _context_) will present the "homepage" of the app, allowing the user to view concerts. On this page, users may scroll through the list of concerts on the left side of the page. Clicking on a concert will cause that concert's info to be displayed in the detail view.

![](./spec/concerts-page.PNG)

On the detail view, users can see more information about the currently selected concert, as well as a list of performers and dates for that concert.

Clicking on a performer's image will display a modal dialog containing more information about that performer:

![](./spec/view-performer.PNG)

If the user hasn't logged in, then clicking on the "Book!" button next to one of a concert's dates will prompt the user to sign in:

![](./spec/please-login.PNG)

If the user has already signed in, they will be redirected to the seat selection page for their selected concert and date. See `concert-service/src/main/resources/db-init.sql` for the data that the server is initialised with, including possible users and their passwords.

![](./spec/booking-page.PNG)

Here, users can see a visual seat map of the venue, along with an indicator of which seats have already been booked. Clicking unbooked seats will toggle a blue halo around that seat - an indicator that the seat has been selected for booking, as can be seen in the above screenshot. A list of selected seats - along with the total ticket price - can be seen below the seat map.

Once the user has made their selection, they can click the "Book!" button. Assuming they have "sufficient funds" (which will always be true for this test case), and that no other user has booked the same seats in the meantime, the user will be presented with a success dialog box:

![](./spec/booking-success.PNG)

Dismissing the dialog will redirect the user back to the concerts page, where they may continue to book tickets at other concerts / dates. (or more tickets for the same concert / date).

## Contributors

| Name                      |
| ------------------------- |
| Melo Guan (ygua699)       |
| Nancy Zhong (nancy111573) |
| Alex Cao (Alex-Beep-Cao)  |
