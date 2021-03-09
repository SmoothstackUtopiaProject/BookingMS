package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ss.utopia.exceptions.BookingAlreadyExistsException;
import com.ss.utopia.exceptions.BookingGuestNotFoundException;
import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.models.BookingGuest;
import com.ss.utopia.repositories.BookingGuestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingGuestService {
  
	@Autowired 
	BookingGuestRepository bookingGuestRepository;

	@Autowired 
	BookingService bookingService;

	public List<BookingGuest> findAll() throws ConnectException, IllegalArgumentException, SQLException {
		return bookingGuestRepository.findAll();
	}

	public BookingGuest findByBookingId(Integer bookingId) throws BookingGuestNotFoundException, 
  ConnectException, SQLException {
		
		try {
			Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findById(bookingId);
			if(!optionalBookingGuest.isPresent()) throw new BookingGuestNotFoundException("No Guests exists for Booking ID: " + bookingId + ".");
			return optionalBookingGuest.get();
		} catch(IllegalArgumentException err) {
			throw new BookingGuestNotFoundException("No Guests exists for Booking ID: " + bookingId + ".");
		}
	}

  public BookingGuest findByEmail(String email) throws BookingGuestNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findByEmail(email);
		if(!optionalBookingGuest.isPresent()) throw new BookingGuestNotFoundException("No Guests exists for email: " + email + ".");
		return optionalBookingGuest.get();
	}

	public BookingGuest findByPhone(String phone) throws BookingGuestNotFoundException, 
  ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findByPhone(phone);
		if(!optionalBookingGuest.isPresent()) throw new BookingGuestNotFoundException("No Guests exists for phone number: " + phone + ".");
		return optionalBookingGuest.get();
	}

	public BookingGuest insert(Integer bookingId, String email, String phone) throws BookingAlreadyExistsException, 
  ConnectException, IllegalArgumentException, SQLException {

    String formattedEmail = formatGeneric(email);
		String formattedPhone = formatPhone(phone);

		if(!validateEmail(formattedEmail)) throw new IllegalArgumentException("The email: " + email + " is not valid!");
		if(!validatePhone(formattedPhone)) throw new IllegalArgumentException("The phone number: " + phone + " is not valid!");
		
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findById(bookingId);
		if(optionalBookingGuest.isPresent()) {
      throw new BookingAlreadyExistsException("A Booking already exists for Booking ID: " + bookingId + ".");
    } else {
      return bookingGuestRepository.save(new BookingGuest(bookingId, email, phone));
    }
	}

  public BookingGuest update(Integer bookingId, String email, String phone) throws BookingNotFoundException, 
  ConnectException, IllegalArgumentException, SQLException {

		bookingService.findById(bookingId);

		String formattedEmail = formatGeneric(email);
		String formattedPhone = formatPhone(phone);

		if(!validateEmail(formattedEmail)) throw new IllegalArgumentException("The email: " + email + " is not valid!");
		if(!validatePhone(formattedPhone)) throw new IllegalArgumentException("The phone number: " + phone + " is not valid!");

		try {
			BookingGuest bookingGuest = findByBookingId(bookingId);
			bookingGuest.setBookingGuestEmail(formattedEmail);
			bookingGuest.setBookingGuestPhone(formattedPhone);
			return bookingGuestRepository.save(bookingGuest);
		} catch(BookingGuestNotFoundException err) {
			return bookingGuestRepository.save(new BookingGuest(bookingId, formattedEmail, formattedPhone));
		}
	}

	public void delete(Integer bookingId) throws BookingGuestNotFoundException, 
	ConnectException, SQLException {

		findByBookingId(bookingId);
		bookingGuestRepository.deleteByBookingId(bookingId);
	}

	public long deleteByBookingId(Integer bookingId) throws ConnectException, SQLException {

		long preRowsCount = bookingGuestRepository.count();
		bookingGuestRepository.deleteByBookingId(bookingId);
		long postRowsCount = bookingGuestRepository.count();
		return preRowsCount - postRowsCount;
	}

  private String formatGeneric(String name) {
		return name.trim().toUpperCase();
	}

	private String formatPhone(String phone) {
		return phone.replaceAll("[^0-9]", "");
	}

	private Boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile("^(.+)@(.+)$");
		Matcher matcher = pattern.matcher(email);
		return email != null &&
		matcher.matches() && 
		email.length() < 256 && 
		!email.isEmpty();
	}

	private Boolean validatePhone(String phone) {
		return phone != null &&
		phone.length() < 46 &&
		phone.replaceAll("[^0-9#]", "").length() == phone.length() &&
		!phone.isEmpty();
	}
}