package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.BookingNotFoundException;
import com.ss.utopia.exceptions.BookingUserNotFoundException;
import com.ss.utopia.models.BookingWithReferenceData;
import com.ss.utopia.services.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class BookingControllerTest {

  private final String SERVICE_PATH_BOOKINGS = "/bookings";
  private final ObjectMapper mapper = new ObjectMapper();

  @InjectMocks
  private BookingController controller;

  @Mock
  private BookingService service;

  private MockMvc mvc;
  private HttpHeaders headers;

  @BeforeEach
  void setup() throws Exception {
    mvc = MockMvcBuilders.standaloneSetup(controller).build();
    Mockito.reset(service);
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
  }


  // validateModel
  //=======================================================================
  @Test
  void test_validBookingTestModel() throws Exception {
    assertEquals(Integer.valueOf(1), MOCKBookingService.getTestBooking().getBookingId());
    assertEquals("ACTIVE", MOCKBookingService.getTestBooking().getBookingStatus());
    assertEquals("AFHAJKFHKAJS", MOCKBookingService.getTestBooking().getBookingConfirmationCode());
  }


  // healthCheck
  //======================================================================= 
  @Test
  void test_healthCheck_thenStatus200() throws Exception {
    when(service.findAll()).thenReturn(MOCKBookingService.findAllWithResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_BOOKINGS + "/health")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals("\"status\": \"up\"", response.getResponse().getContentAsString());
  }


  // findAll
  //=======================================================================
  @Test
  void test_findAllBookings_withValidBookings_thenStatus200() throws Exception {
    when(service.findAll()).thenReturn(MOCKBookingService.findAllWithResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKBookingService.getTestBookingList()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findAllBookings_withNoValidBookings_thenStatus204() throws Exception {
    when(service.findAll()).thenReturn(MOCKBookingService.findAllWithNoResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      )
      .andExpect(status().is(204))
      .andReturn();

    assertEquals("", response.getResponse().getContentAsString());
  }

  // findAllWithReferenceData
  //=======================================================================
  @Test
  void test_findAllBookingsWithReferenceData_withValidBookings_thenStatus200() throws Exception {
    when(service.findAllWithReferenceData()).thenReturn(MOCKBookingService.findAllWithReferenceDataWithResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_BOOKINGS + "/referencedata")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKBookingService.getTestBookingWithReferenceDataList()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findAllBookingsWithReferenceData_withNoValidBookings_thenStatus204() throws Exception {
    when(service.findAllWithReferenceData()).thenReturn(MOCKBookingService.findAllWithReferenceDataWithNoResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_BOOKINGS + "/referencedata")
      .headers(headers)
      )
      .andExpect(status().is(204))
      .andReturn();

    assertEquals("", response.getResponse().getContentAsString());
  }


  // findById
  //=======================================================================
  @Test
  void test_findById_withValidBooking_thenStatus200() throws Exception {
    when(service.findByIdWithReferenceData(1)).thenReturn(MOCKBookingService.findById(1));

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_BOOKINGS + "/1")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKBookingService.getTestBookingWithReferenceData()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findById_withInvalidBooking_thenStatus404() throws Exception {
    when(service.findByIdWithReferenceData(-1)).thenThrow(new BookingNotFoundException());

    mvc
      .perform(get(SERVICE_PATH_BOOKINGS + "/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_findById_withBadParams_thenStatus400() throws Exception {
    mvc
      .perform(get(SERVICE_PATH_BOOKINGS + "/NotAnInteger")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // findBySearchAndFilter
  //=======================================================================
  @Test
  void test_findBySearchAndFilter_withValidBookings_thenStatus200() throws Exception {
    
    // An empty filterMap through findBySearchAndFilter should return all
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", "");

    when(service.findBySearchAndFilter(filterMap)).thenReturn(MOCKBookingService.findAllWithReferenceDataWithResults());

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_BOOKINGS + "/search")
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKBookingService.getTestBookingWithReferenceDataList()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findBySearchAndFilter_withNoValidBookings_thenStatus204() throws Exception {
    
    // A bookingId filter of "-1" through findBySearchAndFilter should return empty
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", "-1");

    when(service.findBySearchAndFilter(filterMap)).thenReturn(MOCKBookingService.findAllWithReferenceDataWithNoResults());

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_BOOKINGS + "/search")
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(204))
      .andReturn();

    assertEquals("", response.getResponse().getContentAsString());
  }

  @Test
  void test_findBySearchAndFilter_withInvalidParams_thenStatus400() throws Exception {
    mvc
      .perform(post(SERVICE_PATH_BOOKINGS + "/search")
      .headers(headers)
      .content("NotAJSONObject")
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // insert
  //=======================================================================
  @Test
  void test_insert_withValidBooking_thenStatus201() throws Exception {
    
    BookingWithReferenceData testBooking = MOCKBookingService.getTestBookingWithReferenceData();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingUserId", testBooking.getBookingUserId().toString());
    filterMap.put("bookingFlightId", testBooking.getBookingFlightId().toString());
    filterMap.put("bookingGuestEmail", testBooking.getBookingGuestEmail());
    filterMap.put("bookingGuestPhone", testBooking.getBookingGuestPhone());

    when(service.insert(filterMap)).thenReturn(testBooking);

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(201))
      .andReturn();

    assertEquals(mapper.writeValueAsString(testBooking), response.getResponse().getContentAsString());
  }

  @Test
  void test_insert_withNonExistingBookingUser_thenStatus404() throws Exception {
    
    BookingWithReferenceData testBooking = MOCKBookingService.getTestBookingWithReferenceData();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingUserId", testBooking.getBookingUserId().toString());
    filterMap.put("bookingFlightId", testBooking.getBookingFlightId().toString());
    filterMap.put("bookingGuestEmail", testBooking.getBookingGuestEmail());
    filterMap.put("bookingGuestPhone", testBooking.getBookingGuestPhone());

    when(service.insert(filterMap)).thenThrow(new BookingUserNotFoundException());

    mvc
      .perform(post(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_insert_withInvalidParams_thenStatus400() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingUserId", "NOT_A_VALID_USER_ID");
    filterMap.put("bookingFlightId", "NOT_A_VALID_FLIGHT_ID");
    filterMap.put("bookingGuestEmail", "NOT_A_VALID_GUEST_EMAIL");
    filterMap.put("bookingGuestPhone", "NOT_A_VALID_GUEST_PHONE");

    when(service.insert(filterMap)).thenThrow(new NumberFormatException());

    mvc
      .perform(post(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // update
  //=======================================================================
  @Test
  void test_update_withValidBooking_thenStatus202() throws Exception {
    
    BookingWithReferenceData testBooking = MOCKBookingService.getTestBookingWithReferenceData();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", testBooking.getBookingId().toString());
    filterMap.put("bookingStatus", testBooking.getBookingStatus());
    filterMap.put("bookingUserId", testBooking.getBookingUserId().toString());
    filterMap.put("bookingFlightId", testBooking.getBookingFlightId().toString());
    filterMap.put("bookingGuestEmail", testBooking.getBookingGuestEmail());
    filterMap.put("bookingGuestPhone", testBooking.getBookingGuestPhone());

    when(service.update(filterMap)).thenReturn(testBooking);

    MvcResult response = mvc
      .perform(put(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(202))
      .andReturn();

    assertEquals(mapper.writeValueAsString(testBooking), response.getResponse().getContentAsString());
  }

  @Test
  void test_update_withNonExistingBooking_thenStatus404() throws Exception {
    
    BookingWithReferenceData testBooking = MOCKBookingService.getTestBookingWithReferenceData();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", "-1");
    filterMap.put("bookingStatus", testBooking.getBookingStatus());
    filterMap.put("bookingUserId", testBooking.getBookingUserId().toString());
    filterMap.put("bookingFlightId", testBooking.getBookingFlightId().toString());
    filterMap.put("bookingGuestEmail", testBooking.getBookingGuestEmail());
    filterMap.put("bookingGuestPhone", testBooking.getBookingGuestPhone());

    when(service.update(filterMap)).thenThrow(new BookingNotFoundException());

    mvc
      .perform(put(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_update_withNonExistingBookingUser_thenStatus404() throws Exception {
    
    BookingWithReferenceData testBooking = MOCKBookingService.getTestBookingWithReferenceData();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", testBooking.getBookingId().toString());
    filterMap.put("bookingStatus", testBooking.getBookingStatus());
    filterMap.put("bookingUserId", "-1");
    filterMap.put("bookingFlightId", testBooking.getBookingFlightId().toString());
    filterMap.put("bookingGuestEmail", testBooking.getBookingGuestEmail());
    filterMap.put("bookingGuestPhone", testBooking.getBookingGuestPhone());

    when(service.update(filterMap)).thenThrow(new BookingUserNotFoundException());

    mvc
      .perform(put(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_update_withInvalidParams_thenStatus400() throws Exception {
    
    BookingWithReferenceData testBooking = MOCKBookingService.getTestBookingWithReferenceData();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("bookingId", "NOT_A_VALID_BOOKING_ID");
    filterMap.put("bookingStatus", testBooking.getBookingStatus());
    filterMap.put("bookingUserId", testBooking.getBookingUserId().toString());
    filterMap.put("bookingFlightId", testBooking.getBookingFlightId().toString());
    filterMap.put("bookingGuestEmail", testBooking.getBookingGuestEmail());
    filterMap.put("bookingGuestPhone", testBooking.getBookingGuestPhone());

    when(service.update(filterMap)).thenThrow(new NumberFormatException());

    mvc
      .perform(put(SERVICE_PATH_BOOKINGS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // delete
  //=======================================================================
  @Test
  void test_delete_withValidBooking_thenStatus202() throws Exception {

    mvc
      .perform(delete(SERVICE_PATH_BOOKINGS + "/1")
      .headers(headers)
      )
      .andExpect(status().is(202))
      .andReturn();
  }

  @Test
  void test_delete_withNonExistingBooking_thenStatus404() throws Exception {
    
    when(service.delete(-1)).thenThrow(new BookingNotFoundException());

    mvc
      .perform(delete(SERVICE_PATH_BOOKINGS + "/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_delete_withInvalidParams_thenStatus400() throws Exception {
    mvc
      .perform(delete(SERVICE_PATH_BOOKINGS + "/NOT_AN_INTEGER")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }
}
