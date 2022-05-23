package sit.it.rvcomfort.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.it.rvcomfort.exception.list.BusinessException;
import sit.it.rvcomfort.exception.list.InvalidAttributeException;
import sit.it.rvcomfort.exception.list.NotFoundException;
import sit.it.rvcomfort.mapper.ReservationMapper;
import sit.it.rvcomfort.model.entity.Reservation;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.entity.User;
import sit.it.rvcomfort.model.request.reservation.ReservationRequest;
import sit.it.rvcomfort.model.response.ReservationResponse;
import sit.it.rvcomfort.repository.ReservationJpaRepository;
import sit.it.rvcomfort.repository.RoomJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeJpaRepository;
import sit.it.rvcomfort.repository.UserJpaRepository;
import sit.it.rvcomfort.service.ReservationService;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static sit.it.rvcomfort.exception.response.ExceptionResponse.ERROR_CODE.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationJpaRepository reservationJpaRepository;
    private final RoomJpaRepository roomJpaRepository;
    private final RoomTypeJpaRepository roomTypeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    private final ObjectMapper objectMapper;


    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationJpaRepository.findAll().stream()
                .map(ReservationMapper.INSTANCE::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> getPagingReservations(int pageNo, int size, String sortBy) {
        return reservationJpaRepository.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy))).stream()
                .map(ReservationMapper.INSTANCE::from)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse getReservationById(Integer id) {
        return reservationJpaRepository.findById(id)
                .map(ReservationMapper.INSTANCE::from)
                .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND,
                        MessageFormat.format("The reservation id: {0} does not exist in the database.", id)));
    }

    @Override
    public List<ReservationResponse> getReservationByRoomId(Integer roomId) {
        return reservationJpaRepository.findByRoomRoomId(roomId).stream()
                .map(ReservationMapper.INSTANCE::from)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public ReservationResponse reserveRoom(ReservationRequest request) {
        // STEP 1: Validation
        // STEP 1.1: Validate if time valid or overlapped
        validateTimeEligibility(request);

        // STEP 1.2: Validated if room type valid
        RoomType roomType = roomTypeJpaRepository.findById(request.getTypeId())
                .orElseThrow(() -> new NotFoundException(ROOM_TYPE_NOT_FOUND,
                        MessageFormat.format("The room type with id: {0} does not exist in the database.", request.getTypeId())));
        log.info("[ReserveRoom] STEP 1.2: RoomTypeList = {}", objectMapper.writeValueAsString(roomType));

        User user = userJpaRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND,
                        MessageFormat.format("not have user with user id {0} in database.", request.getUserId())));
        log.info("[ReserveRoom] STEP 1.2: User = {}", objectMapper.writeValueAsString(user));

        // STEP 1.3: Get first available room of requested room type
        List<Room> roomList = roomJpaRepository.findRoomByRoomType(roomType);
        log.info("[ReserveRoom] STEP 1.3.1: roomList = {}", objectMapper.writeValueAsString(roomList));

        List<Room> notAvailableRoom = roomJpaRepository
                .findNotAvailableRoomsFromRoomTypeAndReservationDateTime(request.getTypeId(), request.getCheckInDate(), request.getCheckOutDate());
        log.info("[ReserveRoom] STEP 1.3.2: notAvailableRoom = {}", objectMapper.writeValueAsString(notAvailableRoom));

        notAvailableRoom.stream().forEach(room -> roomList.remove(room));
        log.info("[ReserveRoom] STEP 1.3.3: roomList = {}", objectMapper.writeValueAsString(roomList));

        Room room = roomList.stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_AVAILABLE,
                        MessageFormat.format("Not have room available for room type {0} currently.", request.getTypeId())));
        log.info("[ReserveRoom] STEP 1.3.4: room = {}", objectMapper.writeValueAsString(room));

        validateOverlappedReservationTime(request, room);

        // STEP 2: Mapped to entity
        Reservation reservation = ReservationMapper.INSTANCE.from(request, room, user);

        // STEP 3: Save to database
        Reservation savedReservation = reservationJpaRepository.save(reservation);

        // STEP 4: Mapped to response then return
        return ReservationMapper.INSTANCE.from(savedReservation);
    }

    private void validateOverlappedReservationTime(ReservationRequest request, Room room) {
        boolean isOverlapped = !reservationJpaRepository
                .findReservationByRoomRoomIdAndCheckOutDateAfterAndCheckInDateBefore(room.getRoomId(), request.getCheckInDate(), request.getCheckOutDate())
                .isEmpty();
        if (isOverlapped) throw new BusinessException(OVERLAPPED_RESERVATION_TIME,
                "The reservation time is overlapped with other reservation");
    }

    private void validateTimeEligibility(ReservationRequest request) {
        // check if check-in-date is before check-out-date
        if (request.getCheckInDate().isAfter(request.getCheckOutDate())) {
            throw new InvalidAttributeException(RESERVATION_INVALID_ATTRIBUTE, "Check in date must not after check out date");
        }

        // Check if check-in-date or check-out-date before minimum interval before reserveation date
        int minDayInterval = 1; //TODO: Select minimum day interval before reservation, Change this to constant
        LocalDate minimumDateFromNow = ZonedDateTime.now().plusDays(minDayInterval).toLocalDate();
        if (minimumDateFromNow.isAfter(request.getCheckInDate().toLocalDate())
                || minimumDateFromNow.isAfter(request.getCheckOutDate().toLocalDate())) {
            throw new InvalidAttributeException(RESERVATION_DATE_TOO_CLOSE,
                    MessageFormat.format("Can reserve a room after {0} days from today which is {1}.", minDayInterval, minimumDateFromNow.format(DateTimeFormatter.ISO_LOCAL_DATE)));
        }
    }

    @Override
    public ReservationResponse cancelRoomReservation(Integer id) {
        // STEP 1: Validate if reservation is exists & not end
        Reservation reservation = reservationJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND,
                        MessageFormat.format("The reservation id: {0} does not exist in the database.", id)));

        // STEP 2: If remaining time before check in is
        // STEP 2.1: > 14 days

        // STEP 2.2: > 7 days && < 14 days

        // STEP 2.3: < 7 days

        // STEP 3: Change status to CANCELLED and save (or remove from db)
        reservation.setStatus("CANCELLED");
        Reservation savedReservation = reservationJpaRepository.save(reservation);

        // STEP 4: Mapped to response and return
        return ReservationMapper.INSTANCE.from(savedReservation);
    }
}
