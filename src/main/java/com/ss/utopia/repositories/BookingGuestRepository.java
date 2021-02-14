package com.ss.utopia.repositories;
import java.util.Optional;

import com.ss.utopia.models.BookingGuest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingGuestRepository extends JpaRepository<BookingGuest, Integer> {

  @Query(value = "SELECT * FROM booking_guest WHERE contact_email = ?1", nativeQuery = true)
  Optional<BookingGuest> findByEmail(String email);

  @Query(value = "SELECT * FROM booking_guest WHERE contact_phone = ?1", nativeQuery = true)
  Optional<BookingGuest> findByPhone(String phone);

}