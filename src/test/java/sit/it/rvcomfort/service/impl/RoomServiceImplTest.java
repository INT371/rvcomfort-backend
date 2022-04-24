package sit.it.rvcomfort.service.impl;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.repository.RoomJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
class RoomServiceImplTest {

    private static List<RoomType> roomTypeList;
    private static List<Room> roomList;
    @InjectMocks
    RoomServiceImpl service;
    @Mock
    RoomJpaRepository roomRepo;
    @Mock
    RoomTypeJpaRepository roomTypeRepo;

    @BeforeAll
    static void setUp() {

        EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
                .excludeField(FieldPredicates.named("roomType").and(FieldPredicates.ofType(RoomType.class)).and(FieldPredicates.inClass(Room.class)));
        EasyRandom generator = new EasyRandom(easyRandomParameters);

        roomTypeList = generator.objects(RoomType.class, 10).collect(Collectors.toList());

        roomList = generator.objects(Room.class, 20)
                .peek(room -> {
                    int random = Faker.instance().random().nextInt(0, roomTypeList.size() - 1);
                    room.setRoomType(roomTypeList.get(random));
                })
                .collect(Collectors.toList());
    }

    @Test
    void get_all_room_type() {
        // given
        when(roomTypeRepo.findAll())
                .thenReturn(roomTypeList);

        // when
        List<RoomTypeResponse> actualRoomType = service.getAllRoomType();

        // then
        assertThat(actualRoomType).usingRecursiveComparison().isEqualTo(roomTypeList);
    }

    @Test
    void get_page_of_room_type() {
        // given
        int page = 1;
        int size = 2;
        PagedListHolder pagedListHolder = new PagedListHolder(roomTypeList);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(size);
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomType> roomTypePage = new PageImpl<>(pagedListHolder.getPageList(), pageable, roomTypeList.size());
        when(roomTypeRepo.findAll(any(PageRequest.class)))
                .thenReturn(roomTypePage);

        // when
        List<RoomTypeResponse> actualPage = service.getPagingRoomType(page, size, "typeId");

        // then
        assertThat(actualPage).usingRecursiveComparison().isEqualTo(roomTypePage);
    }

    @Test
    void get_list_of_all_rooms() {
        // given
        when(roomRepo.findAll())
                .thenReturn(roomList);

        // when
        List<RoomResponse> actualRooms = service.getAllRooms();

        // then
        assertThat(actualRooms).usingRecursiveComparison().isEqualTo(roomList);
    }

    @Test
    void get_paging_rooms() {
        // given
        int page = 0;
        int size = 4;
        PagedListHolder pagedListHolder = new PagedListHolder(roomList);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> roomPage = new PageImpl<>(pagedListHolder.getPageList(), pageable, roomList.size());
        when(roomRepo.findAll(any(PageRequest.class)))
                .thenReturn(roomPage);

        // when
        List<RoomResponse> actualPage = service.getPagingRooms(page, size, "typeId");

        // then
        assertThat(actualPage).usingRecursiveComparison().isEqualTo(roomPage);
    }

    @Test
    @Disabled
    void testGetRoomType() {
    }

    @Test
    @Disabled
    void addRoomType() {
    }

    @Test
    @Disabled
    void addRoomTypeWithRooms() {
    }

    @Test
    @Disabled
    void addRoomOfExistingType() {
    }

    @Test
    @Disabled
    void addMultipleRoomOfExistingType() {
    }

    @Test
    @Disabled
    void editRoomType() {
    }

    @Test
    @Disabled
    void editRoom() {
    }

    @Test
    @Disabled
    void deleteRoomType() {
    }

    @Test
    @Disabled
    void deleteRoom() {
    }

    @Nested
    @DisplayName("Get rooms")
    class GetRoom {

        @Test
        void get_room_by_id_success_case() {
            // given
            Room expectedRoom = SerializationUtils.clone(roomList.get(0));
            when(roomRepo.findById(any(Integer.class)))
                    .thenReturn(Optional.ofNullable(expectedRoom));

            // when
            RoomResponse actualRoom = service.getRoom(expectedRoom.getRoomId());

            // then
            assertThat(actualRoom).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

        //TODO: Failed case after create exception class
        @Test
        @Disabled
        void get_no_room_by_id_then_throw_exception() {

        }

        @Test
        void get_room_by_string_success_case() {
            // given
            Room expectedRoom = SerializationUtils.clone(roomList.get(0));
            when(roomRepo.findByRoomName(any(String.class)))
                    .thenReturn(Optional.ofNullable(expectedRoom));

            // when
            RoomResponse actualRoom = service.getRoom(expectedRoom.getRoomName());

            // then
            assertThat(actualRoom).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

        //TODO: Failed case after create exception class
        @Test
        @Disabled
        void get_no_room_by_string_then_throw_exception() {

        }

    }
}