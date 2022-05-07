package sit.it.rvcomfort.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.entity.RoomTypeImage;
import sit.it.rvcomfort.util.TimeUtils;

@Mapper
public interface RoomTypeImageMapper {

    RoomTypeImageMapper INSTANCE = Mappers.getMapper(RoomTypeImageMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", source = "type")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RoomTypeImage from(RoomType type, String image);

    @AfterMapping
    default void after(@MappingTarget RoomTypeImage.RoomTypeImageBuilder target, RoomType type, String image) {
        target.id(0);
        target.createdAt(TimeUtils.now());
    }

}
