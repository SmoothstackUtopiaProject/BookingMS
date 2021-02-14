package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exception.BookingAlreadyExistsException;
import com.ss.utopia.exception.BookingGuestNotFoundException;
import com.ss.utopia.exception.BookingNotFoundException;
import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingGuest;
import com.ss.utopia.services.BookingGuestService;
import com.ss.utopia.services.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Profile("test")
@SpringBootTest
public class BookingControllerTest {

  final String SERVICE_PATH_USERS = "/bookings";

  @Mock
  BookingService service;

  @Mock
  BookingGuestService guestService;

  @InjectMocks
  BookingController controller;

  MockMvc mvc;
  Booking testBooking;
  List<Booking> testBookingList;

	@BeforeEach
	void setup() throws Exception {
    mvc = MockMvcBuilders.standaloneSetup(controller).build();
    Mockito.reset(service);
    Mockito.reset(guestService);

    testBooking = new Booking(12, 1, "AFHJA-89066879JHKFA");

    testBookingList = new ArrayList<Booking>();
    testBookingList.add(new Booking(1, 1, "AHKJAFGKJLAH123"));
    testBookingList.add(new Booking(2, 1, "AHKJAFGKJLAH124"));
    testBookingList.add(new Booking(3, 1, "AHKJAFGKJLAH125"));
    testBookingList.add(new Booking(4, 2, "AHKJAFGKJLAH126"));
    testBookingList.add(new Booking(5, 2, "AHKJAFGKJLAH127"));
    testBookingList.add(new Booking(6, 1, "AHKJAFGKJLAH128"));
    testBookingList.add(new Booking(7, 1, "AHKJAFGKJLAH129"));
    testBookingList.add(new Booking(8, 0, "AHKJAFGKJLAH130"));
    testBookingList.add(new Booking(9, 0, "AHKJAFGKJLAH131"));
    testBookingList.add(new Booking(10, 3, "AHKJAFGKJLAH132"));
  }

  @Test
  void test_validBookingModel_getId() {
    assertEquals(12, testBooking.getId());
  }

  @Test
  void test_validBookingModel_getIsActive() {
    assertEquals(1, testBooking.getIsActive());
  }

  @Test
  void test_validBookingModel_getConfirmationCode() {
    assertEquals("AFHJA-89066879JHKFA", testBooking.getConfirmationCode());
  }

  @Test
  void test_findAllBookings_withValidBookings_thenStatus200() {    
    try {
      when(service.findAll()).thenReturn(testBookingList);

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();
  
      List<Booking> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        Booking[].class)).collect(Collectors.toList());
  
      assertEquals(testBookingList.size(), actual.size());
      for(int i = 0; i < testBookingList.size(); i++) {
        assertEquals(testBookingList.get(i).getId(), actual.get(i).getId());
        assertEquals(testBookingList.get(i).getIsActive(), actual.get(i).getIsActive());
        assertEquals(testBookingList.get(i).getConfirmationCode(), actual.get(i).getConfirmationCode());
      }
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findAllBookings_withNoValidBookings_thenStatus204() {    
    try {
      when(service.findAll()).thenReturn(Collections.emptyList());

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS)
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
  
      assertEquals("", response.getResponse().getContentAsString());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findById_withValidBooking_thenStatus200() {    
    try {
      when(service.findById(testBooking.getId())).thenReturn(testBooking);

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/" + testBooking.getId())
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();
  
      Booking actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), Booking.class);
  
      assertEquals(testBooking.getId(), actual.getId());
      assertEquals(testBooking.getIsActive(), actual.getIsActive());
      assertEquals(testBooking.getConfirmationCode(), actual.getConfirmationCode());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findById_withInvalidBooking_thenStatus404() {    
    try {
      Integer invalidId = -1;
      when(service.findById(invalidId)).thenThrow(new BookingNotFoundException());

      mvc.perform(get(SERVICE_PATH_USERS + "/" + invalidId)
      .header("Accept", "application/json"))
      .andExpect(status().is(404))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findById_withBadParams_thenStatus400() {    
    try {
      Integer invalidId = null;
      when(service.findById(invalidId)).thenThrow(new IllegalArgumentException());

      mvc.perform(get(SERVICE_PATH_USERS + "/" + invalidId)
      .header("Accept", "application/json"))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByConfirmationCode_withValidConfirmationCode_thenStatus200() {    
    try {
      String confirmation = testBooking.getConfirmationCode();
      when(service.findByConfirmationCode(confirmation)).thenReturn(testBooking);

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/confirmation/" + confirmation)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();
  
      Booking actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), Booking.class);
  
      assertEquals(testBooking.getId(), actual.getId());
      assertEquals(testBooking.getIsActive(), actual.getIsActive());
      assertEquals(testBooking.getConfirmationCode(), actual.getConfirmationCode());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByConfirmationCode_withInvalidConfirmationCode_thenStatus404() {    
    try {
      String invalidConfirmationCode = "notaconfirmationcode";
      when(service.findByConfirmationCode(invalidConfirmationCode)).thenThrow(new BookingNotFoundException());

      mvc.perform(get(SERVICE_PATH_USERS + "/confirmation/" + invalidConfirmationCode)
      .header("Accept", "application/json"))
      .andExpect(status().is(404))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByStatus_withValidBooking_singleResult_thenStatus200() {    
    try {
      String testStatusSearch = "3"; // Should only return one Booking
      when(service.findByStatus(testStatusSearch)).thenReturn(testBookingList.stream()
      .filter(i -> i.getIsActive().equals(3))
      .collect(Collectors.toList()));

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/search?status=" + testStatusSearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();

      List<Booking> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        Booking[].class)).collect(Collectors.toList());

      assertEquals(1, actual.size());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByStatus_withValidBookings_multiResult_thenStatus200() {    
    try {
      String testStatusSearch = "1"; // Should return exactly 5 Bookings
      when(service.findByStatus(testStatusSearch)).thenReturn(testBookingList.stream()
      .filter(i -> i.getIsActive().equals(1))
      .collect(Collectors.toList()));

      MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/search?status=" + testStatusSearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(200))
      .andReturn();

      List<Booking> actual = Arrays.stream(
        new ObjectMapper().readValue(response.getResponse().getContentAsString(),
        Booking[].class)).collect(Collectors.toList());

      assertEquals(5, actual.size());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByStatus_withNoValidBookings_thenStatus204() {    
    try {
      String testStatusSearch = "-1"; // Should return NO Bookings
      when(service.findByStatus(testStatusSearch)).thenReturn(testBookingList.stream()
      .filter(i -> i.getIsActive().equals(-1))
      .collect(Collectors.toList()));

      mvc.perform(get(SERVICE_PATH_USERS + "/search?status=" + testStatusSearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_findByStatus_withBadParams_thenStatus400() {    
    try {
      String testStatusSearch = "NotAnInt"; // Should trigger badparams as cannot be cast to an Integer
      mvc.perform(get(SERVICE_PATH_USERS + "/search?roleId=" + testStatusSearch)
      .header("Accept", "application/json"))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insertWithUser_withValidUser_thenStatus201() {    
    try {
      String testUserId = "1";
      when(service.insertByBookingUser(1)).thenReturn(testBooking);      

      MvcResult response = mvc.perform(post(SERVICE_PATH_USERS + "/user/" + testUserId)
      .header("Accept", "application/json"))
      .andExpect(status().is(201))
      .andReturn();

      Booking actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), Booking.class);

      assertEquals(testBooking.getId(), actual.getId());
      assertEquals(testBooking.getIsActive(), actual.getIsActive());
      assertEquals(testBooking.getConfirmationCode(), actual.getConfirmationCode());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insertWithUser_withDuplicateUser_thenStatus409() {    
    try {
      String testUserId = "1";
      when(service.insertByBookingUser(1)).thenThrow(new BookingAlreadyExistsException());  

      mvc.perform(post(SERVICE_PATH_USERS + "/user/" + testUserId)
      .header("Accept", "application/json"))
      .andExpect(status().is(409))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insertWithGuest_withValidGuest_thenStatus201() {    
    try {
      String testGuestEmail = "soso@gmail.com";
      String testGuestPhone = "5987643213";
      when(service.insertByBookingGuest(testGuestEmail, testGuestPhone)).thenReturn(testBooking);      

      MvcResult response = mvc.perform(post(SERVICE_PATH_USERS + "/guest/" + testGuestEmail + "," + testGuestPhone)
      .header("Accept", "application/json"))
      .andExpect(status().is(201))
      .andReturn();

      Booking actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), Booking.class);

      assertEquals(testBooking.getId(), actual.getId());
      assertEquals(testBooking.getIsActive(), actual.getIsActive());
      assertEquals(testBooking.getConfirmationCode(), actual.getConfirmationCode());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insertWithUser_withDuplicateGuest_thenStatus409() {    
    try {
      String testGuestEmail = "soso@gmail.com";
      String testGuestPhone = "5987643213";
      when(service.insertByBookingGuest(testGuestEmail, testGuestPhone)).thenThrow(new BookingAlreadyExistsException());      

      mvc.perform(post(SERVICE_PATH_USERS + "/guest/" + testGuestEmail + "," + testGuestPhone)
      .header("Accept", "application/json"))
      .andExpect(status().is(409))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insertGuest_withBadEmailParams_thenStatus400() {    
    try {
      String testGuestEmail = "notanemail";
      String testGuestPhone = "5987643213";  
      when(service.insertByBookingGuest(testGuestEmail, testGuestPhone)).thenThrow(new IllegalArgumentException());   

      mvc.perform(post(SERVICE_PATH_USERS + "/guest/" + testGuestEmail + "," + testGuestPhone)
      .header("Accept", "application/json"))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_insertGuest_withBadPhoneParams_thenStatus400() {    
    try {
      String testGuestEmail = "soso@gmail.com";
      String testGuestPhone = "notaphone";
      when(service.insertByBookingGuest(testGuestEmail, testGuestPhone)).thenThrow(new IllegalArgumentException()); 

      mvc.perform(post(SERVICE_PATH_USERS + "/guest/" + testGuestEmail + "," + testGuestPhone)
      .header("Accept", "application/json"))
      .andExpect(status().is(400))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_update_withValidBooking_thenStatus202() {    
    try {
      Booking newBooking = new Booking(testBooking.getId(), 7, testBooking.getConfirmationCode());
      when(service.update(newBooking.getId(), newBooking.getIsActive())).thenReturn(newBooking);

      MvcResult response = mvc.perform(put(SERVICE_PATH_USERS + "/" + newBooking.getId() + "," + newBooking.getIsActive())
      .header("Accept", "application/json"))
      .andExpect(status().is(202))
      .andReturn();

      Booking actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), Booking.class);

      assertEquals(newBooking.getId(), actual.getId());
      assertEquals(newBooking.getIsActive(), actual.getIsActive());
      assertEquals(newBooking.getConfirmationCode(), actual.getConfirmationCode());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_update_withInvalidBooking_thenStatus404() {    
    try {
      Booking newBooking = testBooking;
      when(service.update(newBooking.getId(), newBooking.getIsActive())).thenThrow(new BookingNotFoundException());

      mvc.perform(put(SERVICE_PATH_USERS + "/" + newBooking.getId() + "," + newBooking.getIsActive())
      .header("Accept", "application/json"))
      .andExpect(status().is(404))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_updateGuest_withValidGuest_thenStatus202() {    
    try {
      String testGuestEmail = "soso@gmail.com";
      String testGuestPhone = "5987643213";
      BookingGuest testBookingGuest = new BookingGuest(testBooking.getId(), testGuestEmail, testGuestPhone);
      when(guestService.update(testBookingGuest.getBookingId(), testBookingGuest.getEmail(), testBookingGuest.getPhone()))
      .thenReturn(testBookingGuest);    

      MvcResult response = mvc.perform(put(SERVICE_PATH_USERS + "/guest/" + testBookingGuest.getBookingId() + "/" + testBookingGuest.getEmail() + "," + testBookingGuest.getPhone())
      .header("Accept", "application/json"))
      .andExpect(status().is(202))
      .andReturn();

      BookingGuest actual = new ObjectMapper().readValue(response
      .getResponse().getContentAsString(), BookingGuest.class);

      assertEquals(testBookingGuest.getBookingId(), actual.getBookingId());
      assertEquals(testBookingGuest.getEmail(), actual.getEmail());
      assertEquals(testBookingGuest.getPhone(), actual.getPhone());
    } catch(Exception e) {
      fail();
    }
  }

  @Test
  void test_updateGuest_withInvalidGuest_thenStatus404() {    
    try {
      String testGuestEmail = "soso@gmail.com";
      String testGuestPhone = "5987643213";
      BookingGuest testBookingGuest = new BookingGuest(-1, testGuestEmail, testGuestPhone);
      when(guestService.update(testBookingGuest.getBookingId(), testBookingGuest.getEmail(), testBookingGuest.getPhone()))
      .thenThrow(new BookingGuestNotFoundException());    

      mvc.perform(put(SERVICE_PATH_USERS + "/guest/" + testBookingGuest.getBookingId() + "/" + testBookingGuest.getEmail() + "," + testBookingGuest.getPhone())
      .header("Accept", "application/json"))
      .andExpect(status().is(404))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }

  // @Test
  // void test_updateGuest_withBadEmailParams_thenStatus400() {    
  //   try {
  //     String testGuestEmailInvalid = "notanemail";
  //     String testGuestPhone = "5987643213";
  //     BookingGuest testBookingGuest = new BookingGuest(testBooking.getId(), testGuestEmailInvalid, testGuestPhone);
  //     when(guestService.update(testBookingGuest.getBookingId(), testBookingGuest.getEmail(), testBookingGuest.getPhone())).thenThrow(new IllegalArgumentException());

  //     mvc.perform(put(SERVICE_PATH_USERS + "/guest/" + testBookingGuest.getBookingId() + "/" + testBookingGuest.getEmail() + "," + testBookingGuest.getPhone())
  //     .header("Accept", "application/json"))
  //     .andExpect(status().is(400))
  //     .andReturn();
  //   } catch(Exception e) {
  //     System.out.println("===========================");
  //     System.out.println(e.getMessage());
  //     fail();
  //   }
  // }

  // @Test
  // void test_updateGuest_withBadPhoneParams_thenStatus400() {    
  //   try {
  //     String testGuestEmail = "soso@gmail.com";
  //     String testGuestPhoneInvalid = "notaphone";
  //     BookingGuest testBookingGuest = new BookingGuest(testBooking.getId(), testGuestEmail, testGuestPhoneInvalid);  
  //     when(guestService.update(testBookingGuest.getBookingId(), testBookingGuest.getEmail(), testBookingGuest.getPhone())).thenThrow(new IllegalArgumentException());

  //     mvc.perform(put(SERVICE_PATH_USERS + "/guest/" + testBookingGuest.getBookingId() + "/" + testBookingGuest.getEmail() + "," + testBookingGuest.getPhone())
  //     .header("Accept", "application/json"))
  //     .andExpect(status().is(400))
  //     .andReturn();
  //   } catch(Exception e) {
  //     fail();
  //   }
  // }

  @Test
  void test_delete_withValidBooking_thenStatus204() {    
    try {
      mvc.perform(delete(SERVICE_PATH_USERS + "/" + testBooking.getId())
      .header("Accept", "application/json"))
      .andExpect(status().is(204))
      .andReturn();
    } catch(Exception e) {
      fail();
    }
  }
}
