# COMPSCI 331 - Large-Scale Software Development

## Summary
### Project
Build a JAX-RS Web service for concert booking.

### Team
ProjectTeam 3
### Team Members:
- Melo Guan (ygua699)
    - Designed project structure, led and motivated team members to reach the target. Hosted regular tech design meetings and pair programming sections.
    - Created PR template, added some domain classes such as `Seat` and `User` and mapper classes such as `BookingMapper` and `SeatMapper`, implemented `login` and `booking` endpoints as well as contributed to other endpoints, cleaned up the project by doing code refactoring and commenting on the code.
- Nancy Zhong (nancy111573)
    - Implemented `Performer` and `Concert` domain, and edited other domain classes to pass initial database set up. Implemented first version of post `booking`, `concerts/{id}` and `performers/{id}` endpoints. Helped in `subscribe` and `login` endpoints.
    - Participated in group meetings as well as individual coding sections.
- Alex Cao (Alex-Beep-Cao)
    - Create domain files, implemented some methods in `ConcertMapper` and `PerformerMapper`. Implemented `Seats` and `subscribe` endpoints. Discussed with teammates about other endpoints also try to implemented `Booking` endpoints in own branch.
    - Participated in group meetings as well as individual coding sections.


## Description

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



