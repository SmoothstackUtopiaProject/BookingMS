package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.BookingGuestNotFoundException;
import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
import com.ss.utopia.filters.BookingFilters;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingWithReferenceData;
import com.ss.utopia.models.Flight;
import com.ss.utopia.models.Role;
import com.ss.utopia.models.User;
import com.ss.utopia.repositories.BookingRepository;
import com.ss.utopia.repositories.FlightBookingRepository;
import com.ss.utopia.repositories.PassengerRepository;
import com.ss.utopia.services.BookingGuestService;
import com.ss.utopia.services.BookingService;
import com.ss.utopia.services.BookingUserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookingServiceTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @InjectMocks
  private BookingService service;

  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private BookingGuestService bookingGuestService;

  @Mock
  private BookingUserService bookingUserService;

  @Mock
  private FlightBookingRepository flightBookingRepository;

  @Mock
  private PassengerRepository passengerRepository;

  @BeforeEach
  void setup() throws Exception {
    Mockito.reset(bookingRepository);
    Mockito.reset(bookingGuestService);
    Mockito.reset(bookingUserService);
    Mockito.reset(flightBookingRepository);
    Mockito.reset(passengerRepository);
  }

  // Validate Models
  //=======================================================================
  @Test
  void test_validBookingTestModel() throws Exception {
    assertEquals(Integer.valueOf(1), MOCKBookingRepository.getTestBooking().getBookingId());
    assertEquals("ACTIVE", MOCKBookingRepository.getTestBooking().getBookingStatus());
    assertEquals("AFHAJKFHKAJS", MOCKBookingRepository.getTestBooking().getBookingConfirmationCode());
  }

  @Test
  void test_validBookingWithReferenceDataTestModel() throws Exception {
    assertEquals(Integer.valueOf(1), MOCKBookingRepository.getTestBookingWithReferenceData().getBookingId());
    assertEquals("ACTIVE", MOCKBookingRepository.getTestBookingWithReferenceData().getBookingStatus());
    assertEquals("AFHAJKFHKAJS", MOCKBookingRepository.getTestBookingWithReferenceData().getBookingConfirmationCode());
    assertEquals(Integer.valueOf(27), MOCKBookingRepository.getTestBookingWithReferenceData().getBookingFlightId());
    assertEquals(Integer.valueOf(7), MOCKBookingRepository.getTestBookingWithReferenceData().getBookingPassengerId());
    assertEquals(Integer.valueOf(75), MOCKBookingRepository.getTestBookingWithReferenceData().getBookingUserId());
    assertEquals("xyz@gmail.com", MOCKBookingRepository.getTestBookingWithReferenceData().getBookingGuestEmail());
    assertEquals("7895437665", MOCKBookingRepository.getTestBookingWithReferenceData().getBookingGuestPhone());
  }

  // findAll
  //=======================================================================
  @Test
  void test_findAll_WithValidResults() throws Exception {
    when(bookingRepository.findAll()).thenReturn(MOCKBookingRepository.findAllWithResults());
    assertEquals(MOCKBookingRepository.findAllWithResults(), service.findAll());
  }

  @Test
  void test_findAll_WithInvalidResults() throws Exception {
    when(bookingRepository.findAll()).thenReturn(MOCKBookingRepository.findAllWithNoResults());
    assertEquals(MOCKBookingRepository.findAllWithNoResults(), service.findAll());
  }

  // findAllWithReferenceData
  //=======================================================================
  @Test
  void test_findAllWithReferenceData_MatchesResults() throws Exception {
    when(bookingRepository.findAll()).thenReturn(MOCKBookingRepository.findAllWithResults());
    when(bookingGuestService.findAll()).thenReturn(MOCKBookingRepository.findAllBookingGuests());
    when(bookingUserService.findAll()).thenReturn(MOCKBookingRepository.findAllBookingUsers());
    when(flightBookingRepository.findAll()).thenReturn(MOCKBookingRepository.findAllFlightBookings());
    when(passengerRepository.findAll()).thenReturn(MOCKBookingRepository.findAllPassengers());

    List<BookingWithReferenceData> bookingsWithReferenceDataExpected = MOCKBookingRepository.findAllWithReferenceDataWithResults();
    List<BookingWithReferenceData> bookingsWithReferenceDataActual = service.findAllWithReferenceData();
    for(int i = 0; i < bookingsWithReferenceDataExpected.size(); i++) {
      BookingWithReferenceData expected = bookingsWithReferenceDataExpected.get(i);
      BookingWithReferenceData actual = bookingsWithReferenceDataActual.get(i);

      assertEquals(expected.getBookingId(), actual.getBookingId());
      assertEquals(expected.getBookingStatus(), actual.getBookingStatus());
      assertEquals(expected.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
      assertEquals(expected.getBookingFlightId(), actual.getBookingFlightId());
      assertEquals(expected.getBookingPassengerId(), actual.getBookingPassengerId());
      assertEquals(expected.getBookingUserId(), actual.getBookingUserId());
      assertEquals(expected.getBookingGuestEmail(), actual.getBookingGuestEmail());
      assertEquals(expected.getBookingGuestPhone(), actual.getBookingGuestPhone());
    }
  }

  // findById
  //=======================================================================
  @Test
  void test_findById_WithValidResults() throws Exception {
    when(bookingRepository.findById(1)).thenReturn(MOCKBookingRepository.findById(1));
    assertEquals(MOCKBookingRepository.getTestBookingList().get(0), service.findById(1));
  }

  @Test
  void test_findById_WithInvalidResults() throws Exception {
    when(bookingRepository.findById(-1)).thenReturn(MOCKBookingRepository.findById(-1));
    assertThrows(BookingNotFoundException.class, () -> service.findById(-1));
  }

  // findByIdWithReferenceData
  //=======================================================================
  @Test
  void test_findByIdWithReferenceData_WithValidResults() throws Exception {
    when(bookingRepository.findById(1)).thenReturn(MOCKBookingRepository.findById(1));
    when(bookingGuestService.findByBookingId(1)).thenReturn(MOCKBookingRepository.findAllBookingGuests().get(0));
    when(bookingUserService.findByBookingId(1)).thenReturn(MOCKBookingRepository.findAllBookingUsers().get(0));
    when(flightBookingRepository.findById(1)).thenReturn(Optional.of(MOCKBookingRepository.findAllFlightBookings().get(0)));
    when(passengerRepository.findByBookingId(1)).thenReturn(Optional.of(MOCKBookingRepository.findAllPassengers().get(0)));

    BookingWithReferenceData expected = MOCKBookingRepository.getTestBookingWithReferenceDataList().get(0);
    BookingWithReferenceData actual = service.findByIdWithReferenceData(1);

    assertEquals(expected.getBookingId(), actual.getBookingId());
    assertEquals(expected.getBookingStatus(), actual.getBookingStatus());
    assertEquals(expected.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
    assertEquals(expected.getBookingFlightId(), actual.getBookingFlightId());
    assertEquals(expected.getBookingPassengerId(), actual.getBookingPassengerId());
    assertEquals(expected.getBookingUserId(), actual.getBookingUserId());
    assertEquals(expected.getBookingGuestEmail(), actual.getBookingGuestEmail());
    assertEquals(expected.getBookingGuestPhone(), actual.getBookingGuestPhone());
  }

  @Test
  void test_findByIdWithReferenceData_WithInvalidResults() throws Exception {
    when(bookingRepository.findById(-1)).thenReturn(MOCKBookingRepository.findById(-1));
    assertThrows(BookingNotFoundException.class, () -> service.findByIdWithReferenceData(-1));
  }

    // findBySearchAndFilter
  //=======================================================================
  @Test
  void test_findBySearchAndFilter_SearchSingle_WithResults() throws Exception {
    String searchTerm1 = "ACTIVE";
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm1);

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    for(int i = 0; i < searchAndFiterResults.size(); i++) {
      String bookingAsString = mapper.writeValueAsString(searchAndFiterResults.get(i));
      assertTrue(bookingAsString.contains(searchTerm1));
    }
  }

  @Test
  void test_findBySearchAndFilter_SearchSingle_WithNoResults() throws Exception {
    String searchTerm1 = "NOT_SOMETHING_IN_BOOKING";
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm1);

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_SearchMulti_WithResults() throws Exception {
    String searchTerm1 = "ACTIVE";
    String searchTerm2 = "7895437665";
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm1 + ", " + searchTerm2);

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    for(int i = 0; i < searchAndFiterResults.size(); i++) {
      String bookingAsString = mapper.writeValueAsString(searchAndFiterResults.get(i));
      assertTrue(bookingAsString.contains(searchTerm1));
      assertTrue(bookingAsString.contains(searchTerm2));
    }
  }

  @Test
  void test_findBySearchAndFilter_SearchMulti_WithNoResults() throws Exception {
    String searchTerm1 = "ACTIVE";
    String searchTerm2 = "9999999999999";
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm1 + ", " + searchTerm2);

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_FilterBookingID_WithResult() throws Exception {;
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", "4");

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(1, searchAndFiterResults.size());
    assertEquals(4, searchAndFiterResults.get(0).getBookingId());
  }

  @Test
  void test_findBySearchAndFilter_FilterBookingID_WithNoResult() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", "-1");

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_FilterBookingPassengerID_WithResult() throws Exception {;
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingPassengerId", "8");

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(1, searchAndFiterResults.size());
    assertEquals(8, searchAndFiterResults.get(0).getBookingPassengerId());
  }

  @Test
  void test_findBySearchAndFilter_FilterBookingPassengerID_WithNoResult() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingPassengerId", "-1");

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_FilterBookingUserID_WithResult() throws Exception {;
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingUserId", "82");

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(1, searchAndFiterResults.size());
    assertEquals(82, searchAndFiterResults.get(0).getBookingUserId());
  }

  @Test
  void test_findBySearchAndFilter_FilterBookingUserID_WithNoResult() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingUserId", "-1");

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_FilterMulti_WithResult() throws Exception {;
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", "1");
    filterMap.put("bookingPassengerId", "7");
    filterMap.put("bookingUserId", "76");

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(1, searchAndFiterResults.size());
    assertEquals(1, searchAndFiterResults.get(0).getBookingId());
    assertEquals(7, searchAndFiterResults.get(0).getBookingPassengerId());
    assertEquals(76, searchAndFiterResults.get(0).getBookingUserId());
  }

  @Test
  void test_findBySearchAndFilter_FilterMulti_WithNoResult() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", "1");
    filterMap.put("bookingPassengerId", "-1");
    filterMap.put("bookingUserId", "76");

    List<BookingWithReferenceData> searchAndFiterResults = BookingFilters.apply(MOCKBookingRepository.getTestBookingWithReferenceDataList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  // insert
  //=======================================================================
  @Test
  void test_insert_byBookingUser_Valid() throws Exception {
    when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking(1, "INACTIVE", "IOIUAIODASFHKJ"));
    when(bookingUserService.findUserByUserId(1)).thenReturn(new User(1, Role.USER, "firstname", "lastname", "email", "password", "phone"));
    when(flightBookingRepository.findByFlightById(1)).thenReturn(Optional.of(new Flight(1, 1, 1, "departTime", 1, 42000, "ACTIVE")));

    Map<String, String> bookingMap = new HashMap<>();
    bookingMap.put("bookingUserId", "1");
    bookingMap.put("bookingFlightId", "1");

    BookingWithReferenceData expected = new BookingWithReferenceData(1, "INACTIVE", "IOIUAIODASFHKJ", 1, null, 1, null, null);
    BookingWithReferenceData actual = service.insert(bookingMap);
    assertEquals(expected.getBookingId(), actual.getBookingId());
    assertEquals(expected.getBookingStatus(), actual.getBookingStatus());
    assertEquals(expected.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
    assertEquals(expected.getBookingFlightId(), actual.getBookingFlightId());
    assertEquals(expected.getBookingPassengerId(), actual.getBookingPassengerId());
    assertEquals(expected.getBookingUserId(), actual.getBookingUserId());
    assertEquals(expected.getBookingGuestEmail(), actual.getBookingGuestEmail());
    assertEquals(expected.getBookingGuestPhone(), actual.getBookingGuestPhone());
  }

  @Test
  void test_insert_byBookingUser_Invalid() throws Exception {
    when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking(1, "INACTIVE", "IOIUAIODASFHKJ"));
    when(bookingUserService.findUserByUserId(1)).thenThrow(new BookingUserNotFoundException());
    when(flightBookingRepository.findByFlightById(1)).thenReturn(Optional.of(new Flight(1, 1, 1, "departTime", 1, 42000, "ACTIVE")));

    Map<String, String> bookingMap = new HashMap<>();
    bookingMap.put("bookingUserId", "1");
    bookingMap.put("bookingFlightId", "1");

    assertThrows(BookingUserNotFoundException.class, () -> service.insert(bookingMap));
  }

  @Test
  void test_insert_byBookingUser_BadParams() throws Exception {
    Map<String, String> bookingMap = new HashMap<>();
    bookingMap.put("bookingUserId", "NOT_AN_INTEGER");
    bookingMap.put("bookingFlightId", "1");

    assertThrows(NumberFormatException.class, () -> service.insert(bookingMap));
  }

  @Test
  void test_insert_byBookingGuest_Valid() throws Exception {
    when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking(1, "INACTIVE", "IOIUAIODASFHKJ"));
    when(flightBookingRepository.findByFlightById(1)).thenReturn(Optional.of(new Flight(1, 1, 1, "departTime", 1, 42000, "ACTIVE")));

    Map<String, String> bookingMap = new HashMap<>();
    bookingMap.put("bookingGuestEmail", "email@org.com");
    bookingMap.put("bookingGuestPhone", "46546546554");
    bookingMap.put("bookingFlightId", "1");

    BookingWithReferenceData expected = new BookingWithReferenceData(1, "INACTIVE", "IOIUAIODASFHKJ", 1, null, null, "email@org.com", "46546546554");
    BookingWithReferenceData actual = service.insert(bookingMap);
    assertEquals(expected.getBookingId(), actual.getBookingId());
    assertEquals(expected.getBookingStatus(), actual.getBookingStatus());
    assertEquals(expected.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
    assertEquals(expected.getBookingFlightId(), actual.getBookingFlightId());
    assertEquals(expected.getBookingPassengerId(), actual.getBookingPassengerId());
    assertEquals(expected.getBookingUserId(), actual.getBookingUserId());
    assertEquals(expected.getBookingGuestEmail(), actual.getBookingGuestEmail());
    assertEquals(expected.getBookingGuestPhone(), actual.getBookingGuestPhone());
  }

  // update
  //=======================================================================
  @Test
  void test_update_Valid() throws Exception {
    when(bookingRepository.findById(1)).thenReturn(Optional.of(new Booking(1, "INACTIVE", "IOIUAIODASFHKJ")));
    when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking(1, "ACTIVE", "IOIUAIODASFHKJ"));
    when(bookingUserService.findByBookingId(1)).thenReturn(MOCKBookingRepository.findAllBookingUsers().get(0));
    when(bookingUserService.findUserByUserId(1)).thenReturn(new User(1, Role.USER, "firstname", "lastname", "email", "password", "phone"));
    when(bookingGuestService.findByBookingId(1)).thenThrow(new BookingGuestNotFoundException());
    when(flightBookingRepository.findByFlightById(1)).thenReturn(Optional.of(new Flight(1, 1, 1, "departTime", 1, 42000, "ACTIVE")));
    when(passengerRepository.findByBookingId(1)).thenReturn(Optional.of(MOCKBookingRepository.findAllPassengers().get(0)));

    Map<String, String> bookingMap = new HashMap<>();
    bookingMap.put("bookingId", "1");
    bookingMap.put("bookingStatus", "ACTIVE");
    bookingMap.put("bookingUserId", "1");
    bookingMap.put("bookingFlightId", "1");
    bookingMap.put("bookingPassengerId", "7");

    BookingWithReferenceData expected = new BookingWithReferenceData(1, "ACTIVE", "IOIUAIODASFHKJ", 1, 7, 1, "", "");
    BookingWithReferenceData actual = service.update(bookingMap);
    assertEquals(expected.getBookingId(), actual.getBookingId());
    assertEquals(expected.getBookingStatus(), actual.getBookingStatus());
    assertEquals(expected.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
    assertEquals(expected.getBookingFlightId(), actual.getBookingFlightId());
    assertEquals(expected.getBookingPassengerId(), actual.getBookingPassengerId());
    assertEquals(expected.getBookingUserId(), actual.getBookingUserId());
    assertEquals(expected.getBookingGuestEmail(), actual.getBookingGuestEmail());
    assertEquals(expected.getBookingGuestPhone(), actual.getBookingGuestPhone());
  }

  @Test
  void test_update_Invalid() throws Exception {
    when(bookingRepository.findById(1)).thenReturn(Optional.empty());

    Map<String, String> bookingMap = new HashMap<>();
    bookingMap.put("bookingId", "1");
    bookingMap.put("bookingStatus", "ACTIVE");

    assertThrows(BookingNotFoundException.class, () -> service.update(bookingMap));
  }

  @Test
  void test_update_BadParams() throws Exception {

    Map<String, String> bookingMap = new HashMap<>();
    bookingMap.put("bookingId", "NOT_A_VALID_INTEGER");
    bookingMap.put("bookingStatus", "ACTIVE");

    assertThrows(NumberFormatException.class, () -> service.update(bookingMap));
  }

  // delete
  //=======================================================================
  @Test
  void test_delete_Valid() throws Exception {
    when(bookingRepository.findById(1)).thenReturn(Optional.of(new Booking(1, "INACTIVE", "IOIUAIODASFHKJ")));
    assertEquals("Booking with ID: 1 was deleted.", service.delete(1));
  }

  @Test
  void test_delete_Invalid() throws Exception {
    when(bookingRepository.findById(1)).thenReturn(Optional.empty());
    assertThrows(BookingNotFoundException.class, () -> service.delete(1));
  }
}