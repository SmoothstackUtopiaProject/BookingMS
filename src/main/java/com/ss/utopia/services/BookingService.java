package com.ss.utopia.services;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.BookingGuestNotFoundException;
import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
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
	BookingRepository bookingRepository;

	@Autowired 
	BookingGuestService bookingGuestService;

	@Autowired 
	BookingUserService bookingUserService;

	@Autowired 
	FlightBookingRepository flightBookingRepository;

	@Autowired 
	PassengerRepository passengerRepository;


	public List<Booking> findAll() throws ConnectException, IllegalArgumentException, SQLException {
		return bookingRepository.findAll();
	}

	public List<BookingWithReferenceData> findAllWithReferenceData() throws ConnectException, IllegalArgumentException, SQLException {
		List<Booking> bookings = bookingRepository.findAll();
		List<BookingGuest> bookingGuests = bookingGuestService.findAll();
		List<BookingUser> bookingUsers = bookingUserService.findAll();
		List<FlightBooking> flightBookings = flightBookingRepository.findAll();
		List<Passenger> passengers = passengerRepository.findAll();

		List<BookingWithReferenceData> bookingsWithNames = new ArrayList<BookingWithReferenceData>();
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

	public Booking findById(Integer bookingId) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		
		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		if(!optionalBooking.isPresent()) throw new BookingNotFoundException("No booking with ID: " + bookingId + " exist!");
		return optionalBooking.get();
	}

	public BookingWithReferenceData findByIdWithReferenceData(Integer bookingId) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {

		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		if(!optionalBooking.isPresent()) throw new BookingNotFoundException("No booking with ID: " + bookingId + " exist!");
		Booking booking = optionalBooking.get();
		BookingWithReferenceData bookingWithReferenceData = new BookingWithReferenceData(
			booking.getBookingId(), 
			booking.getBookingStatus(), 
			booking.getBookingConfirmationCode()
			, 0, 0, 0, "", ""
		);

		Optional<FlightBooking> optionalFlightBooking = flightBookingRepository.findById(bookingId);
		if(optionalFlightBooking.isPresent()) bookingWithReferenceData.setBookingFlightId(optionalFlightBooking.get().getFlightId());

		Optional<Passenger> optionalPassenger = passengerRepository.findByBookingId(bookingId);
		if(optionalPassenger.isPresent()) bookingWithReferenceData.setBookingPassengerId(optionalPassenger.get().getPassengerId());

		try {
			BookingUser bookingUser = bookingUserService.findByBookingId(bookingId);
			bookingWithReferenceData.setBookingUserId(bookingUser.getBookingUserId());
		} catch(BookingUserNotFoundException err){/* Nothing needed if not exists */}
		
		try {
			BookingGuest bookingGuest = bookingGuestService.findByBookingId(bookingId);
			bookingWithReferenceData.setBookingGuestEmail(bookingGuest.getBookingGuestEmail());
			bookingWithReferenceData.setBookingGuestPhone(bookingGuest.getBookingGuestPhone());
		} catch(BookingGuestNotFoundException err2){/* Nothing needed if not exists */}
		
		return bookingWithReferenceData;
	}
	
	public List<BookingWithReferenceData> findBySearchAndFilter(HashMap<String, String> filterMap) 
	throws ConnectException, SQLException {
		List<BookingWithReferenceData> bookings = findAllWithReferenceData();
		if(!filterMap.keySet().isEmpty()) bookings = applyFilters(bookings, filterMap);
		return bookings;
	}

	public List<BookingWithReferenceData> applyFilters(List<BookingWithReferenceData> bookings, HashMap<String, String> filterMap) {
		// Booking ID
		String bookingId = "bookingId";
		if(filterMap.keySet().contains(bookingId)) {
			try {
				Integer parsedBookingId = Integer.parseInt(filterMap.get(bookingId));
				bookings = bookings.stream()
				.filter(i -> i.getBookingId().equals(parsedBookingId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Booking Status
		String bookingStatus = "bookingStatus";
		if(filterMap.keySet().contains(bookingStatus)) {
			try {
				String parsedBookingStatus = filterMap.get(bookingStatus);
				bookings = bookings.stream()
				.filter(i -> i.getBookingStatus().equals(parsedBookingStatus))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Booking Confirmation Code
		String bookingConfirmationCode = "bookingConfirmationCode";
		if(filterMap.keySet().contains(bookingConfirmationCode)) {
			try {
				String parsedBookingConfirmationCode = filterMap.get(bookingConfirmationCode);
				bookings = bookings.stream()
				.filter(i -> i.getBookingConfirmationCode().equals(parsedBookingConfirmationCode))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Booking Flight ID
		String bookingFlightId = "bookingFlightId";
		if(filterMap.keySet().contains(bookingFlightId)) {
			try {
				Integer parsedBookingFlightId = Integer.parseInt(filterMap.get(bookingFlightId));
				bookings = bookings.stream()
				.filter(i -> i.getBookingFlightId().equals(parsedBookingFlightId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Booking User ID
		String bookingUserId = "bookingUserId";
		if(filterMap.keySet().contains(bookingUserId)) {
			try {
				Integer parsedBookingUserId = Integer.parseInt(filterMap.get(bookingUserId));
				bookings = bookings.stream()
				.filter(i -> i.getBookingUserId().equals(parsedBookingUserId))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Booking Guest Email
		String bookingGuestEmail = "bookingGuestEmail";
		if(filterMap.keySet().contains(bookingGuestEmail)) {
			try {
				String parsedBookingGuestEmail = filterMap.get(bookingGuestEmail);
				bookings = bookings.stream()
				.filter(i -> i.getBookingGuestEmail().equals(parsedBookingGuestEmail))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Booking Guest Phone
		String bookingGuestPhone = "bookingGuestPhone";
		if(filterMap.keySet().contains(bookingGuestPhone)) {
			try {
				String parsedBookingGuestPhone = filterMap.get(bookingGuestPhone);
				bookings = bookings.stream()
				.filter(i -> i.getBookingGuestPhone().equals(parsedBookingGuestPhone))
				.collect(Collectors.toList());
			} catch(Exception err){/*Do nothing*/}
		}

		// Search - (applied last due to save CPU usage
		return applySearch(bookings, filterMap);
	}

	public List<BookingWithReferenceData> applySearch(List<BookingWithReferenceData> bookings, HashMap<String, String> filterMap) {
		List<BookingWithReferenceData> bookingsWithSearchTerms = new ArrayList<BookingWithReferenceData>();
		
		String searchTerms = "searchTerms";
		if(filterMap.keySet().contains(searchTerms)) {
			String formattedSearch = filterMap.get(searchTerms)
			.toLowerCase()
			.replace(", ", ",");
			String[] splitTerms = formattedSearch.split(",");
			ObjectMapper mapper = new ObjectMapper();
			
			for(BookingWithReferenceData booking : bookings) {
				boolean containsSearchTerms = true;
				
				try {
					String bookingAsString = mapper.writeValueAsString(booking)
					.toLowerCase()
					.replace("bookingid", "")
					.replace("bookingstatus", "")
					.replace("bookingconfirmationcode", "")
					.replace("bookingflightid", "")
					.replace("bookinguserid", "")
					.replace("bookingguestemail", "")
					.replace("bookingguestphone", "");

					for(String term : splitTerms) {
						if(!bookingAsString.contains(term)) {
							containsSearchTerms = false;
							break;
						}
					}
				} catch(JsonProcessingException err){
					containsSearchTerms = false;
				}

				if(containsSearchTerms) {
					bookingsWithSearchTerms.add(booking);
				}
			}
		}
		return bookingsWithSearchTerms;
	}

	public BookingWithReferenceData insert(HashMap<String, String> bookingMap) 
	throws BookingUserNotFoundException, ConnectException, IllegalArgumentException, 
	NullPointerException, SQLException {

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
			try {
				bookingUserService.insert(booking.getBookingId(), bookingUserId);
				newBookingWithReferenceData.setBookingUserId(bookingUserId);
			} catch(Exception override) {/*Do Nothing*/}
		}

		// Create the Booking Guest
		if(bookingMap.keySet().contains("bookingGuestEmail")) {
			String email = bookingMap.get("bookingGuestEmail");
			String phone = bookingMap.get("bookingGuestPhone");
			try {
				bookingGuestService.insert(booking.getBookingId(), email, phone);
				newBookingWithReferenceData.setBookingGuestEmail(email);
				newBookingWithReferenceData.setBookingGuestPhone(phone);
			} catch(Exception override) {/*Do Nothing*/}
		}

		// Create the FlightBooking
		Integer bookingFlightId = null;
		if(bookingMap.keySet().contains("bookingFlightId")) {
			bookingFlightId = Integer.parseInt(bookingMap.get("bookingFlightId"));

			// TODO - temp disabled while flights is overhauled
			try {
				Optional<Flight> optionalFlight = flightBookingRepository.findByFlightById(bookingFlightId);
				if(optionalFlight.isPresent()) {
					try {
						flightBookingRepository.save(new FlightBooking(booking.getBookingId(), bookingFlightId));
						newBookingWithReferenceData.setBookingFlightId(bookingFlightId);
					} catch(Exception override) {/*Do Nothing*/}
				}
			} catch(Exception ignoreAll){}
		}
		return newBookingWithReferenceData;
	}

	public BookingWithReferenceData update(HashMap<String, String> bookingMap) 
	throws BookingNotFoundException, BookingUserNotFoundException, ConnectException, 
	IllegalArgumentException, NullPointerException, SQLException {

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
			try {
				bookingUserService.update(newBooking.getBookingId(), bookingUserId);
				newBookingWithReferenceData.setBookingUserId(bookingUserId);
			} catch(Exception override) {/*Do Nothing*/}
		}

		// Update the Booking Guest
		if(bookingMap.keySet().contains("bookingGuestEmail")) {
			String email = bookingMap.get("bookingGuestEmail");
			String phone = bookingMap.get("bookingGuestPhone");
			try {
				bookingGuestService.update(newBooking.getBookingId(), email, phone);
				newBookingWithReferenceData.setBookingGuestEmail(email);
				newBookingWithReferenceData.setBookingGuestPhone(phone);
			} catch(Exception override) {/*Do Nothing*/}
		}

		// Update the FlightBooking (creating it if does not exists)
		Integer bookingFlightId = null;
		if(bookingMap.keySet().contains("bookingFlightId")) {
			bookingFlightId = Integer.parseInt(bookingMap.get("bookingFlightId"));

			// TODO - temp disabled while flights is overhauled
			try {
				Optional<Flight> optionalFlight = flightBookingRepository.findByFlightById(bookingFlightId);
				if(optionalFlight.isPresent()) {
					try {
						Optional<FlightBooking> optionalFlightBooking = flightBookingRepository.findById(bookingId);
						if(optionalFlightBooking.isPresent()) {
							FlightBooking flightBooking = optionalFlightBooking.get();
							flightBooking.setFlightId(bookingFlightId);
							flightBookingRepository.save(flightBooking);
							newBookingWithReferenceData.setBookingFlightId(bookingFlightId);
						} else {
							flightBookingRepository.save(new FlightBooking(newBooking.getBookingId(), bookingFlightId));
							newBookingWithReferenceData.setBookingFlightId(bookingFlightId);
						}
					} catch(Exception override) {/*Do Nothing*/}
				}
			} catch(Exception ignoreAll){}
		}
		return newBookingWithReferenceData;
	}

	public void delete(Integer id) throws BookingNotFoundException, 
	ConnectException, IllegalArgumentException, SQLException {
		// Delete the Booking
		// Delete the BookingUser
		// Delete the BookingGuest
		// Delete the FlightBooking
		// Delete the Passenger

		findById(id);
		bookingRepository.deleteById(id);
	}
}