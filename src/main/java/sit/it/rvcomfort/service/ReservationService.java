package sit.it.rvcomfort.service;

import sit.it.rvcomfort.model.request.reservation.ReservationRequest;
import sit.it.rvcomfort.model.response.ReservationResponse;

import java.util.List;

public interface ReservationService {

    List<ReservationResponse> getAllReservations();
    List<ReservationResponse> getPagingReservations(int pageNo, int size, String sortBy);

    ReservationResponse getReservationById(Integer id);

    List<ReservationResponse> getReservationByRoomId(Integer roomId);

    ReservationResponse reserveRoom(ReservationRequest request);

    ReservationResponse cancelRoomReservation(Integer id);

//    ReservationResponse changeRoomReservationDetail(UpdateReservationRequest request);

//    List<ReservedDate> getReservedDateByRoomType(Integer typeId);


}
