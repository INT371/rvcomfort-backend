package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.entity.RoomTypeImage;

import java.util.List;

@Repository
public interface RoomTypeImageJpaRepository extends JpaRepository<RoomTypeImage, Integer> {

    List<RoomTypeImage> findRoomTypeImagesByTypeTypeIdOrderByIdAsc(Integer typeId);

    void deleteByTypeTypeId(Integer typeId);

}
