package sit.it.rvcomfort.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sit.it.rvcomfort.exception.list.DuplicateDataException;
import sit.it.rvcomfort.exception.list.InvalidAttributeException;
import sit.it.rvcomfort.exception.list.NotFoundException;
import sit.it.rvcomfort.mapper.RoomMapper;
import sit.it.rvcomfort.mapper.RoomTypeImageMapper;
import sit.it.rvcomfort.mapper.RoomTypeMapper;
import sit.it.rvcomfort.model.dto.RoomTypeCountQuery;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.entity.RoomTypeImage;
import sit.it.rvcomfort.model.request.room.*;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.model.response.RoomTypeWithRoomResponse;
import sit.it.rvcomfort.model.response.SaveRoomTypeResponse;
import sit.it.rvcomfort.repository.ReservationJpaRepository;
import sit.it.rvcomfort.repository.RoomJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeImageJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeJpaRepository;
import sit.it.rvcomfort.service.RoomService;
import sit.it.rvcomfort.service.impl.file.RoomImageService;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static sit.it.rvcomfort.exception.response.ExceptionResponse.ERROR_CODE.*;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final ObjectMapper objectMapper;

    private final RoomImageService roomImageService;
    private final RoomJpaRepository roomRepo;
    private final RoomTypeJpaRepository roomTypeRepo;
    private final RoomTypeImageJpaRepository roomTypeImageRepo;
    private final ReservationJpaRepository reservationRepo;

    @Override
    public List<RoomTypeResponse> getAllRoomType() {
        return roomTypeRepo.findAll().stream()
                .map(RoomTypeMapper.INSTANCE::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomTypeResponse> getPagingRoomType(int pageNo, int size, String sortBy) {
        return roomTypeRepo.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy))).toList()
                .stream().map(RoomTypeMapper.INSTANCE::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepo.findAll().stream()
                .map(RoomMapper.INSTANCE::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getPagingRooms(int pageNo, int size, String sortBy) {
        return roomRepo.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy))).toList()
                .stream().map(RoomMapper.INSTANCE::from)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse getRoom(Integer roomId) {
        return roomRepo.findById(roomId)
                .map(RoomMapper.INSTANCE::from)
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND,
                        MessageFormat.format("The room with id: {0} does not exist in the database.", roomId)));
    }

    @Override
    public RoomResponse getRoom(String roomName) {
        return roomRepo.findByRoomName(roomName)
                .map(RoomMapper.INSTANCE::from)
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND,
                        MessageFormat.format("The room with name: {0} does not exist in the database.", roomName)));
    }

    @Override
    public RoomTypeResponse getRoomType(Integer roomTypeId) {
        return roomTypeRepo.findById(roomTypeId)
                .map(RoomTypeMapper.INSTANCE::from)
                .orElseThrow(() -> new NotFoundException(ROOM_TYPE_NOT_FOUND,
                        MessageFormat.format("The room type with id: {0} does not exist in the database.", roomTypeId)));
    }

    @Override
    public RoomTypeResponse getRoomType(String roomTypeName) {
        return roomTypeRepo.findByTypeName(roomTypeName)
                .map(RoomTypeMapper.INSTANCE::from)
                .orElseThrow(() -> new NotFoundException(ROOM_TYPE_NOT_FOUND,
                        MessageFormat.format("The room type with name: {0} does not exist in the database.", roomTypeName)));
    }

    @SneakyThrows
    @Override
    public RoomTypeResponse addRoomType(RoomTypeRequest request, MultipartFile[] images) {
        // STEP 1: Validation
        log.info("[addRoomType] STEP 1: Validation");
        validateIfRoomTypeIsDuplicated(request.getTypeName());
        validateIsImageEmpty(images);
        // STEP 2: Mapped to entity
        RoomType roomType = RoomTypeMapper.INSTANCE.from(request);
        log.info("[addRoomType] STEP 2: mapped to entity '{}'", objectMapper.writeValueAsString(roomType));
        // STEP 3: Save image -> mapped to RoomTypeImage entity -> save to database
        List<RoomTypeImage> imageList = storeImages(images, roomType);
        log.info("[addRoomType] STEP 3.1: mapped to RoomTypeImage entity '{}'", objectMapper.writeValueAsString(imageList));
        roomType.setImages(imageList);
        log.info("[addRoomType] STEP 3.2: set image to roomType '{}'", objectMapper.writeValueAsString(roomType));
        // STEP 4: Save to database
        RoomType savedRoom = roomTypeRepo.save(roomType);
        log.info("[addRoomType] STEP 4: Save to database '{}'", objectMapper.writeValueAsString(savedRoom));
        // STEP 5: Mapped to response then return
        return RoomTypeMapper.INSTANCE.from(savedRoom);
    }

    @Override
    public SaveRoomTypeResponse addRoomTypeWithRooms(MultipleRoomTypeRequest request, MultipartFile[] images) {
        // STEP 1.1: Mapped request to normal roomType request
        RoomTypeRequest roomTypeRequest = RoomTypeMapper.INSTANCE.derivedFrom(request);
        // STEP 1.2: Send to add room type method
        RoomTypeResponse addRoomTypeResponse = addRoomType(roomTypeRequest, images);

        // STEP 2: Add new room
        List<RoomRequest> roomList = request.getRooms().stream()
                .peek(room -> room.setTypeId(addRoomTypeResponse.getTypeId()))
                .collect(Collectors.toList());
        List<RoomResponse> roomResponses = this.addMultipleRoomOfExistingType(roomList);

        // STEP 3: Mapped and return
        SaveRoomTypeResponse response = RoomTypeMapper.INSTANCE.addFrom(addRoomTypeResponse);
        response.setRooms(roomResponses);

        return response;
    }

    private void validateIfRoomTypeIsDuplicated(String request) {
        roomTypeRepo.findByTypeName(request).ifPresent(roomType -> {
            throw new DuplicateDataException(DUPLICATE_ROOM_TYPE,
                    MessageFormat.format("The specify type name: {0} is already exist in the database.", roomType.getTypeName()));
        });
    }

    @Override
    public RoomResponse addRoomOfExistingType(RoomRequest request) {
        // STEP 1: Validate
        roomRepo.findByRoomName(request.getRoomName()).ifPresent(room -> {
            throw new DuplicateDataException(DUPLICATE_ROOM_NAME,
                    MessageFormat.format("The specify room name: {0} is already exist in the database.", room.getRoomName()));
        });

        // STEP 1.2: Get RoomType from Id
        RoomType roomType = roomTypeRepo.findById(request.getTypeId())
                .orElseThrow(() -> new NotFoundException(ROOM_TYPE_NOT_FOUND,
                        MessageFormat.format("The room type with id: {0} does not exist in the database.", request.getTypeId())));

        // STEP 2: Mapped request to entity
        Room room = RoomMapper.INSTANCE.from(request, roomType);

        // STEP 3: Save entity
        Room savedRoom = roomRepo.save(room);

        // STEP 4: Mapped entity to response then return
        return RoomMapper.INSTANCE.from(savedRoom);
//        return RoomMapper.INSTANCE.from(roomRepo.save(RoomMapper.INSTANCE.from(request, roomTypeRepo.findById(request.getTypeId()).orElseThrow(() -> new RuntimeException("")))));  // One line version
    }

    @Override
    public List<RoomResponse> addMultipleRoomOfExistingType(List<RoomRequest> requests) {
        return requests.stream()
                .map(this::addRoomOfExistingType)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request, MultipartFile[] images, int typeId) {
        // STEP 1: Validation
        // STEP 1.1:
        log.info("[updateRoomType] STEP 1: Validation Started");
        roomTypeRepo.findRoomTypeByTypeIdNotAndTypeName(typeId, request.getTypeName())
                .ifPresent(roomType -> {
                    throw new DuplicateDataException(DUPLICATE_ROOM_TYPE,
                            MessageFormat.format("The given type name: {0} is already exist in the database", roomType.getTypeName()));
                });

        // STEP 1.2: Validate exist room type
        RoomType roomType = roomTypeRepo.findById(typeId)
                .orElseThrow(() -> new NotFoundException(ROOM_TYPE_NOT_FOUND,
                        MessageFormat.format("The room type with id: {0} does not exist in the database.", typeId)));

        // STEP 1.3: Validate image list must not empty
        validateIsImageEmpty(images);
        log.info("[updateRoomType] STEP 1: Validation End");

        // STEP 2: Mapped request to entity
        RoomTypeMapper.INSTANCE.update(roomType, request);
        log.info("[updateRoomType] STEP 2.1: Mapped request: {}", objectMapper.writeValueAsString(roomType));
        List<RoomTypeImage> oldImages = SerializationUtils.clone(roomType).getImages();
        log.info("[updateRoomType] STEP 2.2: Get old image: {}", objectMapper.writeValueAsString(oldImages));

        // STEP 3: Save image -> set RoomTypeImage entity -> remove current image in database
        List<RoomTypeImage> imageList = storeImages(images, roomType);
        log.info("[updateRoomType] STEP 3.1: Save image: {}", objectMapper.writeValueAsString(imageList));
        roomType.setImages(imageList);
        log.info("[updateRoomType] STEP 3.2: Set room type image: {}", objectMapper.writeValueAsString(roomType));
        roomTypeImageRepo.deleteByTypeTypeId(typeId);
        log.info("[updateRoomType] STEP 3.3: Delete old foreign key");

        // STEP 4: Save room type to database
        RoomType updatedRoomType = roomTypeRepo.save(roomType);
        log.info("[updateRoomType] STEP 4: Save room to database: {}", objectMapper.writeValueAsString(updatedRoomType));

        // STEP 5: Remove old image
        log.info("[updateRoomType] STEP 5: Remove old image Started");
        oldImages.stream()
                .map(RoomTypeImage::getImage)
                .forEach(s -> {
                    roomImageService.deleteOne(s);
                });

        // STEP 6: Return response
        return RoomTypeMapper.INSTANCE.from(updatedRoomType);
    }

    private List<RoomTypeImage> storeImages(MultipartFile[] images, RoomType roomType) {
        List<RoomTypeImage> imageList = Arrays.stream(images)
                .map(image -> roomImageService.save(image))
                .map(image -> RoomTypeImageMapper.INSTANCE.from(roomType, image))
                .collect(Collectors.toList());
        return imageList;
    }

    private void validateIsImageEmpty(MultipartFile[] images) {
        if (images.length <= 0) {
            throw new NotFoundException(EMPTY_LIST, "Image array is empty");
        }
    }

    @Override
    public RoomResponse updateRoom(RoomRequest request, int roomId) {
        // STEP 1: Validation
        roomRepo.findRoomByRoomIdNotAndRoomName(roomId, request.getRoomName())
                .ifPresent(room -> {
                    throw new DuplicateDataException(DUPLICATE_ROOM_NAME,
                            MessageFormat.format("The room with name: {0} is already exist in the database.", room.getRoomName()));
                });

        // STEP 1.1: Retrieve exist room
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND,
                        MessageFormat.format("The room with id: {0} does not exist in the database.", roomId)));

        // STEP 1.2: Retrieve room type
        RoomType roomType = roomTypeRepo.findById(request.getTypeId())
                .orElseThrow(() -> new NotFoundException(ROOM_TYPE_NOT_FOUND,
                        MessageFormat.format("The room type with id: {0} does not exist in the database.", request.getTypeId())));

        // STEP 2: Mapped request to entity
        RoomMapper.INSTANCE.update(room, request, roomType);

        // STEP 3: Save room to database
        Room updatedRoom = roomRepo.save(room);

        // STEP 4: Return response
        return RoomMapper.INSTANCE.from(updatedRoom);
    }

    @Override
    public RoomTypeWithRoomResponse getRoomTypeWithRoom(Integer typeId) {
        // STEP 1: Validation + Retrieve room type from database
        RoomType roomType = roomTypeRepo.findById(typeId)
                .orElseThrow(() -> new NotFoundException(ROOM_TYPE_NOT_FOUND,
                        MessageFormat.format("The room type with id: {0} does not exist in the database.", typeId)));

        // STEP 2: Retrieve rooms this type from database
        List<Room> roomList = roomRepo.findRoomByRoomType(roomType);

        // STEP 3: Mapped entities to response
        RoomTypeWithRoomResponse response = RoomTypeMapper.INSTANCE.from(roomType, roomList);

        // STEP 4: Return response
        return response;
    }

    @Override
    public void deleteRoomType(int typeId) {
        // STEP 1: Check if delete room type available
        roomTypeRepo.findById(typeId)
                .orElseThrow(() -> new NotFoundException(ROOM_TYPE_NOT_FOUND,
                        MessageFormat.format("The room type with id: {0} does not exist in the database.", typeId)));

        // STEP 2: Check if room type is deletable (TODO: implemented that later. with better approach than cascade delete)

        // STEP 3: Delete the room
        roomTypeRepo.deleteById(typeId);
    }

    @Override
    public void deleteRoom(int roomId) {
        // STEP 1: Check if delete room available
        roomRepo.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ROOM_NOT_FOUND,
                        MessageFormat.format("The room with id: {0} does not exist in the database.", roomId)));

        // STEP 2: Check if room is deletable (TODO: implemented that later. with better approach than cascade delete)


        // STEP 3: Delete the room
        roomRepo.deleteById(roomId);

    }

    @SneakyThrows
    @Override
    public List<RoomTypeResponse> filterRoomWith(RoomFilterRequest request) {
        // STEP 1: Validation -> Check if date eligible
        validateDateEligibility(request);

        // STEP 2: Retrieve room type by checkin - checkout date
        /*
         * Retrieve reservation that was time overlapped
         * **COUNT** retrieve data and compare to room count for each type
         * if reservation count not equals to room count then return that room type
         */
        List<RoomType> roomTypeList = roomTypeRepo.findAll();
        List<RoomTypeCountQuery> roomTypeCountQueryList = roomTypeRepo.countTotalRoomByRoomTypeInterface();
        Map<Integer, Long> roomTypeCountMap = roomTypeCountQueryList.stream()
                .collect(Collectors.toMap(RoomTypeCountQuery::getTypeId, RoomTypeCountQuery::getRoomCount));
        List<RoomTypeCountQuery> reservedRoomQueryList = reservationRepo.countReservedRoom(request.getCheckInDate(), request.getCheckOutDate());
        Map<Integer, Long> reservedRoomMap = reservedRoomQueryList.stream()
                .collect(Collectors.toMap(RoomTypeCountQuery::getTypeId, RoomTypeCountQuery::getRoomCount));

        // STEP 3: Filtering data

        roomTypeList = roomTypeList.stream().filter(roomType ->
                        !roomTypeCountMap.get(roomType.getTypeId()).equals(reservedRoomMap.get(roomType.getTypeId()))
                )
                .filter(roomType -> {
                    if(request.getMinPrice() != null && roomType.getPrice().compareTo(request.getMinPrice()) < 0 ) {
                        return false;
                    }
                    if(request.getMaxPrice() != null && roomType.getPrice().compareTo(request.getMaxPrice()) > 0 ) {
                        return false;
                    }
                    if(request.getNumOfPerson() != null && roomType.getMaxCapacity() < request.getNumOfPerson()) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        // STEP 4: Mapped to response
        List<RoomTypeResponse> responses = roomTypeList.stream()
                .map(RoomTypeMapper.INSTANCE::from)
                .collect(Collectors.toList());

        return responses;
    }

    private void validateDateEligibility(RoomFilterRequest request) {
        if (request.getCheckInDate().isAfter(request.getCheckOutDate())) {
            throw new InvalidAttributeException(RESERVATION_INVALID_ATTRIBUTE, "Check in date must not after check out date");
        }

        int minDayInterval = 1; //TODO: Select minimum day interval before reservation, Change this to constant
        LocalDate minimumDateFromNow = ZonedDateTime.now().plusDays(minDayInterval).toLocalDate();
        if (minimumDateFromNow.isAfter(request.getCheckInDate().toLocalDate())
                || minimumDateFromNow.isAfter(request.getCheckOutDate().toLocalDate())) {
            throw new InvalidAttributeException(RESERVATION_DATE_TOO_CLOSE,
                    MessageFormat.format("Can reserve a room after {0} days from today which is {1}.", minDayInterval, minimumDateFromNow.format(DateTimeFormatter.ISO_LOCAL_DATE)));
        }
    }
}
