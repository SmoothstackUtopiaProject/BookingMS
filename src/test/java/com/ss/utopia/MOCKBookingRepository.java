package com.ss.utopia;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingGuest;
import com.ss.utopia.models.BookingUser;
import com.ss.utopia.models.BookingWithReferenceData;
import com.ss.utopia.models.FlightBooking;
import com.ss.utopia.models.Passenger;

public class MOCKBookingRepository {

  private static final Booking testBooking = new Booking(1, "ACTIVE", "AFHAJKFHKAJS");
  private static final BookingWithReferenceData testBookingWithReferenceData = new BookingWithReferenceData(
    1, "ACTIVE", "AFHAJKFHKAJS", 27, 7, 75, "xyz@gmail.com", "7895437665"
  );

  private static final Booking[] testBookings = {
    new Booking(1, "ACTIVE", "bhdjkHKKKAJS"),
    new Booking(2, "ONHOLD", "UOIUHKJAHSAS"),
    new Booking(3, "INACTIVE", "WYTWHJKASFHJ"),
    new Booking(4, "ACTIVE", "PIPOIMBJJSSJ"),
    new Booking(5, "INACTIVE", "RTYCGZNCBCCC"),
    new Booking(6, "ACTIVE", "MOKJOIASJKHD"),
    new Booking(7, "INACTIVE", "UIASHASJKKZC"),
    new Booking(8, "ACTIVE", "QQWASNDAJSDK"),
  };

  private static final BookingWithReferenceData[] testBookingWithReferenceDatas = {
    new BookingWithReferenceData(1, "ACTIVE", "bhdjkHKKKAJS", 28, 7, 76, "xyz@gmail.com", "7895437665"),
    new BookingWithReferenceData(2, "ONHOLD", "UOIUHKJAHSAS", 29, 8, 77, "axyz@gmail.com", "1568945621"),
    new BookingWithReferenceData(3, "INACTIVE", "WYTWHJKASFHJ", 30, 9, 78, "abxyz@gmail.com", "6987641235"),
    new BookingWithReferenceData(4, "ACTIVE", "PIPOIMBJJSSJ", 31, 10, 79, "abcxyz@gmail.com", "7853216584"),
    new BookingWithReferenceData(5, "INACTIVE", "RTYCGZNCBCCC", 32, 11, 80, "abcdxyz@gmail.com", "9874215684"),
    new BookingWithReferenceData(6, "ACTIVE", "MOKJOIASJKHD", 33, 12, 81, "123xyz@gmail.com", "9871542365"),
    new BookingWithReferenceData(7, "INACTIVE", "UIASHASJKKZC", 34, 13, 82, "Johnxyz@gmail.com", "1854765623"),
    new BookingWithReferenceData(8, "ACTIVE", "QQWASNDAJSDK", 35, 14, 83, "Billyxyz@gmail.com", "9876592487"),
  };

  private static final BookingGuest[] testBookingGuests = {
    new BookingGuest(1, "xyz@gmail.com", "7895437665"),
    new BookingGuest(2, "axyz@gmail.com", "1568945621"),
    new BookingGuest(3, "abxyz@gmail.com", "6987641235"),
    new BookingGuest(4, "abcxyz@gmail.com", "7853216584"),
    new BookingGuest(5, "abcdxyz@gmail.com", "9874215684"),
    new BookingGuest(6, "123xyz@gmail.com", "9871542365"),
    new BookingGuest(7, "Johnxyz@gmail.com", "1854765623"),
    new BookingGuest(8, "Billyxyz@gmail.com", "9876592487"),
  };

  private static final BookingUser[] testBookingUsers = {
    new BookingUser(1, 76),
    new BookingUser(2, 77),
    new BookingUser(3, 78),
    new BookingUser(4, 79),
    new BookingUser(5, 80),
    new BookingUser(6, 81),
    new BookingUser(7, 82),
    new BookingUser(8, 83),
  };

  private static final FlightBooking[] testFlightBookings = {
    new FlightBooking(28, 1),
    new FlightBooking(29, 2),
    new FlightBooking(30, 3),
    new FlightBooking(31, 4),
    new FlightBooking(32, 5),
    new FlightBooking(33, 6),
    new FlightBooking(34, 7),
    new FlightBooking(35, 8),
  };

  private static final Passenger[] testPassengers = {
    new Passenger(7, 1, "bhdjkHKKKAJS", "FirstName2", "LastName2", "1988-1-9", "MALE", "8880 Woodsman Street Marquette, MI 49855", false),
    new Passenger(8, 2, "UOIUHKJAHSAS", "FirstName3", "LastName3", "1989-7-3", "FEMALE", "530 Homestead Rd. North Miami Beach, FL 33160", false),
    new Passenger(9, 3, "WYTWHJKASFHJ", "FirstName4", "LastName4", "1988-1-9", "MALE", "75 Amherst Dr. Raleigh, NC 27603", false),
    new Passenger(10, 4, "PIPOIMBJJSSJ", "FirstName5", "LastName5", "2003-7-3", "FEMALE", "495 Henry Smith Road Rowlett, TX 75088", false),
    new Passenger(11, 5, "RTYCGZNCBCCC", "FirstName6", "LastName6", "1956-1-9", "MALE", "107 Greenrose St. Brownsburg, IN 46112", true),
    new Passenger(12, 6, "MOKJOIASJKHD", "FirstName7", "LastName7", "1972-7-3", "FEMALE", "9329 West Lakeshore St. Parkville, MD 21234", true),
    new Passenger(13, 7, "UIASHASJKKZC", "FirstName8", "LastName8", "1994-1-9", "MALE", "8109 Jefferson Drive Holland, MI 49423", false),
    new Passenger(14, 8, "QQWASNDAJSDK", "FirstName9", "LastName9", "2011-7-3", "FEMALE", "670 Gartner Dr. Shakopee, MN 55379", false),
  };


  public static Booking getTestBooking() {
    return testBooking;
  }

  public static BookingWithReferenceData getTestBookingWithReferenceData() {
    return testBookingWithReferenceData;
  }

  public static List<Booking> getTestBookingList() {
    return Arrays.asList(testBookings);
  }

  public static List<BookingWithReferenceData> getTestBookingWithReferenceDataList() {
    return Arrays.asList(testBookingWithReferenceDatas);
  }

  public static List<Booking> findAllWithResults() {
    return getTestBookingList();
  }

  public static List<Booking> findAllWithNoResults() {
    List<Booking> emptyBookingList = Arrays.asList();
    return emptyBookingList;
  }

  public static List<BookingWithReferenceData> findAllWithReferenceDataWithResults() {
    return getTestBookingWithReferenceDataList();
  }

  public static List<BookingWithReferenceData> findAllWithReferenceDataWithNoResults() {
    List<BookingWithReferenceData> emptyBookingWithReferenceDataList = Arrays.asList();
    return emptyBookingWithReferenceDataList;
  }

  public static List<BookingGuest> findAllBookingGuests() {
    return Arrays.asList(testBookingGuests);
  }

  public static List<BookingUser> findAllBookingUsers() {
    return Arrays.asList(testBookingUsers);
  }

  public static List<FlightBooking> findAllFlightBookings() {
    return Arrays.asList(testFlightBookings);
  }

  public static List<Passenger> findAllPassengers() {
    return Arrays.asList(testPassengers);
  }

  public static Optional<Booking> findById(Integer id) {
    if(id > 8 || id < 1) {
      return Optional.empty();
    }
    return Optional.of(testBookings[(id - 1)]);
  }

  public static Booking save(Booking booking) {
    return booking;
  }

  public static void deleteById() {}
}