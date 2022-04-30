package sit.it.rvcomfort.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sit.it.rvcomfort.service.ReservationService;

import java.time.ZonedDateTime;

@Slf4j
class ReservationServiceImplTest {

    @Autowired
    ReservationService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void proveOfIdea() {
        ZonedDateTime start1 = ZonedDateTime.now();
        ZonedDateTime end1 = start1.plusDays(3);
        ZonedDateTime start2 = start1.plusDays(2);
        ZonedDateTime end2 = start1.plusDays(4);

        boolean isOverlapped = start1.isBefore(end2) && end1.isAfter(start2);

        log.info("Time is {}overlapped", isOverlapped?"":"not ");
    }
}