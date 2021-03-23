package com.ss.utopia;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ss.utopia.models.Booking;
import com.ss.utopia.models.BookingWithReferenceData;

public class MOCKBookingRepository {

  private static final Booking testBooking = new Booking(1, "ACTIVE", "AFHAJKFHKAJS");
  private static final BookingWithReferenceData testBookingWithReferenceData = new BookingWithReferenceData(
    1, "ACTIVE", "AFHAJKFHKAJS", 28, 143, 76, "xyz@gmail.com", "7895437665"
  );

  private static final Booking[] testBookingArray = {
    testBooking,
    new Booking(2, "ACTIVE", "bhdjkHKKKAJS"),
    new Booking(3, "ONHOLD", "UOIUHKJAHSAS"),
    new Booking(4, "INACTIVE", "WYTWHJKASFHJ"),
    new Booking(5, "ACTIVE", "PIPOIMBJJSSJ"),
    new Booking(6, "INACTIVE", "RTYCGZNCBCCC"),
    new Booking(7, "ACTIVE", "MOKJOIASJKHD"),
    new Booking(8, "INACTIVE", "UIASHASJKKZC"),
    new Booking(9, "ACTIVE", "QQWASNDAJSDK"),
  };

  private static final BookingWithReferenceData[] testBookingWithReferenceDataArray = {
    testBookingWithReferenceData,
    new BookingWithReferenceData(2, "ACTIVE", "bhdjkHKKKAJS", 28, 143, 76, "xyz@gmail.com", "7895437665"),
    new BookingWithReferenceData(3, "ONHOLD", "UOIUHKJAHSAS", 29, 144, 77, "axyz@gmail.com", "1568945621"),
    new BookingWithReferenceData(4, "INACTIVE", "WYTWHJKASFHJ", 30, 145, 78, "abxyz@gmail.com", "6987641235"),
    new BookingWithReferenceData(5, "ACTIVE", "PIPOIMBJJSSJ", 31, 146, 79, "abcxyz@gmail.com", "7853216584"),
    new BookingWithReferenceData(6, "INACTIVE", "RTYCGZNCBCCC", 32, 147, 80, "abcdxyz@gmail.com", "9874215684"),
    new BookingWithReferenceData(7, "ACTIVE", "MOKJOIASJKHD", 33, 148, 90, "123xyz@gmail.com", "9871542365"),
    new BookingWithReferenceData(8, "INACTIVE", "UIASHASJKKZC", 34, 149, 91, "Johnxyz@gmail.com", "1854765623"),
    new BookingWithReferenceData(9, "ACTIVE", "QQWASNDAJSDK", 35, 150, 92, "Billyxyz@gmail.com", "9876592487"),
  };


  public static Booking getTestBooking() {
    return testBooking;
  }

  public static BookingWithReferenceData getTestBookingWithReferenceData() {
    return testBookingWithReferenceData;
  }

  public static List<Booking> getTestBookingList() {
    return Arrays.asList(testBookingArray);
  }

  public static List<BookingWithReferenceData> getTestBookingWithReferenceDataList() {
    return Arrays.asList(testBookingWithReferenceDataArray);
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

  public static Optional<BookingWithReferenceData> findById(Integer id) {
    List<BookingWithReferenceData> bookingByIdList = getTestBookingWithReferenceDataList().stream()
      .filter(i -> i.getBookingId().equals(id))
      .collect(Collectors.toList());
    return Optional.of(bookingByIdList.get(0));
  }

  public static Booking save(Booking booking) {
    return booking;
  }

  public static void deleteById() {}
}