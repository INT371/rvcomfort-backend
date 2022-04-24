package sit.it.rvcomfort.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.request.room.NewRoomTypeRequest;
import sit.it.rvcomfort.model.request.room.UpdateRoomTypeRequest;
import sit.it.rvcomfort.model.response.RoomTypeResponse;

import java.time.ZonedDateTime;

@Mapper
public interface RoomTypeMapper {

    RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);

    RoomTypeResponse from(RoomType roomType);

    RoomType from(NewRoomTypeRequest request);

    RoomType from(UpdateRoomTypeRequest request);

    @AfterMapping
    default void after(@MappingTarget RoomType.RoomTypeBuilder target, NewRoomTypeRequest request) {
        target.typeId(0);
        target.createdAt(ZonedDateTime.now());
    }

    @AfterMapping
    default void after(@MappingTarget RoomType.RoomTypeBuilder target, UpdateRoomTypeRequest request) {
        target.updatedAt(ZonedDateTime.now());
    }

}
