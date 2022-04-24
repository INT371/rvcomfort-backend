package sit.it.rvcomfort.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.request.room.RoomRequest;
import sit.it.rvcomfort.model.response.RoomResponse;

@Mapper
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomResponse from(Room room);

    Room from(RoomRequest request, RoomType roomType);

//    @AfterMapping
//    default void after(@MappingTarget User.UserBuilder target, UserRegistrationRequest user, String password) {
//
//    }

}
