package com.ss.utopia;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import com.ss.utopia.exception.BookingAlreadyExistsException;
import com.ss.utopia.exception.BookingGuestNotFoundException;
import com.ss.utopia.exception.BookingNotFoundException;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingGuest;
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

	@GetMapping("{path}")
	public ResponseEntity<Object> findById(@PathVariable String path)
	throws ConnectException, SQLException {

		try {
			Integer bookingId = Integer.parseInt(path);
			Booking booking = bookingService.findById(bookingId);
			return new ResponseEntity<>(booking, HttpStatus.OK);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process Booking ID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);
			
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<Object> findByConfirmationCode(@PathVariable String confirmationCode)
	throws ConnectException, SQLException {

		try {
			Booking booking = bookingService.findByConfirmationCode(confirmationCode);
			return new ResponseEntity<>(booking, HttpStatus.OK);

		} catch( IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Object> findByStatus(@RequestParam String status)
	throws ConnectException, SQLException {

		try{
			List<Booking> bookingList = bookingService.findByStatus(status);
			return !bookingList.isEmpty() 
			? new ResponseEntity<>(bookingList, HttpStatus.OK)
			: new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process Status " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/guest/{email},{phone}")
	public ResponseEntity<Object> insertByGuest(@PathVariable String email, @PathVariable String phone)
	throws ConnectException, SQLException {

		try {
			Booking newBooking = bookingService.insertByBookingGuest(email, phone);
			return new ResponseEntity<>(newBooking, HttpStatus.CREATED);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);

		} catch(BookingAlreadyExistsException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.CONFLICT);
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
			return new ResponseEntity<>("Cannot process UserID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);

		} catch(BookingAlreadyExistsException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("{bookingIdString},{statusIdString}")
	public ResponseEntity<Object> update(@PathVariable String bookingIdString, @PathVariable String statusIdString)
	throws ConnectException, SQLException {

		try {
			Integer bookingId = Integer.parseInt(bookingIdString);
			Integer status = Integer.parseInt(statusIdString);
			Booking newBooking = bookingService.update(bookingId, status);
			return new ResponseEntity<>(newBooking, HttpStatus.ACCEPTED);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process UserID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);

		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/guest/{bookingIdString}/{email},{phone}")
	public ResponseEntity<Object> updateBookingGuest(@PathVariable String bookingIdString, @PathVariable String email, @PathVariable String phone)
	throws ConnectException, SQLException {

		try {
			Integer bookingId = Integer.parseInt(bookingIdString);
			BookingGuest newBookingGuest = bookingGuestService.update(bookingId, email, phone);
			return new ResponseEntity<>(newBookingGuest, HttpStatus.ACCEPTED);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>("Cannot process UserID " + err.getMessage()
			.substring(0, 1).toLowerCase() + err.getMessage()
			.substring(1, err.getMessage().length()), HttpStatus.BAD_REQUEST);

		} catch(BookingGuestNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{bookingId}")
	public ResponseEntity<Object> delete(@PathVariable Integer bookingId)
	throws ConnectException, SQLException  {

		try {
			bookingService.delete(bookingId);
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<Object> deleteByConfirmationCode(@PathVariable String confirmationCode)
	throws ConnectException, SQLException  {

		try {
			Booking booking = bookingService.findByConfirmationCode(confirmationCode);
			bookingService.delete(booking.getId());
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch(IllegalArgumentException | NullPointerException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
		} catch(BookingNotFoundException err) {
			return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@ExceptionHandler(ConnectException.class)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>("Invalid Message Content!", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
