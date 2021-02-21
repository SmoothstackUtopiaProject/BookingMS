package com.ss.utopia;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exception.BookingAlreadyExistsException;
import com.ss.utopia.exception.BookingGuestNotFoundException;
import com.ss.utopia.exception.BookingNotFoundException;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingGuest;
import com.ss.utopia.models.HttpError;
import com.ss.utopia.models.BookingWithReferenceData;
import com.ss.utopia.services.BookingGuestService;
import com.ss.utopia.services.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;

	@Autowired
	private BookingGuestService bookingGuestService;

	@GetMapping()
	public ResponseEntity<Object> findAll() 
	throws ConnectException, SQLException {

		List<Booking> bookingList = bookingService.findAll();
		return !bookingList.isEmpty() 
		? new ResponseEntity<>(bookingList, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/referencedata")
	public ResponseEntity<Object> findAllWithReferenceData() 
	throws ConnectException, SQLException {

		List<BookingWithReferenceData> bookingList = bookingService.findAllWithReferenceData();
		return !bookingList.isEmpty() 
		? new ResponseEntity<>(bookingList, HttpStatus.OK)
		: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@GetMapping("{path}")
	public ResponseEntity<Object> findById(@PathVariable String path)
	throws ConnectException, SQLException {

		try {
			Integer bookingId = Integer.parseInt(path);
			BookingWithReferenceData bookingWithReferenceData = bookingService.findByIdWithReferenceData(bookingId);
			return new ResponseEntity<>(bookingWithReferenceData, HttpStatus.OK);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Booking ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
			
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<Object> findByConfirmationCode(@PathVariable String confirmationCode)
	throws ConnectException, SQLException {

		try {
			Booking booking = bookingService.findByConfirmationCode(confirmationCode);
			return new ResponseEntity<>(booking, HttpStatus.OK);

		} catch( IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 400), HttpStatus.BAD_REQUEST);
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Object> findByStatus(@RequestParam String status)
	throws ConnectException, SQLException {

		try{
			Integer statusId = Integer.parseInt(status);
			List<Booking> bookingList = bookingService.findByStatus(statusId);
			return !bookingList.isEmpty() 
			? new ResponseEntity<>(bookingList, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process Status " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/guest")
	public ResponseEntity<Object> insertByGuest(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			BookingGuest guest = new ObjectMapper().readValue(body, BookingGuest.class);
			Booking newBooking = bookingService.insertByBookingGuest(guest.getEmail(), guest.getPhone());
			return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

		} catch(IllegalArgumentException | JsonProcessingException | NullPointerException err) {
			String errorMessage = "Cannot process Guest contact information " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingAlreadyExistsException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 409), HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/user/{userIdString}")
	public ResponseEntity<Object> insertByUser(@PathVariable String userIdString)
	throws ConnectException, SQLException {

		try {
			Integer userId = Integer.parseInt(userIdString);
			Booking newBooking = bookingService.insertByBookingUser(userId);
			return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process UserID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingAlreadyExistsException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 409), HttpStatus.CONFLICT);
		}
	}

	@PutMapping()
	public ResponseEntity<Object> update(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			BookingWithReferenceData bookingWithReferenceData = new ObjectMapper().readValue(body, BookingWithReferenceData.class);			
			BookingWithReferenceData newBookingWithReferenceData = bookingService.updateWithReferenceData(
				bookingWithReferenceData.getId(), bookingWithReferenceData.getStatus(), bookingWithReferenceData.getFlightId(),
				bookingWithReferenceData.getPassengerId(), bookingWithReferenceData.getUserId(),
				bookingWithReferenceData.getGuestEmail(), bookingWithReferenceData.getGuestPhone());			
			return new ResponseEntity<>(newBookingWithReferenceData, HttpStatus.ACCEPTED);

		} catch(IllegalArgumentException | JsonProcessingException | NullPointerException err) {			
			String errorMessage = "Cannot process Booking " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/guest")
	public ResponseEntity<Object> updateBookingGuest(@RequestBody String body)
	throws ConnectException, SQLException {

		try {
			BookingGuest guest = new ObjectMapper().readValue(body, BookingGuest.class);
			BookingGuest newBookingGuest = bookingGuestService.update(guest.getBookingId(), guest.getEmail(), guest.getPhone());
			return new ResponseEntity<>(newBookingGuest, HttpStatus.ACCEPTED);

		} catch(IllegalArgumentException | JsonProcessingException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingGuestNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{bookingIdString}")
	public ResponseEntity<Object> delete(@PathVariable String bookingIdString)
	throws ConnectException, SQLException  {

		try {
			Integer bookingId = Integer.parseInt(bookingIdString);
			bookingService.delete(bookingId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			String errorMessage = "Cannot process BookingID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length());
			return new ResponseEntity<>(new HttpError(errorMessage, 400), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(new HttpError(err.getMessage(), 404), HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(new HttpError("Service unavailable.", 503), HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(new HttpError("Invalid http request body.", 404), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(new HttpError("Service unavailable.", 503), HttpStatus.SERVICE_UNAVAILABLE);
	}
}
