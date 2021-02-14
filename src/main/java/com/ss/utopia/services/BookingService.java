package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exception.BookingAlreadyExistsException;
import com.ss.utopia.exception.BookingNotFoundException;
import com.ss.utopia.models.Booking;
import com.ss.utopia.repositories.BookingRepository;

@Service
public class BookingService {
	
	@Autowired
	BookingRepository bookingRepository;

	@Autowired 
	BookingGuestService bookingGuestService;

	@Autowired 
	BookingUserService bookingUserService;


	public List<Booking> findAll() throws ConnectException, IllegalArgumentException, SQLException {
		return bookingRepository.findAll();
	}

	public Booking findById(Integer bookingId) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		if(!optionalBooking.isPresent()) throw new BookingNotFoundException("No booking with ID: \"" + bookingId + "\" exist!");
		return optionalBooking.get();
	}
	
	public Booking findByConfirmationCode(String confirmationCode) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {

		Optional<Booking> optionalBooking = bookingRepository.findByConfirmationCode(confirmationCode);
		if(!optionalBooking.isPresent()) throw new BookingNotFoundException("No booking with Confirmation Code: \"" + confirmationCode + "\" exist!");
		return optionalBooking.get();
	}

	public List<Booking> findByStatus(String status) throws ConnectException, 
	IllegalArgumentException, SQLException {
		return bookingRepository.findByStatus(status);
	}

	public Booking insertByBookingUser(Integer userId) throws BookingAlreadyExistsException,
	 ConnectException, IllegalArgumentException, SQLException {

		Booking newBooking = bookingRepository.save(new Booking());
		bookingUserService.insert(newBooking.getId(), userId);
		return newBooking;
	}

	public Booking insertByBookingGuest(String email, String phone) throws BookingAlreadyExistsException,
	 ConnectException, IllegalArgumentException, SQLException {

		Booking newBooking = bookingRepository.save(new Booking());
		bookingGuestService.insert(newBooking.getId(), email, phone);
		return newBooking;
	}

	public Booking update(Integer bookingId, Integer status) 
	throws BookingNotFoundException, ConnectException, IllegalArgumentException, SQLException {

		Booking booking = findById(bookingId);
		booking.setIsActive(status);
		return bookingRepository.save(booking);
	}

	public void delete(Integer id) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		findById(id);
		bookingRepository.deleteById(id);
	}
}