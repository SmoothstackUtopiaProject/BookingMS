package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.ss.utopia.exceptions.BookingAlreadyExistsException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
import com.ss.utopia.models.BookingUser;
import com.ss.utopia.models.User;
import com.ss.utopia.repositories.BookingUserRepository;
import com.ss.utopia.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingUserService {
  
	@Autowired 
	BookingService bookingService;

	@Autowired 
	BookingUserRepository bookingUserRepository;

	@Autowired 
	UserRepository userRepository;

	public List<BookingUser> findAll() throws ConnectException, IllegalArgumentException, SQLException {
		return bookingUserRepository.findAll();
	}

	public BookingUser findByBookingId(Integer bookingId) throws BookingUserNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingUser> optionalBookingUser = bookingUserRepository.findById(bookingId);
		if(!optionalBookingUser.isPresent()) throw new BookingUserNotFoundException("No Booking User exists for Booking ID: " + bookingId + ".");
		return optionalBookingUser.get();
	}

	public BookingUser findByUserId(Integer userId) throws BookingUserNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingUser> optionalBookingUser = bookingUserRepository.findByUserId(userId);
		if(!optionalBookingUser.isPresent()) throw new BookingUserNotFoundException("No Booking User exists for User ID: " + userId + ".");
		return optionalBookingUser.get();
	}

	public User findUserByUserId(Integer userId) throws BookingUserNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<User> optionalUser = userRepository.findById(userId);
		if(!optionalUser.isPresent()) throw new BookingUserNotFoundException("No User with ID: " + userId + " exists.");
		return optionalUser.get();
	}

  public BookingUser insert(Integer bookingId, Integer userId) 
	throws BookingAlreadyExistsException, BookingUserNotFoundException, ConnectException, IllegalArgumentException, SQLException {
		
		findUserByUserId(userId);
		return bookingUserRepository.save(new BookingUser(bookingId, userId));
	}

  public BookingUser update(Integer bookingId, Integer userId) 
	throws BookingUserNotFoundException, ConnectException, IllegalArgumentException, SQLException {
    
		findUserByUserId(userId);
		BookingUser bookingUser = findByBookingId(bookingId);
    bookingUser.setBookingUserId(userId);
    return bookingUserRepository.save(bookingUser);
	}

	public void delete(Integer bookingId) throws BookingUserNotFoundException, 
	ConnectException, SQLException {

		findByBookingId(bookingId);
		bookingUserRepository.deleteByBookingId(bookingId);
	}

	public long deleteByBookingId(Integer bookingId) throws ConnectException, SQLException {

		long preRowsCount = bookingUserRepository.count();
		bookingUserRepository.deleteByBookingId(bookingId);
		long postRowsCount = bookingUserRepository.count();
		return preRowsCount - postRowsCount;
	}
}