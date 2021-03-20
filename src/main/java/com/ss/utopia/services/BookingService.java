package com.ss.utopia.services;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.BookingAlreadyExistsException;
import com.ss.utopia.exceptions.BookingGuestNotFoundException;
import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
import com.ss.utopia.filters.BookingFilters;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingGuest;
import com.ss.utopia.models.BookingUser;
import com.ss.utopia.models.BookingWithReferenceData;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.FlightBooking;
import com.ss.utopia.models.Passenger;
import com.ss.utopia.repositories.FlightBookingRepository;
import com.ss.utopia.repositories.BookingRepository;
import com.ss.utopia.repositories.PassengerRepository;

@Service
public class BookingService {
	
	@Autowired
	private BookingRepository bookingRepository;

	@Autowired 
	private BookingGuestService bookingGuestService;

	@Autowired 
	private BookingUserService bookingUserService;

	@Autowired 
	private FlightBookingRepository flightBookingRepository;

	@Autowired 
	private PassengerRepository passengerRepository;


	public List<Booking> findAll() {
		return bookingRepository.findAll();
	}

	public List<BookingWithReferenceData> findAllWithReferenceData() {
		List<Booking> bookings = bookingRepository.findAll();
		List<BookingGuest> bookingGuests = bookingGuestService.findAll();
		List<BookingUser> bookingUsers = bookingUserService.findAll();
		List<FlightBooking> flightBookings = flightBookingRepository.findAll();
		List<Passenger> passengers = passengerRepository.findAll();

		List<BookingWithReferenceData> bookingsWithNames = new ArrayList<>();
		for(Booking booking : bookings) {
			BookingWithReferenceData newBookingWithReferenceData = new BookingWithReferenceData(
				booking.getBookingId(), 
				booking.getBookingStatus(), 
				booking.getBookingConfirmationCode(),
				 0, 0, 0
			);
			
			for(BookingGuest bookingGuest : bookingGuests) {
				if(bookingGuest.getBookingGuestId().equals(newBookingWithReferenceData.getBookingId())) {
					newBookingWithReferenceData.setBookingGuestEmail(bookingGuest.getBookingGuestEmail());
					newBookingWithReferenceData.setBookingGuestPhone(bookingGuest.getBookingGuestPhone());
				}
			}

			for(BookingUser bookingUser : bookingUsers) {
				if(bookingUser.getBookingId().equals(newBookingWithReferenceData.getBookingId())) {
					newBookingWithReferenceData.setBookingUserId(bookingUser.getBookingUserId());
				}
			}

			for(FlightBooking flightBooking : flightBookings) {
				if(flightBooking.getBookingId().equals(newBookingWithReferenceData.getBookingId())) {
					newBookingWithReferenceData.setBookingFlightId(flightBooking.getFlightId());
				}
			}
			
			for(Passenger passenger : passengers) {
				if(passenger.getPassengerBookingId().equals(newBookingWithReferenceData.getBookingId())) {
					newBookingWithReferenceData.setBookingPassengerId(passenger.getPassengerId());
				}
			}
			bookingsWithNames.add(newBookingWithReferenceData);
		}
		return bookingsWithNames;
	}

	public Booking findById(Integer bookingId) throws BookingNotFoundException {
		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		if(!optionalBooking.isPresent()) { 
			throw new BookingNotFoundException(
				"No booking with ID: " + bookingId + " exist!"
			);
		}
		return optionalBooking.get();
	}

	public BookingWithReferenceData findByIdWithReferenceData(Integer bookingId) throws BookingNotFoundException {
		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		if(!optionalBooking.isPresent()) {
			throw new BookingNotFoundException(
				"No booking with ID: " + bookingId + " exist!"
			);
		}

		Booking booking = optionalBooking.get();
		BookingWithReferenceData bookingWithReferenceData = new BookingWithReferenceData(
			booking.getBookingId(), 
			booking.getBookingStatus(), 
			booking.getBookingConfirmationCode()
			, 0, 0, 0, "", ""
		);

		Optional<FlightBooking> optionalFlightBooking = flightBookingRepository.findById(bookingId);
		if(optionalFlightBooking.isPresent()) {
			bookingWithReferenceData.setBookingFlightId(optionalFlightBooking.get().getFlightId());
		}

		Optional<Passenger> optionalPassenger = passengerRepository.findByBookingId(bookingId);
		if(optionalPassenger.isPresent()) {
			bookingWithReferenceData.setBookingPassengerId(optionalPassenger.get().getPassengerId());
		}

		try {
			BookingUser bookingUser = bookingUserService.findByBookingId(bookingId);
			bookingWithReferenceData.setBookingUserId(bookingUser.getBookingUserId());
		} 
		catch(BookingUserNotFoundException err){/* Nothing needed if not exists */}
		
		try {
			BookingGuest bookingGuest = bookingGuestService.findByBookingId(bookingId);
			bookingWithReferenceData.setBookingGuestEmail(bookingGuest.getBookingGuestEmail());
			bookingWithReferenceData.setBookingGuestPhone(bookingGuest.getBookingGuestPhone());
		} 
		catch(BookingGuestNotFoundException err2){/* Nothing needed if not exists */}
		return bookingWithReferenceData;
	}
	
	public List<BookingWithReferenceData> findBySearchAndFilter(Map<String, String> filterMap) {
		List<BookingWithReferenceData> bookings = findAllWithReferenceData();
		if(!filterMap.keySet().isEmpty()) {
			bookings = BookingFilters.apply(bookings, filterMap);
		}
		return bookings;
	}

	public BookingWithReferenceData insert(Map<String, String> bookingMap) throws BookingUserNotFoundException {

		// Verify any UserID is valid before creating the new Booking
		Integer bookingUserId = null;
		if(bookingMap.keySet().contains("bookingUserId")) {
			bookingUserId = Integer.parseInt(bookingMap.get("bookingUserId"));
			bookingUserService.findUserByUserId(bookingUserId);
		}

		// Create the Booking
		Booking booking = bookingRepository.save(new Booking("INACTIVE"));
		BookingWithReferenceData newBookingWithReferenceData = new BookingWithReferenceData();
		newBookingWithReferenceData.setBookingId(booking.getBookingId());
		newBookingWithReferenceData.setBookingConfirmationCode(booking.getBookingConfirmationCode());
		newBookingWithReferenceData.setBookingstatus(booking.getBookingStatus());

		// Create the Booking User
		if(bookingUserId != null) {
			bookingUserService.insert(booking.getBookingId(), bookingUserId);
			newBookingWithReferenceData.setBookingUserId(bookingUserId);
		}

		// Create the Booking Guest
		if(bookingMap.keySet().contains("bookingGuestEmail")) {
			String email = bookingMap.get("bookingGuestEmail");
			String phone = bookingMap.get("bookingGuestPhone");
			try {
				bookingGuestService.insert(booking.getBookingId(), email, phone);
			} 
			catch (BookingAlreadyExistsException e) {/*No problem if Booking Guest is already in the system*/}
			newBookingWithReferenceData.setBookingGuestEmail(email);
			newBookingWithReferenceData.setBookingGuestPhone(phone);
		}

		// Create the FlightBooking
		Integer bookingFlightId = null;
		if(bookingMap.keySet().contains("bookingFlightId")) {
			bookingFlightId = Integer.parseInt(bookingMap.get("bookingFlightId"));
			Optional<Flight> optionalFlight = flightBookingRepository.findByFlightById(bookingFlightId);
			if(optionalFlight.isPresent()) {
				flightBookingRepository.save(new FlightBooking(booking.getBookingId(), bookingFlightId));
				newBookingWithReferenceData.setBookingFlightId(bookingFlightId);
			}
		}
		return newBookingWithReferenceData;
	}

	public BookingWithReferenceData update(Map<String, String> bookingMap) 
	throws BookingNotFoundException, BookingUserNotFoundException {

		// Verify any UserID is valid before updating the Booking
		Integer bookingUserId = null;
		if(bookingMap.keySet().contains("bookingUserId")) {
			bookingUserId = Integer.parseInt(bookingMap.get("bookingUserId"));
			bookingUserService.findUserByUserId(bookingUserId);
		}

		// Update the Booking
		Integer bookingId = Integer.parseInt(bookingMap.get("bookingId"));
		String bookingStatus = bookingMap.get("bookingStatus");

		Booking currentBooking = findById(bookingId);
		BookingWithReferenceData newBookingWithReferenceData = findByIdWithReferenceData(bookingId);
		
		currentBooking.setBookingStatus(bookingStatus);
		Booking newBooking = bookingRepository.save(currentBooking);

		newBookingWithReferenceData.setBookingId(newBooking.getBookingId());
		newBookingWithReferenceData.setBookingConfirmationCode(newBooking.getBookingConfirmationCode());
		newBookingWithReferenceData.setBookingstatus(newBooking.getBookingStatus());

		// Update the Booking User
		if(bookingUserId != null) {
			bookingUserService.update(newBooking.getBookingId(), bookingUserId);
			newBookingWithReferenceData.setBookingUserId(bookingUserId);
		}

		// Update the Booking Guest
		if(bookingMap.keySet().contains("bookingGuestEmail")) {
			String email = bookingMap.get("bookingGuestEmail");
			String phone = bookingMap.get("bookingGuestPhone");
			try {
				bookingGuestService.insert(newBookingWithReferenceData.getBookingId(), email, phone);
			} 
			catch (BookingAlreadyExistsException e) {/*No problem if Booking Guest is already in the system*/}
			newBookingWithReferenceData.setBookingGuestEmail(email);
			newBookingWithReferenceData.setBookingGuestPhone(phone);
		}

		// Update the FlightBooking (creating it if does not exists)
		Integer bookingFlightId = null;
		if(bookingMap.keySet().contains("bookingFlightId")) {
			bookingFlightId = Integer.parseInt(bookingMap.get("bookingFlightId"));
			Optional<Flight> optionalFlight = flightBookingRepository.findByFlightById(bookingFlightId);
			if(optionalFlight.isPresent()) {
				flightBookingRepository.save(new FlightBooking(newBookingWithReferenceData.getBookingId(), bookingFlightId));
				newBookingWithReferenceData.setBookingFlightId(bookingFlightId);
			}
		}
		return newBookingWithReferenceData;
	}

	public void delete(Integer id) throws BookingNotFoundException {
		// Delete the Booking
		findById(id);
		bookingRepository.deleteById(id);

		// Delete the BookingUser
		// Delete the BookingGuest
		// Delete the FlightBooking
		// Delete the Passenger
	}
}