package sit.it.rvcomfort.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.request.room.MultipleRoomTypeRequest;
import sit.it.rvcomfort.model.request.room.RoomTypeRequest;
import sit.it.rvcomfort.model.request.room.UpdateRoomTypeRequest;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse.RoomTypeImageResponse;
import sit.it.rvcomfort.model.response.SaveRoomTypeResponse;
import sit.it.rvcomfort.util.TimeUtils;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RoomTypeMapper {

    RoomTypeMapper INSTANCE = Mappers.getMapper(RoomTypeMapper.class);

    RoomTypeRequest derivedFrom(MultipleRoomTypeRequest request);

    RoomTypeResponse from(RoomType roomType);

    SaveRoomTypeResponse addFrom(RoomTypeResponse roomType);

    RoomType from(MultipleRoomTypeRequest request);

    RoomType from(RoomTypeRequest request);

    void update(@MappingTarget RoomType roomType, UpdateRoomTypeRequest request);

    @AfterMapping
    default void after(@MappingTarget RoomTypeResponse.RoomTypeResponseBuilder target, RoomType roomType) {

        List<RoomTypeImageResponse> imageResponses = roomType.getImages().stream()
                .map(roomTypeImage -> RoomTypeImageResponse.builder()
                        .id(roomTypeImage.getId())
                        .image(roomTypeImage.getImage())
                        .build())
                .collect(Collectors.toList());
        target.images(imageResponses);

    }

    @AfterMapping
    default void after(@MappingTarget RoomType.RoomTypeBuilder target, RoomTypeRequest request) {
        target.typeId(0);
        target.createdAt(TimeUtils.now());
    }

    @AfterMapping
    default void after(@MappingTarget RoomType.RoomTypeBuilder target, MultipleRoomTypeRequest request) {
        target.typeId(0);
        target.createdAt(TimeUtils.now());
    }

    @AfterMapping
    default void after(@MappingTarget RoomType target, UpdateRoomTypeRequest request) {
        target.setUpdatedAt(TimeUtils.now());
    }

}
