package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Optional;

import com.ss.utopia.exception.BookingAlreadyExistsException;
import com.ss.utopia.exception.BookingUserNotFoundException;
import com.ss.utopia.models.BookingUser;
import com.ss.utopia.repositories.BookingUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingUserService {
  
	@Autowired 
	BookingUserRepository bookingUserRepository;

	public BookingUser findByBookingId(Integer bookingId) throws BookingUserNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingUser> optionalBookingUser = bookingUserRepository.findById(bookingId);
		if(!optionalBookingUser.isPresent()) throw new BookingUserNotFoundException("No Booking User exists for Booking ID: " + bookingId + "!");
		return optionalBookingUser.get();
	}

	public BookingUser findByUserId(Integer userId) throws BookingUserNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingUser> optionalBookingUser = bookingUserRepository.findByUserId(userId);
		if(!optionalBookingUser.isPresent()) throw new BookingUserNotFoundException("No Booking User exists for User ID: " + userId + "!");
		return optionalBookingUser.get();
	}

  public BookingUser insert(Integer bookingId, Integer userId) 
	throws BookingAlreadyExistsException, ConnectException, IllegalArgumentException, SQLException {
		
    Optional<BookingUser> optionalBookingUser = bookingUserRepository.findById(bookingId);
		if(optionalBookingUser.isPresent()) {
      throw new BookingAlreadyExistsException("A Booking already exists for Booking ID: " + bookingId + "!");
    } else {
      return bookingUserRepository.save(new BookingUser(bookingId, userId));
    }
	}

  public BookingUser update(Integer bookingId, Integer userId) 
	throws BookingUserNotFoundException, ConnectException, IllegalArgumentException, SQLException {
    BookingUser bookingUser = findByBookingId(bookingId);
    bookingUser.setUserId(userId);
    return bookingUserRepository.save(bookingUser);
	}
}
