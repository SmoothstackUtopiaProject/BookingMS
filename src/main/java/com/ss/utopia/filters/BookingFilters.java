package com.ss.utopia.filters;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.ss.utopia.models.BookingWithReferenceData;

public final class BookingFilters {

  private static final String BOOKING_ID = "bookingId";
  private static final String BOOKING_ID_USER = "bookingUserId";
  private static final String BOOKING_ID_PASSENGER = "bookingPassengerId";
  private static final String SEARCH_TERMS = "searchTerms";

  private BookingFilters() {
    throw new IllegalStateException("Utilility class 'BookingFilters' is static and should not be instantiated.");
  }

  public static List<BookingWithReferenceData> apply
  (List<BookingWithReferenceData> bookings, Map<String, String> filterMap) {
    
    // ID
		if(filterMap.keySet().contains(BOOKING_ID)) {
			Integer parsedBookingId = Integer.parseInt(filterMap.get(BOOKING_ID));
			bookings = BookingFilters.filterByBookingId(bookings, parsedBookingId);
		}

		// Passenger ID
		if(filterMap.keySet().contains(BOOKING_ID_PASSENGER)) {
      Integer parsedBookingPassengerId = Integer.parseInt(filterMap.get(BOOKING_ID_PASSENGER));
			bookings = BookingFilters.filterByBookingPassengerId(bookings, parsedBookingPassengerId);
		}

		// User ID
		if(filterMap.keySet().contains(BOOKING_ID_USER)) {
			Integer parsedBookingUserId = Integer.parseInt(filterMap.get(BOOKING_ID_USER));
			bookings = BookingFilters.filterByBookingUserId(bookings, parsedBookingUserId);
		}

    // SearchTerms
    if(filterMap.keySet().contains(SEARCH_TERMS)) {
      bookings = BookingFilters.filterBySearchTerms(bookings, filterMap.get(SEARCH_TERMS));
    }

		return bookings;
  }

  public static List<BookingWithReferenceData> filterByBookingId 
  (Collection<BookingWithReferenceData> bookings, Integer bookingId) {
    return bookings.parallelStream()
      .filter(i -> i.getBookingId().equals(bookingId))
      .collect(Collectors.toList());
  }

  public static List<BookingWithReferenceData> filterByBookingPassengerId 
  (Collection<BookingWithReferenceData> bookings, Integer bookingPassengerId) {
    return bookings.parallelStream()
      .filter(i -> i.getBookingPassengerId().equals(bookingPassengerId))
      .collect(Collectors.toList());
  }

  public static List<BookingWithReferenceData> filterByBookingUserId 
  (Collection<BookingWithReferenceData> bookings, Integer bookingUserId) {
    return bookings.parallelStream()
      .filter(i -> i.getBookingUserId().equals(bookingUserId))
      .collect(Collectors.toList());
  }

  public static List<BookingWithReferenceData> filterBySearchTerms
  (List<BookingWithReferenceData> bookings, String searchTerms) {
    String formattedSearch = searchTerms.toLowerCase(Locale.getDefault()).replace(", ", ",");
    String[] splitTerms = formattedSearch.split(",");

    for(String searchTerm : splitTerms) {
      bookings = bookings.parallelStream()
      .filter((BookingWithReferenceData i) ->
          i.getBookingConfirmationCode().contains(searchTerm) ||
          i.getBookingFlightId().toString().contains(searchTerm) ||
          i.getBookingGuestEmail().contains(searchTerm) ||
          i.getBookingGuestPhone().contains(searchTerm) ||
          i.getBookingId().toString().contains(searchTerm) ||
          i.getBookingPassengerId().toString().contains(searchTerm) ||
          i.getBookingStatus().contains(searchTerm) ||
          i.getBookingUserId().toString().contains(searchTerm)
      ).collect(Collectors.toList());
    }
		return bookings;
	}
}
