package sit.it.rvcomfort.service;

import sit.it.rvcomfort.model.request.room.NewRoomTypeRequest;
import sit.it.rvcomfort.model.request.room.RoomRequest;
import sit.it.rvcomfort.model.request.room.UpdateRoomTypeRequest;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;

import java.util.List;

public interface RoomService {

    List<RoomTypeResponse> getAllRoomType();

    List<RoomTypeResponse> getPagingRoomType(int pageNo, int size, String sortBy);

    List<RoomResponse> getAllRooms();                                       // return all room

    List<RoomResponse> getPagingRooms(int pageNo, int size, String sortBy);    // return all room with giving page

    RoomResponse getRoom(Integer roomId);                                   // return room of given id

    RoomResponse getRoom(String roomName);                                  // return room of given name

    RoomTypeResponse getRoomType(Integer roomTypeId);                       // return room type of given id

    RoomTypeResponse getRoomType(String roomTypeName);                      // return room type of given name

    RoomTypeResponse addRoomType(NewRoomTypeRequest request);               // add new type of room

    void addRoomTypeWithRooms(NewRoomTypeRequest request);                  // add new type of room along with new room

    RoomResponse addRoomOfExistingType(RoomRequest request);                // add room of the existing type

    void addMultipleRoomOfExistingType(List<RoomRequest> requests);         // add multiple room of the existing type

    RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request, int typeId);

    RoomResponse editRoom(RoomRequest request, int roomId);

    void deleteRoomType(int typeId);

    void deleteRoom(int roomId);


}
