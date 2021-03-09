package com.ss.utopia.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import com.ss.utopia.models.Flight;
import com.ss.utopia.models.FlightBooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Integer> {

  @Query(value = "SELECT * FROM flight WHERE id = ?1", nativeQuery = true)
  Optional<Flight> findByFlightById(Integer flightId);

  @Modifying
  @Transactional
  @Query(value="DELETE from flight_bookings WHERE booking_id = ?1", nativeQuery=true)
  void deleteByBookingId(Integer bookingId);
}