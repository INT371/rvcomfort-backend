package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.dto.RoomTypeCountQuery;
import sit.it.rvcomfort.model.entity.RoomType;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeJpaRepository extends JpaRepository<RoomType, Integer> {

    Optional<RoomType> findByTypeName (String typeName);

    Optional<RoomType> findRoomTypeByTypeIdNotAndTypeName (Integer typeId, String typeName);

    @Query("SELECT rt.typeId AS typeId, COUNT(rt) AS roomCount " +
            "FROM RoomType rt LEFT JOIN Room r ON r.roomType.typeId = rt.typeId " +
            "GROUP BY rt.typeId " +
            "ORDER BY rt.typeId ASC")
    List<RoomTypeCountQuery> countTotalRoomByRoomTypeInterface();

}
