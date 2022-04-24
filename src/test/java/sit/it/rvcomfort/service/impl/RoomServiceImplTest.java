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
import sit.it.rvcomfort.model.request.room.NewRoomTypeRequest;
import sit.it.rvcomfort.model.request.room.RoomRequest;
import sit.it.rvcomfort.model.request.room.UpdateRoomTypeRequest;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.repository.RoomJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeJpaRepository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Nested
    @DisplayName("Get room type")
    class GetRoomType {

        @Test
        void get_room_type_by_id_success_case() {
            // given
            RoomType expectedRoomType = SerializationUtils.clone(roomTypeList.get(0));
            when(roomTypeRepo.findById(any(Integer.class)))
                    .thenReturn(Optional.ofNullable(expectedRoomType));

            // when
            RoomTypeResponse actualRoom = service.getRoomType(expectedRoomType.getTypeId());

            // then
            assertThat(actualRoom).usingRecursiveComparison().isEqualTo(expectedRoomType);
        }

        //TODO: Failed case after create exception class
        @Test
        @Disabled
        void get_no_room_type_by_id_then_throw_exception() {

        }

        @Test
        void get_room_type_by_name_success_case() {
            // given
            RoomType expectedRoom = SerializationUtils.clone(roomTypeList.get(0));
            when(roomTypeRepo.findByTypeName(any(String.class)))
                    .thenReturn(Optional.ofNullable(expectedRoom));

            // when
            RoomTypeResponse actualRoom = service.getRoomType(expectedRoom.getTypeName());

            // then
            assertThat(actualRoom).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

        //TODO: Failed case after create exception class
        @Test
        @Disabled
        void get_no_room_type_by_name_then_throw_exception() {

        }

    }

    @Nested
    @DisplayName("Save room functions")
    class SaveRoomFunctions {

        @Test
        void save_room_type_successfully() {
            // given
            NewRoomTypeRequest newRoomTypeRequest = NewRoomTypeRequest.builder()
                    .typeName("Super Luxurious")
                    .description("Super Luxurious Room")
                    .maxCapacity(10)
                    .price(BigDecimal.valueOf(3999.99))
                    .policy("Policy")
                    .build();

            RoomType expectRoomType = RoomType.builder()
                    .typeId(0)
                    .typeName("Super Luxurious")
                    .description("Super Luxurious Room")
                    .maxCapacity(10)
                    .price(BigDecimal.valueOf(3999.99))
                    .policy("Policy")
                    .createdAt(ZonedDateTime.now())
                    .build();

            when(roomTypeRepo.save(any(RoomType.class)))
                    .thenReturn(expectRoomType);

            // when
            RoomTypeResponse actualRoomType = service.addRoomType(newRoomTypeRequest);

            // then
            assertThat(actualRoomType).usingRecursiveComparison().isEqualTo(expectRoomType);
        }

        @Test
        @Disabled
        void save_room_type_throw_exception() {
            // TODO do after create exception
        }

        @Test
        @Disabled
        void add_room_type_with_new_room_success() {
            
        }

        @Test
        void add_room_success() {
            // given
            RoomRequest request = RoomRequest.builder()
                    .roomName("ROOM999")
                    .typeId(roomTypeList.get(0).getTypeId())
                    .build();
            Room expectedRoom = Room.builder()
                    .roomId(20)
                    .roomName(request.getRoomName())
                    .createdAt(ZonedDateTime.now())
                    .roomType(roomTypeList.get(0))
                    .build();
            when(roomRepo.save(any(Room.class)))
                    .thenReturn(expectedRoom);
            when(roomTypeRepo.findById(request.getTypeId()))
                    .thenReturn(Optional.ofNullable(expectedRoom.getRoomType()));

            // when
            RoomResponse actualResponse = service.addRoomOfExistingType(request);

            // then
            assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

        @Test
        @Disabled
        void add_multiple_rooms_success() {
        }

    }

    @Nested
    @DisplayName("Update room functions")
    class UpdateRoomFunctions {

        @Test
        void update_room_success() {
            // given
            UpdateRoomTypeRequest request = UpdateRoomTypeRequest.builder()
                    .typeName("Updated Type Name")
                    .description("Updated Description")
                    .price(BigDecimal.valueOf(99999.99))
                    .maxCapacity(20)
                    .policy("Updated Policy")
                    .build();
            RoomType roomTypeBefore = SerializationUtils.clone(roomTypeList.get(0));
            RoomType roomTypeAfter = RoomType.builder()
                    .typeName(request.getTypeName())
                    .typeId(roomTypeBefore.getTypeId())
                    .createdAt(ZonedDateTime.now().minusDays(2))
                    .policy(request.getPolicy())
                    .price(request.getPrice())
                    .maxCapacity(request.getMaxCapacity())
                    .description(request.getDescription())
                    .updatedAt(ZonedDateTime.now())
                    .build();

            when(roomTypeRepo.findById(roomTypeBefore.getTypeId()))
                    .thenReturn(Optional.of(roomTypeBefore));
            when(roomTypeRepo.save(any(RoomType.class)))
                    .thenReturn(roomTypeAfter);

            // when
            RoomTypeResponse actualResponse = service.updateRoomType(request, roomTypeBefore.getTypeId());

            // then
            assertThat(actualResponse).usingRecursiveComparison().isEqualTo(roomTypeAfter);
        }

        @Test
        void editRoom() {
            // given
            RoomRequest request = RoomRequest.builder()
                    .roomName("ROOM666")
                    .typeId(roomTypeList.get(0).getTypeId())
                    .build();
            Room roomBefore = SerializationUtils.clone(roomList.get(0));
            Room expectedRoom = Room.builder()
                    .roomId(roomBefore.getRoomId())
                    .roomName(request.getRoomName())
                    .createdAt(ZonedDateTime.now().minusDays(2))
                    .updatedAt(ZonedDateTime.now())
                    .roomType(roomTypeList.get(0))
                    .build();

            when(roomTypeRepo.findById(request.getTypeId()))
                    .thenReturn(Optional.ofNullable(expectedRoom.getRoomType()));
            when(roomRepo.findById(roomBefore.getRoomId()))
                    .thenReturn(Optional.ofNullable(roomBefore));
            when(roomRepo.save(any(Room.class)))
                    .thenReturn(expectedRoom);

            // when
            RoomResponse actualResponse = service.editRoom(request, roomBefore.getRoomId());

            // then
            assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

    }
   

    @Test
    @Disabled
    void deleteRoomType() {
        // given
        RoomType toBeDeletedRoomType = SerializationUtils.clone(roomTypeList.get(0));
        when(roomTypeRepo.findById(toBeDeletedRoomType.getTypeId()))
                .thenReturn(Optional.of(toBeDeletedRoomType));

        // when
        service.deleteRoomType(toBeDeletedRoomType.getTypeId());

        // then
        verify(roomTypeRepo, times(1)).deleteById(toBeDeletedRoomType.getTypeId());
    }

    @Test
    @Disabled
    void deleteRoom() {
        // given
        Room toBeDeletedRoom = SerializationUtils.clone(roomList.get(0));
        when(roomRepo.findById(toBeDeletedRoom.getRoomId()))
                .thenReturn(Optional.of(toBeDeletedRoom));

        // when
        service.deleteRoom(toBeDeletedRoom.getRoomId());

        // then
        verify(roomRepo, times(1)).deleteById(toBeDeletedRoom.getRoomId());
    }
}