package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.entity.RoomType;

import java.util.Optional;

@Repository
public interface RoomTypeJpaRepository extends JpaRepository<RoomType, Integer> {

    Optional<RoomType> findByTypeName (String typeName);

}
