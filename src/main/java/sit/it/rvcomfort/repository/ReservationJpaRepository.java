package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.dto.RoomTypeCountQuery;
import sit.it.rvcomfort.model.entity.Reservation;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByRoomRoomId(Integer roomId);

    List<Reservation> findReservationByRoomRoomIdAndCheckOutDateAfterAndCheckInDateBefore(Integer roomId, ZonedDateTime checkInDate, ZonedDateTime checkOutDate);

    @Query("SELECT rt.typeId AS typeId, COUNT(DISTINCT r.room.roomId) AS roomCount " +
            "FROM RoomType rt LEFT JOIN Room room ON rt.typeId = room.roomType.typeId " +
            "JOIN Reservation r ON room.roomId = r.room.roomId " +
            "WHERE r.checkOutDate > ?1 AND r.checkInDate < ?2 " +
            "GROUP BY rt.typeId")
    List<RoomTypeCountQuery> countReservedRoom(ZonedDateTime checkInDate, ZonedDateTime checkOutDate);
}
