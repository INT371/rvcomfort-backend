package sit.it.rvcomfort.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.it.rvcomfort.model.request.room.MultipleRoomTypeRequest;
import sit.it.rvcomfort.model.request.room.RoomRequest;
import sit.it.rvcomfort.model.request.room.RoomTypeRequest;
import sit.it.rvcomfort.model.request.room.UpdateRoomTypeRequest;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.model.response.SaveRoomTypeResponse;
import sit.it.rvcomfort.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService service;

    @GetMapping("/room")
    public List<RoomResponse> retrieveAllRooms(@RequestParam(required = false, name = "page", defaultValue = "0") Integer pageNo,
                                               @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
                                               @RequestParam(required = false, name = "sort", defaultValue = "roomId") String sortBy) {
        return service.getPagingRooms(pageNo, size, sortBy);
    }

    @GetMapping("/room/id/{roomId}")
    public RoomResponse retrieveRoomById(@PathVariable("roomId") Integer roomId) {
        return service.getRoom(roomId);
    }

    @GetMapping("/room/name/{roomName}")
    public RoomResponse retrieveRoomByName(@PathVariable("roomName") String roomName) {
        return service.getRoom(roomName);
    }

    @GetMapping("/type")
    public List<RoomTypeResponse> retrieveAllRoomTypes(@RequestParam(required = false, name = "page", defaultValue = "0") Integer pageNo,
                                                       @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
                                                       @RequestParam(required = false, name = "sort", defaultValue = "typeId") String sortBy) {
        return service.getPagingRoomType(pageNo, size, sortBy);
    }

    @GetMapping("/type/id/{typeId}")
    public RoomTypeResponse retrieveRoomTypeById(@PathVariable("typeId") Integer typeId) {
        return service.getRoomType(typeId);
    }

    @GetMapping("/type/name/{typeName}")
    public RoomTypeResponse retrieveRoomTypeByName(@PathVariable("typeName") String typeName) {
        return service.getRoomType(typeName);
    }

    @PostMapping("/type")
    public RoomTypeResponse saveNewRoomType(@Validated @RequestBody RoomTypeRequest request) {
        return service.addRoomType(request);
    }

    @PostMapping("/type/rooms")
    public SaveRoomTypeResponse saveNewRoomTypeWithRooms(@Validated @RequestBody MultipleRoomTypeRequest request) {
        return service.addRoomTypeWithRooms(request);
    }

    @PostMapping("/room")
    public RoomResponse saveNewRoom(@Validated @RequestBody RoomRequest request) {
        return service.addRoomOfExistingType(request);
    }

    @PostMapping("/rooms")
    public List<RoomResponse> saveMultipleNewRoom(@Validated @RequestBody List<RoomRequest> request) {
        return service.addMultipleRoomOfExistingType(request);
    }

    @PatchMapping("/type/{typeId}")
    public RoomTypeResponse updateRoomType(@RequestBody UpdateRoomTypeRequest request, @PathVariable("typeId") Integer typeId) {
        return service.updateRoomType(request, typeId);
    }

    @PatchMapping("/room/{roomId}")
    public RoomResponse updateRoom(@RequestBody RoomRequest request, @PathVariable("roomId") Integer roomId) {
        return service.updateRoom(request, roomId);
    }

    @DeleteMapping("/type/{typeId}")
    public void deleteRoomType(@PathVariable("typeId") Integer typeId) {
        service.deleteRoomType(typeId);
    }

    @DeleteMapping("/room/{roomId}")
    public void deleteRoom(@PathVariable("roomId") Integer roomId) {
        service.deleteRoom(roomId);
    }

}
