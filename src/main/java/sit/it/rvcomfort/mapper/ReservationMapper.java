package sit.it.rvcomfort.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import sit.it.rvcomfort.model.entity.Reservation;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.User;
import sit.it.rvcomfort.model.request.reservation.ReservationRequest;
import sit.it.rvcomfort.model.response.ReservationResponse;
import sit.it.rvcomfort.util.TimeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

@Mapper
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "room", ignore = true)
    ReservationResponse from(Reservation reservation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "room", source = "room")
    @Mapping(target = "checkInDate", source = "request.checkInDate")
    @Mapping(target = "checkOutDate", source = "request.checkOutDate")
    @Mapping(target = "reservedName", source = "request.reservedName")
    @Mapping(target = "numOfGuest", source = "request.numOfGuest")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reservation from(ReservationRequest request, Room room, User user);

    @AfterMapping
    default void after(@MappingTarget Reservation.ReservationBuilder target, ReservationRequest request, Room room, User user) {
        target.id(0);
        target.status("WAIT_FOR_PAYMENT"); //TODO: Create status constant
        target.createdAt(TimeUtils.now());
        target.totalPrice(totalPriceCalculate(request, room));
    }

    @AfterMapping
    default void after(@MappingTarget ReservationResponse.ReservationResponseBuilder target, Reservation reservation){
        target.room(RoomMapper.INSTANCE.from(reservation.getRoom()));
        target.user(UserMapper.INSTANCE.from(reservation.getUser()));
    }

    private BigDecimal totalPriceCalculate(ReservationRequest request, Room room) {
        Long totalDays = Duration.between(request.getCheckInDate(), request.getCheckOutDate()).toDays();
        return room.getRoomType().getPrice().multiply(BigDecimal.valueOf(totalDays)).setScale(2, RoundingMode.FLOOR); // TODO: Change scale to constant
    }

}
