package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomJpaRepository extends JpaRepository<Room, Integer> {

    Optional<Room> findByRoomName(String roomName);

    Optional<Room> findRoomByRoomIdNotAndRoomName(Integer roomId, String roomName);

    List<Room> findRoomByRoomType(RoomType roomType);

    @Query("SELECT DISTINCT room " +
            "FROM Room room " +
            "LEFT JOIN Reservation r ON room.roomId = r.room.roomId " +
            "WHERE room.roomType.typeId = ?1 AND (r.checkOutDate > ?2 AND r.checkInDate < ?3) ")
    List<Room> findNotAvailableRoomsFromRoomTypeAndReservationDateTime(Integer typeId, ZonedDateTime checkInDate, ZonedDateTime checkOutDate);

}
