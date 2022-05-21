package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.entity.Reservation;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByRoomRoomId(Integer roomId);

    List<Reservation> findReservationByRoomRoomIdAndCheckOutDateAfterAndCheckInDateBefore(Integer roomId, ZonedDateTime checkInDate, ZonedDateTime checkOutDate);

    @Query("SELECT r.room.roomType.typeId AS typeId, COUNT(DISTINCT r.room.roomId) AS roomCount " +
            "FROM Reservation r " +
            "WHERE r.checkInDate < ?1" +
            "AND r.checkOutDate > ?2" +
            "GROUP BY r.room.roomType.typeId")
    Map<Integer, Long> countReservedRoom(ZonedDateTime checkInDate, ZonedDateTime checkOutDate);
}
