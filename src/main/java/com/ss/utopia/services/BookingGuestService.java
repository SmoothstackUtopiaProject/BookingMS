package com.ss.utopia.services;

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
	private BookingGuestRepository bookingGuestRepository;

	@Autowired 
	private BookingService bookingService;

	private static final Pattern REGEX_EMAIL = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,15}/g");
  private static final Pattern REGEX_PHONE = Pattern.compile("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-s./0-9]*$");

	public List<BookingGuest> findAll() {
		return bookingGuestRepository.findAll();
	}

	public BookingGuest findByBookingId(Integer bookingId) throws BookingGuestNotFoundException {
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findById(bookingId);
		if(!optionalBookingGuest.isPresent()) {
			throw new BookingGuestNotFoundException(
				"No Guests exists for Booking ID: " + bookingId + "."
			);
		}
		return optionalBookingGuest.get();
	}

  public BookingGuest findByEmail(String email) throws BookingGuestNotFoundException {
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findByEmail(email);
		if(!optionalBookingGuest.isPresent()) {
			throw new BookingGuestNotFoundException(
				"No Guests exists for email: " + email + "."
			);
		}
		return optionalBookingGuest.get();
	}

	public BookingGuest findByPhone(String phone) throws BookingGuestNotFoundException {
		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findByPhone(phone);
		if(!optionalBookingGuest.isPresent()) {
			throw new BookingGuestNotFoundException(
				"No Guests exists for phone number: " + phone + "."
			);
		}
		return optionalBookingGuest.get();
	}

	public BookingGuest insert(Integer bookingId, String email, String phone) throws BookingAlreadyExistsException {

		boolean isEmailValid = validateEmail(email);
		if(!isEmailValid) {
			throw new IllegalArgumentException(
				"The email: " + email + " is not valid!"
				);
			}
			
		boolean isPhoneValid = validatePhone(phone);
		if(!isPhoneValid) {
			throw new IllegalArgumentException(
				"The phone number: " + phone + " is not valid!"
			);
		}

		Optional<BookingGuest> optionalBookingGuest = bookingGuestRepository.findById(bookingId);
		if(optionalBookingGuest.isPresent()) {
      throw new BookingAlreadyExistsException(
				"A Booking already exists for Booking ID: " + bookingId + "."
			);
    }
		return bookingGuestRepository.save(new BookingGuest(bookingId, email, phone));
	}

  public BookingGuest update(Integer bookingId, String email, String phone) 
	throws BookingNotFoundException, IllegalArgumentException {
		bookingService.findById(bookingId);

		boolean isEmailValid = validateEmail(email);
		if(!isEmailValid) {
			throw new IllegalArgumentException(
				"The email: " + email + " is not valid!"
				);
			}
			
		boolean isPhoneValid = validatePhone(phone);
		if(!isPhoneValid) {
			throw new IllegalArgumentException(
				"The phone number: " + phone + " is not valid!"
			);
		}

		// Determine whether a new BookingGuest is needed here or an existing BookingGuest is being updated
		try {
			BookingGuest bookingGuest = findByBookingId(bookingId);
			bookingGuest.setBookingGuestEmail(email);
			bookingGuest.setBookingGuestPhone(phone);
			return bookingGuestRepository.save(bookingGuest);
		} 
		catch(BookingGuestNotFoundException err) {
			return bookingGuestRepository.save(new BookingGuest(bookingId, email, phone));
		}
	}

	public void delete(Integer bookingId) throws BookingGuestNotFoundException {
		findByBookingId(bookingId);
		bookingGuestRepository.deleteByBookingId(bookingId);
	}

	public long deleteByBookingId(Integer bookingId) {
		long preRowsCount = bookingGuestRepository.count();
		bookingGuestRepository.deleteByBookingId(bookingId);
		long postRowsCount = bookingGuestRepository.count();
		return preRowsCount - postRowsCount;
	}

	private static Boolean validateEmail(String email) {
		Matcher matcher = REGEX_EMAIL.matcher(email);
		return matcher.matches();
	}

	private static Boolean validatePhone(String phone) {
		Matcher matcher = REGEX_PHONE.matcher(phone);
		return matcher.matches();
	}
}