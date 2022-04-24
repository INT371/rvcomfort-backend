package sit.it.rvcomfort.service;

import org.springframework.data.domain.Page;
import sit.it.rvcomfort.model.entity.Reservation;

import java.util.List;

public interface ReservationService {

    List<Reservation> getAllReservations(); // TODO Implement reservation service

    Page<Reservation> getPagingReservations();

    Reservation getReservation(Integer id);


}
