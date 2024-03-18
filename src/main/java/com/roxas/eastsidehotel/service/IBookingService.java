package com.roxas.eastsidehotel.service;

import com.roxas.eastsidehotel.model.BookedRoom;

import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);
    String saveBooking(Long roomId, BookedRoom bookingRequest);
    BookedRoom findByBookingConfirmationCode(String confirmationCode);
    List<BookedRoom> getAllBookings();
    List<BookedRoom> getBookingsByUserEmail (String email);
}
