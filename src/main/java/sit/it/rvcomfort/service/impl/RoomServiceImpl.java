package sit.it.rvcomfort.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.it.rvcomfort.mapper.RoomMapper;
import sit.it.rvcomfort.mapper.RoomTypeMapper;
import sit.it.rvcomfort.model.request.room.NewRoomTypeRequest;
import sit.it.rvcomfort.model.request.room.RoomRequest;
import sit.it.rvcomfort.model.request.room.UpdateRoomTypeRequest;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.repository.RoomJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeJpaRepository;
import sit.it.rvcomfort.service.RoomService;

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
        return null;
    }

    @Override
    public RoomTypeResponse getRoomType(String roomTypeName) {
        return null;
    }

    @Override
    public void addRoomType(NewRoomTypeRequest request) {

    }

    @Override
    public void addRoomTypeWithRooms(NewRoomTypeRequest request) {

    }

    @Override
    public void addRoomOfExistingType(RoomRequest request) {

    }

    @Override
    public void addMultipleRoomOfExistingType(List<RoomRequest> requests) {

    }

    @Override
    public void editRoomType(UpdateRoomTypeRequest request, int typeId) {

    }

    @Override
    public void editRoom(RoomRequest request, int roomId) {

    }

    @Override
    public void deleteRoomType(int typeId) {

    }

    @Override
    public void deleteRoom(int roomId) {

    }
}
