package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.entity.Room;

@Repository
public interface RoomJpaRepository extends JpaRepository<Room, Integer> {

}
