// package com.ss.utopia;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
// class UtopiaBookingMSTests {

// 	@Test
// 	void contextLoads() {
// 	}

// }

// package com.ss.utopia;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.fail;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.List;
// import java.util.stream.Collectors;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.ss.utopia.exceptions.BookingAlreadyExistsException;
// import com.ss.utopia.exceptions.BookingGuestNotFoundException;
// import com.ss.utopia.exceptions.BookingNotFoundException;
// import com.ss.utopia.models.Booking;
// import com.ss.utopia.models.BookingGuest;
// import com.ss.utopia.models.BookingWithReferenceData;
// import com.ss.utopia.services.BookingGuestService;
// import com.ss.utopia.services.BookingService;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Profile;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// @Profile("test")
// @SpringBootTest
// public class BookingControllerTest {

//   final String SERVICE_PATH_USERS = "/bookings";

//   @Mock
//   BookingService service;

//   @Mock
//   BookingGuestService guestService;

//   @InjectMocks
//   BookingController controller;

//   MockMvc mvc;
//   Booking testBooking;
//   List<Booking> testBookingList;

// 	@BeforeEach
// 	void setup() throws Exception {
//     mvc = MockMvcBuilders.standaloneSetup(controller).build();
//     Mockito.reset(service);
//     Mockito.reset(guestService);

//     testBooking = new Booking(12, "ACTIVE", "AFHJA-89066879JHKFA");

//     testBookingList = new ArrayList<Booking>();
//     testBookingList.add(new Booking(1, "INACTIVE", "AHKJAFGKJLAH123"));
//     testBookingList.add(new Booking(2, "INACTIVE", "AHKJAFGKJLAH124"));
//     testBookingList.add(new Booking(3, "INACTIVE", "AHKJAFGKJLAH125"));
//     testBookingList.add(new Booking(4, "INACTIVE", "AHKJAFGKJLAH126"));
//     testBookingList.add(new Booking(5, "ACTIVE", "AHKJAFGKJLAH127"));
//     testBookingList.add(new Booking(6, "ACTIVE", "AHKJAFGKJLAH128"));
//     testBookingList.add(new Booking(7, "ACTIVE", "AHKJAFGKJLAH129"));
//     testBookingList.add(new Booking(8, "ACTIVE", "AHKJAFGKJLAH130"));
//     testBookingList.add(new Booking(9, "ACTIVE", "AHKJAFGKJLAH131"));
//     testBookingList.add(new Booking(10, "ACTIVE", "AHKJAFGKJLAH132"));
//   }

//   @Test
//   void test_validBookingModel_getBookingId() {
//     assertEquals(12, testBooking.getBookingId());
//   }

//   @Test
//   void test_validBookingModel_getBookingStatus() {
//     assertEquals("ACTIVE", testBooking.getBookingStatus());
//   }

//   @Test
//   void test_validBookingModel_getBookingConfirmationCode() {
//     assertEquals("AFHJA-89066879JHKFA", testBooking.getBookingConfirmationCode());
//   }

//   @Test
//   void test_findAllBookings_withValidBookings_thenStatus200() {    
//     try {
//       when(service.findAll()).thenReturn(testBookingList);

//       MvcResult response = mvc.perform(get(SERVICE_PATH_USERS)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(200))
//       .andReturn();
  
//       List<Booking> actual = Arrays.stream(
//         new ObjectMapper().readValue(response.getResponse().getContentAsString(),
//         Booking[].class)).collect(Collectors.toList());
  
//       assertEquals(testBookingList.size(), actual.size());
//       for(int i = 0; i < testBookingList.size(); i++) {
//         assertEquals(testBookingList.get(i).getBookingId(), actual.get(i).getBookingId());
//         assertEquals(testBookingList.get(i).getBookingStatus(), actual.get(i).getBookingStatus());
//         assertEquals(testBookingList.get(i).getBookingConfirmationCode(), actual.get(i).getBookingConfirmationCode());
//       }
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findAllBookings_withNoValidBookings_thenStatus204() {    
//     try {
//       when(service.findAll()).thenReturn(Collections.emptyList());

//       MvcResult response = mvc.perform(get(SERVICE_PATH_USERS)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(204))
//       .andReturn();
  
//       assertEquals("", response.getResponse().getContentAsString());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findById_withValidBooking_thenStatus200() {    
//     try {
//       when(service.findById(testBooking.getBookingId())).thenReturn(testBooking);

//       MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/" + testBooking.getBookingId())
//       .header("Accept", "application/json"))
//       .andExpect(status().is(200))
//       .andReturn();
  
//       Booking actual = new ObjectMapper().readValue(response
//       .getResponse().getContentAsString(), Booking.class);
  
//       assertEquals(testBooking.getBookingId(), actual.getBookingId());
//       assertEquals(testBooking.getBookingStatus(), actual.getBookingStatus());
//       assertEquals(testBooking.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findById_withInvalidBooking_thenStatus404() {    
//     try {
//       Integer invalidId = -1;
//       when(service.findById(invalidId)).thenThrow(new BookingNotFoundException());

//       mvc.perform(get(SERVICE_PATH_USERS + "/" + invalidId)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(404))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findById_withBadParams_thenStatus400() {    
//     try {
//       Integer invalidId = null;
//       when(service.findById(invalidId)).thenThrow(new IllegalArgumentException());

//       mvc.perform(get(SERVICE_PATH_USERS + "/" + invalidId)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(400))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findByConfirmationCode_withValidConfirmationCode_thenStatus200() {    
//     try {
//       String confirmation = testBooking.getBookingConfirmationCode();
//       when(service.findByConfirmationCode(confirmation)).thenReturn(testBooking);

//       MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/confirmation/" + confirmation)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(200))
//       .andReturn();
  
//       Booking actual = new ObjectMapper().readValue(response
//       .getResponse().getContentAsString(), Booking.class);
  
//       assertEquals(testBooking.getBookingId(), actual.getBookingId());
//       assertEquals(testBooking.getBookingStatus(), actual.getBookingStatus());
//       assertEquals(testBooking.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findByConfirmationCode_withInvalidConfirmationCode_thenStatus404() {    
//     try {
//       String invalidConfirmationCode = "notaconfirmationcode";
//       when(service.findByConfirmationCode(invalidConfirmationCode)).thenThrow(new BookingNotFoundException());

//       mvc.perform(get(SERVICE_PATH_USERS + "/confirmation/" + invalidConfirmationCode)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(404))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findByStatus_withValidBooking_singleResult_thenStatus200() {    
//     try {
//       Integer testStatusSearch = 3; // Should only return one Booking
//       when(service.findByStatus(testStatusSearch)).thenReturn(testBookingList.stream()
//       .filter(i -> i.getBookingStatus().equals(3))
//       .collect(Collectors.toList()));

//       MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/search?status=" + testStatusSearch)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(200))
//       .andReturn();

//       List<Booking> actual = Arrays.stream(
//         new ObjectMapper().readValue(response.getResponse().getContentAsString(),
//         Booking[].class)).collect(Collectors.toList());

//       assertEquals(1, actual.size());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findByStatus_withValidBookings_multiResult_thenStatus200() {    
//     try {
//       Integer testStatusSearch = 1; // Should return exactly 5 Bookings
//       when(service.findByStatus(testStatusSearch)).thenReturn(testBookingList.stream()
//       .filter(i -> i.getBookingStatus().equals(1))
//       .collect(Collectors.toList()));

//       MvcResult response = mvc.perform(get(SERVICE_PATH_USERS + "/search?status=" + testStatusSearch)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(200))
//       .andReturn();

//       List<Booking> actual = Arrays.stream(
//         new ObjectMapper().readValue(response.getResponse().getContentAsString(),
//         Booking[].class)).collect(Collectors.toList());

//       assertEquals(5, actual.size());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findByStatus_withNoValidBookings_thenStatus204() {    
//     try {
//       Integer testStatusSearch = -1; // Should return NO Bookings
//       when(service.findByStatus(testStatusSearch)).thenReturn(testBookingList.stream()
//       .filter(i -> i.getBookingStatus().equals(-1))
//       .collect(Collectors.toList()));

//       mvc.perform(get(SERVICE_PATH_USERS + "/search?status=" + testStatusSearch)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(204))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_findByStatus_withBadParams_thenStatus400() {    
//     try {
//       String testStatusSearch = "NotAnInt"; // Should trigger badparams as cannot be cast to an Integer
//       mvc.perform(get(SERVICE_PATH_USERS + "/search?roleId=" + testStatusSearch)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(400))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_insertWithUser_withValidUser_thenStatus201() {    
//     try {
//       String testUserId = "1";
//       when(service.insertByBookingUser(1)).thenReturn(testBooking);      

//       MvcResult response = mvc.perform(post(SERVICE_PATH_USERS + "/user/" + testUserId)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(201))
//       .andReturn();

//       Booking actual = new ObjectMapper().readValue(response
//       .getResponse().getContentAsString(), Booking.class);

//       assertEquals(testBooking.getBookingId(), actual.getBookingId());
//       assertEquals(testBooking.getBookingStatus(), actual.getBookingStatus());
//       assertEquals(testBooking.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_insertWithUser_withDuplicateUser_thenStatus409() {    
//     try {
//       String testUserId = "1";
//       when(service.insertByBookingUser(1)).thenThrow(new BookingAlreadyExistsException());  

//       mvc.perform(post(SERVICE_PATH_USERS + "/user/" + testUserId)
//       .header("Accept", "application/json"))
//       .andExpect(status().is(409))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_insertWithGuest_withValidGuest_thenStatus201() {    
//     try {
//       String testGuestEmail = "soso@gmail.com";
//       String testGuestPhone = "5987643213";
//       when(service.insertByBookingGuest(testGuestEmail, testGuestPhone)).thenReturn(testBooking);      

//       MvcResult response = mvc.perform(post(SERVICE_PATH_USERS + "/guest")
//       .header("Accept", "application/json")
//       .content(new ObjectMapper().writeValueAsString(new BookingGuest(1, testGuestEmail, testGuestPhone))))
//       .andExpect(status().is(201))
//       .andReturn();

//       Booking actual = new ObjectMapper().readValue(response
//       .getResponse().getContentAsString(), Booking.class);

//       assertEquals(testBooking.getBookingId(), actual.getBookingId());
//       assertEquals(testBooking.getBookingStatus(), actual.getBookingStatus());
//       assertEquals(testBooking.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_insertWithUser_withDuplicateGuest_thenStatus409() {    
//     try {
//       String testGuestEmail = "soso@gmail.com";
//       String testGuestPhone = "5987643213";
//       when(service.insertByBookingGuest(testGuestEmail, testGuestPhone)).thenThrow(new BookingAlreadyExistsException());      

//       mvc.perform(post(SERVICE_PATH_USERS + "/guest")
//       .header("Accept", "application/json")
//       .content(new ObjectMapper().writeValueAsString(new BookingGuest(1, testGuestEmail, testGuestPhone))))
//       .andExpect(status().is(409))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   // @Test
//   // void test_insertGuest_withBadEmailParams_thenStatus400() {    
//   //   try {
//   //     String testGuestEmail = "notanemail";
//   //     String testGuestPhone = "5987643213";  
//   //     when(service.insertByBookingGuest(testGuestEmail, testGuestPhone)).thenThrow(new IllegalArgumentException());   

//   //     mvc.perform(post(SERVICE_PATH_USERS + "/guest")
//   //     .header("Accept", "application/json")
//   //     .content(new ObjectMapper().writeValueAsString(new BookingGuest(1, testGuestEmail, testGuestPhone))))
//   //     .andExpect(status().is(400))
//   //     .andReturn();
//   //   } catch(Exception e) {
//   //     fail();
//   //   }
//   // }

//   // @Test
//   // void test_insertGuest_withBadPhoneParams_thenStatus400() {    
//   //   try {
//   //     String testGuestEmail = "soso@gmail.com";
//   //     String testGuestPhone = "notaphone";
//   //     when(service.insertByBookingGuest(testGuestEmail, testGuestPhone)).thenThrow(new IllegalArgumentException()); 

//   //     mvc.perform(post(SERVICE_PATH_USERS + "/guest")
//   //     .header("Accept", "application/json")
//   //     .content(new ObjectMapper().writeValueAsString(new BookingGuest(1, testGuestEmail, testGuestPhone))))
//   //     .andExpect(status().is(400))
//   //     .andReturn();
//   //   } catch(Exception e) {
//   //     System.out.println("==================================");
//   //     System.out.println(e.getMessage());
//   //     fail();
//   //   }
//   // }

//   @Test
//   void test_update_withValidBooking_thenStatus202() {    
//     try {
//       BookingWithReferenceData newBooking = new BookingWithReferenceData(
//         testBooking.getBookingId(), 
//         "INACTIVE", 
//         testBooking.getBookingConfirmationCode(),
//         1, 1, 1, "", ""
//       );

//       HashMap<String, String> bookingMap = new HashMap<String, String>();
//       bookingMap.put("bookingId", newBooking.getBookingId().toString());
//       bookingMap.put("bookingStatus", newBooking.getBookingStatus());
//       bookingMap.put("bookingConfirmationCode", newBooking.getBookingConfirmationCode());
//       bookingMap.put("bookingFlightId", newBooking.getBookingFlightId().toString());
//       bookingMap.put("bookingPassengerId", newBooking.getBookingPassengerId().toString());
//       bookingMap.put("bookingUserId", newBooking.getBookingUserId().toString());
//       bookingMap.put("bookingGuestEmail", newBooking.getBookingGuestEmail());
//       bookingMap.put("bookingGuestPhone", newBooking.getBookingGuestPhone());

//       when(service.update(bookingMap)).thenReturn(newBooking);

//       MvcResult response = mvc.perform(put(SERVICE_PATH_USERS + "/" + newBooking.getBookingId() + "," + newBooking.getBookingStatus())
//       .header("Accept", "application/json"))
//       .andExpect(status().is(202))
//       .andReturn();

//       Booking actual = new ObjectMapper().readValue(response
//       .getResponse().getContentAsString(), Booking.class);

//       assertEquals(newBooking.getBookingId(), actual.getBookingId());
//       assertEquals(newBooking.getBookingStatus(), actual.getBookingStatus());
//       assertEquals(newBooking.getBookingConfirmationCode(), actual.getBookingConfirmationCode());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_update_withInvalidBooking_thenStatus404() {    
//     try {
//       BookingWithReferenceData newBooking = new BookingWithReferenceData(
//         testBooking.getBookingId(), 
//         "INACTIVE", 
//         testBooking.getBookingConfirmationCode(),
//         1, 1, 1, "", ""
//       );

//       HashMap<String, String> bookingMap = new HashMap<String, String>();
//       bookingMap.put("bookingId", newBooking.getBookingId().toString());
//       bookingMap.put("bookingStatus", newBooking.getBookingStatus());
//       bookingMap.put("bookingConfirmationCode", newBooking.getBookingConfirmationCode());
//       bookingMap.put("bookingFlightId", newBooking.getBookingFlightId().toString());
//       bookingMap.put("bookingPassengerId", newBooking.getBookingPassengerId().toString());
//       bookingMap.put("bookingUserId", newBooking.getBookingUserId().toString());
//       bookingMap.put("bookingGuestEmail", newBooking.getBookingGuestEmail());
//       bookingMap.put("bookingGuestPhone", newBooking.getBookingGuestPhone());


//       when(service.update(bookingMap)).thenThrow(new BookingNotFoundException());

//       mvc.perform(put(SERVICE_PATH_USERS + "/" + newBooking.getBookingId() + "," + newBooking.getBookingStatus())
//       .header("Accept", "application/json"))
//       .andExpect(status().is(404))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_updateGuest_withValidGuest_thenStatus202() {    
//     try {
//       String testGuestEmail = "soso@gmail.com";
//       String testGuestPhone = "5987643213";
//       BookingGuest testBookingGuest = new BookingGuest(testBooking.getBookingId(), testGuestEmail, testGuestPhone);
//       when(guestService.update(testBookingGuest.getBookingGuestId(), testBookingGuest.getBookingGuestEmail(), testBookingGuest.getBookingGuestPhone()))
//       .thenReturn(testBookingGuest);    

//       MvcResult response = mvc.perform(put(SERVICE_PATH_USERS + "/guest")
//       .header("Accept", "application/json")
//       .content(new ObjectMapper().writeValueAsString(testBookingGuest)))
//       .andExpect(status().is(202))
//       .andReturn();

//       BookingGuest actual = new ObjectMapper().readValue(response
//       .getResponse().getContentAsString(), BookingGuest.class);

//       assertEquals(testBookingGuest.getBookingGuestId(), actual.getBookingGuestId());
//       assertEquals(testBookingGuest.getBookingGuestEmail(), actual.getBookingGuestEmail());
//       assertEquals(testBookingGuest.getBookingGuestPhone(), actual.getBookingGuestPhone());
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   @Test
//   void test_updateGuest_withInvalidGuest_thenStatus404() {    
//     try {
//       String testGuestEmail = "soso@gmail.com";
//       String testGuestPhone = "5987643213";
//       BookingGuest testBookingGuest = new BookingGuest(-1, testGuestEmail, testGuestPhone);
//       when(guestService.update(testBookingGuest.getBookingGuestId(), testBookingGuest.getBookingGuestEmail(), testBookingGuest.getBookingGuestPhone()))
//       .thenThrow(new BookingGuestNotFoundException());    

//       mvc.perform(put(SERVICE_PATH_USERS + "/guest")
//       .header("Accept", "application/json")
//       .content(new ObjectMapper().writeValueAsString(testBookingGuest)))
//       .andExpect(status().is(404))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }

//   // @Test
//   // void test_updateGuest_withBadEmailParams_thenStatus400() {    
//   //   try {
//   //     String testGuestEmailInvalid = "notanemail";
//   //     String testGuestPhone = "5987643213";
//   //     BookingGuest testBookingGuest = new BookingGuest(testBooking.getBookingId(), testGuestEmailInvalid, testGuestPhone);
//   //     when(guestService.update(testBookingGuest.getBookingId(), testBookingGuest.getBookingGuestEmail(), testBookingGuest.getBookingGuestPhone())).thenThrow(new IllegalArgumentException());

//   //     mvc.perform(put(SERVICE_PATH_USERS + "/guest/" + testBookingGuest.getBookingId() + "/" + testBookingGuest.getBookingGuestEmail() + "," + testBookingGuest.getBookingGuestPhone())
//   //     .header("Accept", "application/json"))
//   //     .andExpect(status().is(400))
//   //     .andReturn();
//   //   } catch(Exception e) {
//   //     System.out.println("===========================");
//   //     System.out.println(e.getMessage());
//   //     fail();
//   //   }
//   // }

//   // @Test
//   // void test_updateGuest_withBadPhoneParams_thenStatus400() {    
//   //   try {
//   //     String testGuestEmail = "soso@gmail.com";
//   //     String testGuestPhoneInvalid = "notaphone";
//   //     BookingGuest testBookingGuest = new BookingGuest(testBooking.getBookingId(), testGuestEmail, testGuestPhoneInvalid);  
//   //     when(guestService.update(testBookingGuest.getBookingId(), testBookingGuest.getBookingGuestEmail(), testBookingGuest.getBookingGuestPhone())).thenThrow(new IllegalArgumentException());

//   //     mvc.perform(put(SERVICE_PATH_USERS + "/guest/" + testBookingGuest.getBookingId() + "/" + testBookingGuest.getBookingGuestEmail() + "," + testBookingGuest.getBookingGuestPhone())
//   //     .header("Accept", "application/json"))
//   //     .andExpect(status().is(400))
//   //     .andReturn();
//   //   } catch(Exception e) {
//   //     fail();
//   //   }
//   // }

//   @Test
//   void test_delete_withValidBooking_thenStatus204() {    
//     try {
//       mvc.perform(delete(SERVICE_PATH_USERS + "/" + testBooking.getBookingId())
//       .header("Accept", "application/json"))
//       .andExpect(status().is(204))
//       .andReturn();
//     } catch(Exception e) {
//       fail();
//     }
//   }
// }
