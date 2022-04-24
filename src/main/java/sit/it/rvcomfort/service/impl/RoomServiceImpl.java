package sit.it.rvcomfort.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.it.rvcomfort.mapper.RoomMapper;
import sit.it.rvcomfort.mapper.RoomTypeMapper;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.request.room.NewRoomTypeRequest;
import sit.it.rvcomfort.model.request.room.RoomRequest;
import sit.it.rvcomfort.model.request.room.UpdateRoomTypeRequest;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.repository.RoomJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeJpaRepository;
import sit.it.rvcomfort.service.RoomService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomJpaRepository roomRepo;
    private final RoomTypeJpaRepository roomTypeRepo;

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
                .orElseThrow(() -> new RuntimeException("")); //TODO: Exception
    }

    @Override
    public RoomResponse getRoom(String roomName) {
        return roomRepo.findByRoomName(roomName)
                .map(RoomMapper.INSTANCE::from)
                .orElseThrow(() -> new RuntimeException("")); //TODO: Exception
    }

    @Override
    public RoomTypeResponse getRoomType(Integer roomTypeId) {
        return roomTypeRepo.findById(roomTypeId)
                .map(RoomTypeMapper.INSTANCE::from)
                .orElseThrow(() -> new RuntimeException("")); //TODO: Exception
    }

    @Override
    public RoomTypeResponse getRoomType(String roomTypeName) {
        return roomTypeRepo.findByTypeName(roomTypeName)
                .map(RoomTypeMapper.INSTANCE::from)
                .orElseThrow(() -> new RuntimeException("")); //TODO: Exception
    }

    @Override
    public RoomTypeResponse addRoomType(NewRoomTypeRequest request) {
        // STEP 1: TODO Validation

        // STEP 2: Mapped to entity
        RoomType roomType = RoomTypeMapper.INSTANCE.from(request);
        // STEP 3: Save entity
        RoomType savedRoom = roomTypeRepo.save(roomType);
        // STEP 4: Mapped to response then return
        return RoomTypeMapper.INSTANCE.from(savedRoom);
    }

    @Override
    public void addRoomTypeWithRooms(NewRoomTypeRequest request) {
        // STEP 1: Add New RoomType
        // STEP 1.1: TODO Validation + Validate if rooms are available

        // STEP 1.2: Mapped to entity
        RoomType roomType = RoomTypeMapper.INSTANCE.from(request);
        // STEP 1.3: Save entity
        RoomType savedRoom = roomTypeRepo.save(roomType);

        // STEP 2: Add new room         // TODO: This need to be change in the future
        this.addMultipleRoomOfExistingType(request.getRooms());
    }

    @Override
    public RoomResponse addRoomOfExistingType(RoomRequest request) {
        // STEP 1: Validate

        // STEP 1.2: Get RoomType from Id
        RoomType roomType = roomTypeRepo.findById(request.getTypeId())
                .orElseThrow(() -> new RuntimeException("")); //TODO Exception thrown

        // STEP 2: Mapped request to entity
        Room room = RoomMapper.INSTANCE.from(request, roomType);

        // STEP 3: Save entity
        Room savedRoom = roomRepo.save(room);

        // STEP 4: Mapped entity to response then return
        return RoomMapper.INSTANCE.from(savedRoom);
//        return RoomMapper.INSTANCE.from(roomRepo.save(RoomMapper.INSTANCE.from(request, roomTypeRepo.findById(request.getTypeId()).orElseThrow(() -> new RuntimeException("")))));  // One line version
    }

    @Override
    public void addMultipleRoomOfExistingType(List<RoomRequest> requests) {
        requests.stream()
                .forEach(this::addRoomOfExistingType);
        // TODO: This need to be change in the future
    }

    @Override
    public RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request, int typeId) {
        // STEP 1: Validation
        // STEP 1.1:

        // STEP 1.2: Validate exist room type
        roomTypeRepo.findById(typeId)
                .orElseThrow(() -> new RuntimeException("")); // TODO: Exception thrown

        // STEP 2: Mapped request to entity
        RoomType roomType = RoomTypeMapper.INSTANCE.from(request);
        roomType.setTypeId(typeId);

        // STEP 3: Save room type to database
        RoomType updatedRoomType = roomTypeRepo.save(roomType);

        // STEP 4: Return response
        return RoomTypeMapper.INSTANCE.from(updatedRoomType);
    }

    @Override
    public RoomResponse editRoom(RoomRequest request, int roomId) {
        // STEP 1: Validation
        // STEP 1.1: Validate exist room
        roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("")); // TODO: Exception thrown

        // STEP 1.2: Retrieve room type
        RoomType roomType = roomTypeRepo.findById(request.getTypeId())
                .orElseThrow(() -> new RuntimeException(""));// TODO: Exception thrown

        // STEP 2: Mapped request to entity
        Room room = RoomMapper.INSTANCE.from(request, roomType);
        room.setUpdatedAt(ZonedDateTime.now());

        // STEP 3: Save room to database
        Room updatedRoom = roomRepo.save(room);

        // STEP 4: Return response
        return RoomMapper.INSTANCE.from(updatedRoom);
    }

    @Override
    public void deleteRoomType(int typeId) {
        // STEP 1: Check if delete room type available
        roomTypeRepo.findById(typeId)
                .orElseThrow(() -> new RuntimeException("")); // TODO: Exception thrown

        // STEP 2: Check if room type is deletable (TODO: implemented that later. with better approach than cascade delete)

        // STEP 3: Delete the room
        roomTypeRepo.deleteById(typeId);
    }

    @Override
    public void deleteRoom(int roomId) {
        // STEP 1: Check if delete room available
        roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("")); // TODO: Exception thrown

        // STEP 2: Check if room is deletable (TODO: implemented that later. with better approach than cascade delete)

        // STEP 3: Delete the room
        roomRepo.deleteById(roomId);

    }
}
