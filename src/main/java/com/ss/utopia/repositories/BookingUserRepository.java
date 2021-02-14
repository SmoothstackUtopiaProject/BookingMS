package com.ss.utopia.repositories;
import java.util.Optional;

import com.ss.utopia.models.BookingUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingUserRepository extends JpaRepository<BookingUser, Integer> {

  @Query(value = "SELECT * FROM booking_user WHERE user_id = ?1", nativeQuery = true)
  Optional<BookingUser> findByUserId(Integer userId);
  
}