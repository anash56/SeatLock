# SeatLock design

## Features (v1)
1. Users can sign up and log in (JWT).
2. Admins can create events and shows (an event has many shows, each with a venue and time).
3. Users can browse events, shows, and the seat map with availability.
4. Users can hold a seat for 5 minutes, then confirm the booking.
5. A seat can never be booked twice.
6. Users can view and cancel their bookings.

## Tables
- users: id, name, email (unique), password_hash, role (USER/ADMIN), created_at
- events: id, title, description, category, created_at
- shows: id, event_id, venue, start_time, price (in paise)
- seats: id, show_id, seat_label (e.g. A1), status (AVAILABLE/HELD/BOOKED), version
- bookings: id, user_id, show_id, status (PENDING/CONFIRMED/CANCELLED), total_amount, created_at
- booking_seats: id, booking_id, seat_id

## Relationships
- one event has many shows
- one show has many seats
- one user has many bookings
- one booking has many seats (linked through booking_seats)
- a seat belongs to at most one CONFIRMED booking (enforced in the database)

## APIs
- POST /auth/signup
- POST /auth/login
- GET /events
- GET /events/{id}
- GET /events/{id}/shows
- GET /shows/{id}/seats
- POST /shows/{id}/seats/{seatId}/hold
- POST /bookings
- GET /bookings/me
- DELETE /bookings/{id}
- POST /admin/events
- POST /admin/shows

## The main problem
Users A and B both see seat A1 as available and click "book" at the same moment.
The server handles both requests in parallel. Both read the seat as AVAILABLE, both
pass the check, and both write it as BOOKED. Two people now think they own one seat.
This is a race condition, because check-then-act is not atomic. I will reproduce it
with 50 threads and then fix it with a DB constraint, optimistic locking, pessimistic
locking, and Redis holds.