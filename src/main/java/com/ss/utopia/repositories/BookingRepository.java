package com.ss.utopia.repositories;

import com.ss.utopia.models.Booking;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

  @Query(value = "SELECT * FROM booking WHERE confirmation_code = ?1", nativeQuery = true)
  Optional<Booking> findByConfirmationCode(String confirmationCode);

  @Query(value = "SELECT * FROM booking WHERE is_active = ?1", nativeQuery = true)
  List<Booking> findByStatus(String status);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM user WHERE id = ?1", nativeQuery = true)
  void deleteById(Integer id);
}
