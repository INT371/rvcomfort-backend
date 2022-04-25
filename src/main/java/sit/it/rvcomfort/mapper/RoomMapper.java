package sit.it.rvcomfort.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.request.room.RoomRequest;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.util.TimeUtils;

@Mapper
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomResponse from(Room room);

    Room from(RoomRequest request, RoomType roomType);

    @Mapping(target = "roomId", ignore = true)
    @Mapping(target = "roomName", source = "request.roomName")
    @Mapping(target = "roomType", source = "roomType")
    void update(@MappingTarget Room room, RoomRequest request, RoomType roomType);

    @AfterMapping
    default void after(@MappingTarget Room.RoomBuilder target, RoomRequest request, RoomType roomType) {
        target.roomId(0);
        target.createdAt(TimeUtils.now());
    }

    @AfterMapping
    default void after(@MappingTarget Room target, RoomRequest request, RoomType roomType) {
        target.setUpdatedAt(TimeUtils.now());
    }

}
