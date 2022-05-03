package sit.it.rvcomfort.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.it.rvcomfort.model.request.reservation.ReservationRequest;
import sit.it.rvcomfort.model.response.ReservationResponse;
import sit.it.rvcomfort.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @GetMapping
    public List<ReservationResponse> retrieveAllReservations(@RequestParam(required = false, name = "page", defaultValue = "0") Integer pageNo,
                                                             @RequestParam(required = false, name = "size", defaultValue = "20") Integer size,
                                                             @RequestParam(required = false, name = "sort", defaultValue = "id") String sortBy) {
        return service.getPagingReservations(pageNo, size, sortBy);
    }

    @GetMapping("/{reservationId}")
    public ReservationResponse retrieveReservationById(@PathVariable("reservationId") Integer reservationId) {
        return service.getReservationById(reservationId);
    }

    @PostMapping("/reserve")
    public ReservationResponse reserveRoom(@Validated @RequestBody ReservationRequest request) {
        return service.reserveRoom(request);
    }

    @DeleteMapping("/cancel/{reservationId}")
    public ReservationResponse deleteRoom(@PathVariable("reservationId") Integer reservationId) {
        return service.cancelRoomReservation(reservationId);
    }

}
