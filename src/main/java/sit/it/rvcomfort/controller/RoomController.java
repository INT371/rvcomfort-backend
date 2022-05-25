package sit.it.rvcomfort.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.it.rvcomfort.model.request.room.*;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.model.response.RoomTypeWithRoomResponse;
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

    @GetMapping("/type/room/{typeId}")
    public RoomTypeWithRoomResponse retrieveRoomTypeWithRooms(@PathVariable("typeId") Integer typeId) {
        return service.getRoomTypeWithRoom(typeId);
    }

    @GetMapping("/type/room")
    public List<RoomTypeWithRoomResponse> retrieveAllRoomTypeWithRooms() {
        return service.getAllRoomTypeWithRoom();
    }


    @PostMapping("/type/filter")
    public List<RoomTypeResponse> filterAvailableRoomType(@Validated @RequestBody RoomFilterRequest request) {
        return service.filterRoomWith(request);
    }

    @PostMapping(value = "/type", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RoomTypeResponse saveNewRoomType(
            @RequestParam("images") MultipartFile[] images,
            @Validated @RequestPart RoomTypeRequest request) {
        return service.addRoomType(request, images);
    }

    @PostMapping(value = "/type/rooms", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public SaveRoomTypeResponse saveNewRoomTypeWithRooms(
            @RequestParam("images") MultipartFile[] images,
            @Validated @RequestPart MultipleRoomTypeRequest request) {
        return service.addRoomTypeWithRooms(request, images);
    }

    @PostMapping("/room")
    public RoomResponse saveNewRoom(@Validated @RequestBody RoomRequest request) {
        return service.addRoomOfExistingType(request);
    }

    @PostMapping("/rooms")
    public List<RoomResponse> saveMultipleNewRoom(@Validated @RequestBody List<RoomRequest> request) {
        return service.addMultipleRoomOfExistingType(request);
    }

    @PatchMapping(value = "/type/{typeId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RoomTypeResponse updateRoomType(
            @RequestParam(name = "images", required = false) MultipartFile[] images,
            @RequestPart UpdateRoomTypeRequest request,
            @PathVariable("typeId") Integer typeId) {
        return service.updateRoomType(request, images, typeId);
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
