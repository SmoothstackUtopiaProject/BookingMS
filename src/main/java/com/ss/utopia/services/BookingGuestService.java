package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ss.utopia.exception.BookingAlreadyExistsException;
import com.ss.utopia.exception.BookingGuestNotFoundException;
import com.ss.utopia.models.BookingGuest;
import com.ss.utopia.repositories.BookingGuestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingGuestService {
  
	@Autowired 
	BookingGuestRepository bookingGuestRepository;

	public BookingGuest findByBookingId(Integer bookingId) throws BookingGuestNotFoundException, 
  ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findById(bookingId);
		if(!optionalBookingGuest.isPresent()) throw new BookingGuestNotFoundException("No Booking Guest exists for Booking ID: " + bookingId + "!");
		return optionalBookingGuest.get();
	}

  public BookingGuest findByEmail(String email) throws BookingGuestNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findByEmail(email);
		if(!optionalBookingGuest.isPresent()) throw new BookingGuestNotFoundException("No Booking Guest exists for email: " + email + "!");
		return optionalBookingGuest.get();
	}

	public BookingGuest findByPhone(String phone) throws BookingGuestNotFoundException, 
  ConnectException, IllegalArgumentException, SQLException {
		
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findByPhone(phone);
		if(!optionalBookingGuest.isPresent()) throw new BookingGuestNotFoundException("No Booking Guest exists for phone number: " + phone + "!");
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
      throw new BookingAlreadyExistsException("A Booking already exists for Booking ID: " + bookingId + "!");
    } else {
      return bookingGuestRepository.save(new BookingGuest(bookingId, email, phone));
    }
	}

  public BookingGuest update(Integer bookingId, String email, String phone) throws BookingGuestNotFoundException, 
  ConnectException, IllegalArgumentException, SQLException {

		String formattedEmail = formatGeneric(email);
		String formattedPhone = formatPhone(phone);

		if(!validateEmail(formattedEmail)) throw new IllegalArgumentException("The email: " + email + " is not valid!");
		if(!validatePhone(formattedPhone)) throw new IllegalArgumentException("The phone number: " + phone + " is not valid!");

    BookingGuest bookingGuest = findByBookingId(bookingId);
    bookingGuest.setEmail(email);
    bookingGuest.setPhone(phone);
    return bookingGuestRepository.save(bookingGuest);
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