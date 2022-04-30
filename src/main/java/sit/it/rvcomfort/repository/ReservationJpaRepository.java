package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.entity.Reservation;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByRoomRoomId(Integer roomId);

    List<Reservation> findReservationByRoomRoomIdAndCheckOutDateAfterAndCheckInDateBefore(Integer roomId, ZonedDateTime checkInDate, ZonedDateTime checkOutDate);

}
