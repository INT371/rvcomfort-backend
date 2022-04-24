package sit.it.rvcomfort.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.response.RoomTypeResponse;

@Mapper
public interface RoomTypeMapper {

    RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);

    RoomTypeResponse from(RoomType roomType);

//    @AfterMapping
//    default void after(@MappingTarget User.UserBuilder target, UserRegistrationRequest user, String password) {
//
//    }

}
