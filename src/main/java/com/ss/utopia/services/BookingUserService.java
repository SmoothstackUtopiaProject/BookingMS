package com.ss.utopia.services;

import java.util.List;
import java.util.Optional;

import com.ss.utopia.exceptions.BookingNotFoundException;
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
	private BookingService bookingService;

	@Autowired 
	private BookingUserRepository bookingUserRepository;

	@Autowired 
	private UserRepository userRepository;

	public List<BookingUser> findAll() {
		return bookingUserRepository.findAll();
	}

	public BookingUser findByBookingId(Integer bookingId) throws BookingUserNotFoundException {
		Optional<BookingUser> optionalBookingUser = bookingUserRepository.findById(bookingId);
		if(!optionalBookingUser.isPresent()) {
			throw new BookingUserNotFoundException(
				"No Booking User exists for Booking ID: " + bookingId + "."
			);
		}
		return optionalBookingUser.get();
	}

	public BookingUser findByUserId(Integer userId) throws BookingUserNotFoundException {
		Optional<BookingUser> optionalBookingUser = bookingUserRepository.findByUserId(userId);
		if(!optionalBookingUser.isPresent()) {
			throw new BookingUserNotFoundException(
				"No Booking User exists for User ID: " + userId + "."
			);
		}
		return optionalBookingUser.get();
	}

	public User findUserByUserId(Integer userId) throws BookingUserNotFoundException {
		Optional<User> optionalUser = userRepository.findById(userId);
		if(!optionalUser.isPresent()) {
			throw new BookingUserNotFoundException(
				"No User with ID: " + userId + " exists."
			);
		}
		return optionalUser.get();
	}

  public BookingUser insert(Integer bookingId, Integer userId) throws BookingUserNotFoundException {
		findUserByUserId(userId);
		return bookingUserRepository.save(new BookingUser(bookingId, userId));
	}

  public BookingUser update(Integer bookingId, Integer userId) throws BookingUserNotFoundException {
		findUserByUserId(userId);
		BookingUser bookingUser = findByBookingId(bookingId);
    bookingUser.setBookingUserId(userId);
    return bookingUserRepository.save(bookingUser);
	}

	public void delete(Integer bookingId) throws BookingUserNotFoundException {
		findByBookingId(bookingId);
		bookingUserRepository.deleteByBookingId(bookingId);
	}

	public long deleteByBookingId(Integer bookingId) throws BookingNotFoundException {
		bookingService.findById(bookingId);
		long preRowsCount = bookingUserRepository.count();
		bookingUserRepository.deleteByBookingId(bookingId);
		long postRowsCount = bookingUserRepository.count();
		return preRowsCount - postRowsCount;
	}
}