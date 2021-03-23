package com.ss.utopia;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.BookingAlreadyExistsException;
import com.ss.utopia.exceptions.BookingGuestNotFoundException;
import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.ErrorMessage;
import com.ss.utopia.models.BookingWithReferenceData;
import com.ss.utopia.services.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;

	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<Object> findAll() {
		List<Booking> bookingList = bookingService.findAll();
		return !bookingList.isEmpty() 
		? new ResponseEntity<>(bookingList, HttpStatus.OK)
		: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/referencedata")
	public ResponseEntity<Object> findAllWithReferenceData() {
		List<BookingWithReferenceData> bookingList = bookingService.findAllWithReferenceData();
		return !bookingList.isEmpty() 
			? new ResponseEntity<>(bookingList, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("{bookingId}")
	public ResponseEntity<Object> findById(@PathVariable String bookingId) 
	throws BookingNotFoundException, NumberFormatException {
		Integer formattedBookingId = Integer.parseInt(bookingId);
		BookingWithReferenceData bookingWithReferenceData = bookingService.findByIdWithReferenceData(formattedBookingId);
		return new ResponseEntity<>(bookingWithReferenceData, HttpStatus.OK);
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap) 
	throws NumberFormatException {
		List<BookingWithReferenceData> bookings = bookingService.findBySearchAndFilter(filterMap);
		return !bookings.isEmpty()
			? new ResponseEntity<>(bookings, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping()
	public ResponseEntity<Object> insert(@RequestBody Map<String, String> bookingMap) 
	throws BookingUserNotFoundException, NumberFormatException {
		BookingWithReferenceData newBooking = bookingService.insert(bookingMap);
		return new ResponseEntity<>(newBooking, HttpStatus.CREATED); 
	}

	@PutMapping()
	public ResponseEntity<Object> update(@RequestBody Map<String, String> bookingMap) 
	throws BookingNotFoundException, BookingUserNotFoundException, NumberFormatException {
		BookingWithReferenceData updatedBooking = bookingService.update(bookingMap);
		return new ResponseEntity<>(updatedBooking, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("{bookingId}")
	public ResponseEntity<Object> delete(@PathVariable String bookingId) 
	throws BookingNotFoundException, NumberFormatException {
		Integer formattedBookingId = Integer.parseInt(bookingId);
		String deleteInformation = bookingService.delete(formattedBookingId);
		return new ResponseEntity<>(deleteInformation, HttpStatus.ACCEPTED);
	}


	// Exceptions
	// ====================================================================================
	@ExceptionHandler(BookingAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> bookingAlreadyExistsException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.CONFLICT
		);
	}

	@ExceptionHandler(BookingNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> bookingNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(BookingUserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> bookingUserNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(BookingGuestNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> bookingGuestNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(ConnectException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(
			new ErrorMessage("Invalid HTTP message content."), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> invalidParameters(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}
}
