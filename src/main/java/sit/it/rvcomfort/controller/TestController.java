package sit.it.rvcomfort.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.it.rvcomfort.model.entity.*;
import sit.it.rvcomfort.repository.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@AllArgsConstructor
public class TestController {

    private final ReportJpaRepository reportJpaRepository;
    private final RoomJpaRepository roomJpaRepository;
    private final RoomTypeJpaRepository roomTypeJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final UserJpaRepository userJpaRepository;

    // Try query table database
    @GetMapping("/db/user")
    public List<User> getAllUsers() {
        return userJpaRepository.findAll();
    }

    @GetMapping("/db/room")
    public List<Room> getAllRooms() {
        return roomJpaRepository.findAll();
    }

    @GetMapping("/db/roomtype")
    public List<RoomType> getAllRoomTypes() {
        return roomTypeJpaRepository.findAll();
    }

    @GetMapping("/db/reservation")
    public List<Reservation> getAllReservations() {
        return reservationJpaRepository.findAll();
    }

    @GetMapping("/db/report")
    public List<Report> getAllReports() {
        return reportJpaRepository.findAll();
    }

}
