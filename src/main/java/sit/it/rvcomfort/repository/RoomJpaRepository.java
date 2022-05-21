package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomJpaRepository extends JpaRepository<Room, Integer> {

    Optional<Room> findByRoomName(String roomName);

    Optional<Room> findRoomByRoomIdNotAndRoomName(Integer roomId, String roomName);

    List<Room> findRoomByRoomType(RoomType roomType);


}
