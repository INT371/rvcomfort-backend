package sit.it.rvcomfort.service;

import org.springframework.web.multipart.MultipartFile;
import sit.it.rvcomfort.model.request.room.*;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.model.response.RoomTypeWithRoomResponse;
import sit.it.rvcomfort.model.response.SaveRoomTypeResponse;

import java.util.List;

public interface RoomService {

    // Basic CRUD //
    List<RoomTypeResponse> getAllRoomType();
    List<RoomTypeResponse> getPagingRoomType(int pageNo, int size, String sortBy);
    List<RoomResponse> getAllRooms();                                       // return all room
    List<RoomResponse> getPagingRooms(int pageNo, int size, String sortBy);    // return all room with giving page
    RoomResponse getRoom(Integer roomId);                                   // return room of given id
    RoomResponse getRoom(String roomName);                                  // return room of given name
    RoomTypeResponse getRoomType(Integer roomTypeId);                       // return room type of given id
    RoomTypeResponse getRoomType(String roomTypeName);                      // return room type of given name

    RoomTypeResponse addRoomType(RoomTypeRequest request, MultipartFile[] images);               // add new type of room
    SaveRoomTypeResponse addRoomTypeWithRooms(MultipleRoomTypeRequest request, MultipartFile[] images);                  // add new type of room along with new room
    RoomResponse addRoomOfExistingType(RoomRequest request);                // add room of the existing type
    List<RoomResponse> addMultipleRoomOfExistingType(List<RoomRequest> requests);         // add multiple room of the existing type

    RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request, MultipartFile[] images, int typeId);
    RoomResponse updateRoom(RoomRequest request, int roomId);

    void deleteRoomType(int typeId);
    void deleteRoom(int roomId);

    // Method for business logic
    List<RoomTypeResponse> filterRoomWith(RoomFilterRequest request);

    RoomTypeWithRoomResponse getRoomTypeWithRoom(Integer typeId);


}
