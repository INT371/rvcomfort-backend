package sit.it.rvcomfort.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import sit.it.rvcomfort.exception.list.DuplicateDataException;
import sit.it.rvcomfort.exception.list.NotFoundException;
import sit.it.rvcomfort.exception.response.ExceptionResponse.ERROR_CODE;
import sit.it.rvcomfort.model.entity.Room;
import sit.it.rvcomfort.model.entity.RoomType;
import sit.it.rvcomfort.model.entity.RoomTypeImage;
import sit.it.rvcomfort.model.request.room.MultipleRoomTypeRequest;
import sit.it.rvcomfort.model.request.room.RoomRequest;
import sit.it.rvcomfort.model.request.room.RoomTypeRequest;
import sit.it.rvcomfort.model.request.room.UpdateRoomTypeRequest;
import sit.it.rvcomfort.model.response.RoomResponse;
import sit.it.rvcomfort.model.response.RoomTypeResponse;
import sit.it.rvcomfort.model.response.SaveRoomTypeResponse;
import sit.it.rvcomfort.repository.RoomJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeImageJpaRepository;
import sit.it.rvcomfort.repository.RoomTypeJpaRepository;
import sit.it.rvcomfort.service.impl.file.RoomImageService;
import sit.it.rvcomfort.util.TimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
class RoomServiceImplTest {

    @InjectMocks
    private RoomServiceImpl service;
    @Mock
    private RoomJpaRepository roomRepo;
    @Mock
    private RoomTypeJpaRepository roomTypeRepo;
    @Mock
    private RoomTypeImageJpaRepository roomTypeImageRepo;
    @Mock
    private RoomImageService roomImageService;
    @Mock
    private ObjectMapper objectMapper;

    private static List<RoomType> roomTypeList;
    private static List<Room> roomList;
    private static MultipartFile[] files;
    private static List<RoomTypeImage> roomTypeImageList;

    private static RoomTypeRequest roomTypeRequest;
    private static RoomType expectedRoomType;

    private static List<RoomRequest> roomRequestList;

    @BeforeEach
    void setUp() {
        // Set up easy random library to help testing service
        EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
                .excludeField(FieldPredicates.named("roomType").and(FieldPredicates.ofType(RoomType.class)).and(FieldPredicates.inClass(Room.class)))
                .excludeField(FieldPredicates.named("type").and(FieldPredicates.ofType(RoomType.class)).and(FieldPredicates.inClass(RoomTypeImage.class)))
                .excludeField(FieldPredicates.named("image").and(FieldPredicates.ofType(String.class)).and(FieldPredicates.inClass(RoomTypeImage.class)));
        EasyRandom generator = new EasyRandom(easyRandomParameters);

        // Generate list of entities
        roomTypeList = generator.objects(RoomType.class, 10).collect(Collectors.toList());

        roomList = generator.objects(Room.class, 20)
                .peek(room -> {
                    int random = Faker.instance().random().nextInt(0, roomTypeList.size() - 1);
                    room.setRoomType(roomTypeList.get(random));
                })
                .collect(Collectors.toList());

        // Generate mock for MultipartFile
        MockMultipartFile file1 = new MockMultipartFile("image", "abcde.jpg", "image/jpeg", "TEST1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("image", "12345.jpg", "image/jpeg", "TEST2".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("json", "data.json", "application/json", "TEST3".getBytes());

        files = new MultipartFile[]{file1, file2, file3};

        // Generate room type image list
        roomTypeImageList = generator.objects(RoomTypeImage.class, 3)
                .peek(roomTypeImage -> {
                    int random = Faker.instance().random().nextInt(0, roomTypeList.size() - 1);
                    roomTypeImage.setType(roomTypeList.get(random));
                    random = Faker.instance().random().nextInt(0, files.length - 1);
                    roomTypeImage.setImage(files[random].getOriginalFilename());
                })
                .collect(Collectors.toList());

        // Generate mock for
        // ** RoomType **
        roomTypeRequest = RoomTypeRequest.builder()
                .typeName("Super Luxurious")
                .description("Super Luxurious Room")
                .maxCapacity(10)
                .price(BigDecimal.valueOf(3999.99))
                .policy("Policy")
                .build();
        expectedRoomType = RoomType.builder()
                .typeId(0)
                .typeName(roomTypeRequest.getTypeName())
                .description(roomTypeRequest.getDescription())
                .maxCapacity(roomTypeRequest.getMaxCapacity())
                .price(roomTypeRequest.getPrice())
                .policy(roomTypeRequest.getPolicy())
                .images(roomTypeImageList)
                .createdAt(TimeUtils.now())
                .build();

        // ** Room **
        roomRequestList = generator.objects(RoomRequest.class, 10)
                .peek(request -> {
                    int random = Faker.instance().random().nextInt(0, roomTypeList.size() - 1);
                    request.setTypeId(roomTypeList.get(random).getTypeId());
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
            when(roomRepo.findById(anyInt()))
                    .thenReturn(Optional.ofNullable(expectedRoom));

            // when
            RoomResponse actualRoom = service.getRoom(expectedRoom.getRoomId());

            // then
            assertThat(actualRoom).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

        @Test
        void get_no_room_by_id_then_throw_exception() {
            // given
            int roomId = 420;
            when(roomRepo.findById(anyInt()))
                    .thenReturn(Optional.empty());

            // when
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.getRoom(roomId));

            // then
            assertEquals(ERROR_CODE.ROOM_NOT_FOUND.getValue(), exception.getErrorCode().getValue());
            assertEquals("The room with id: " + roomId + " does not exist in the database.", exception.getMessage());
        }

        @Test
        void get_room_by_string_success_case() {
            // given
            Room expectedRoom = SerializationUtils.clone(roomList.get(0));
            when(roomRepo.findByRoomName(anyString()))
                    .thenReturn(Optional.ofNullable(expectedRoom));

            // when
            RoomResponse actualRoom = service.getRoom(expectedRoom.getRoomName());

            // then
            assertThat(actualRoom).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

        @Test
        void get_no_room_by_string_then_throw_exception() {
            // given
            String roomName = "Room43";
            when(roomRepo.findByRoomName(anyString()))
                    .thenReturn(Optional.empty());

            // when
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.getRoom(roomName));

            // then
            assertEquals(ERROR_CODE.ROOM_NOT_FOUND.getValue(), exception.getErrorCode().getValue());
            assertEquals("The room with name: " + roomName + " does not exist in the database.", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("Get room type")
    class GetRoomType {

        @Test
        void get_room_type_by_id_success_case() {
            // given
            RoomType expectedRoomType = SerializationUtils.clone(roomTypeList.get(0));
            when(roomTypeRepo.findById(anyInt()))
                    .thenReturn(Optional.ofNullable(expectedRoomType));

            // when
            RoomTypeResponse actualRoom = service.getRoomType(expectedRoomType.getTypeId());

            // then
            assertThat(actualRoom).usingRecursiveComparison().isEqualTo(expectedRoomType);
        }

        @Test
        void get_no_room_type_by_id_then_throw_exception() {
            // given
            int id = 420;
            when(roomTypeRepo.findById(anyInt()))
                    .thenReturn(Optional.empty());

            // when
            NotFoundException exception = assertThrows(NotFoundException.class, ()
                    -> service.getRoomType(id));

            // then
            assertEquals(ERROR_CODE.ROOM_TYPE_NOT_FOUND.getValue(), exception.getErrorCode().getValue());
            assertEquals("The room type with id: " + id + " does not exist in the database.", exception.getMessage());

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

        @Test
        void get_no_room_type_by_name_then_throw_exception() {
            // given
            String name = "Room_420";
            when(roomTypeRepo.findByTypeName(anyString()))
                    .thenReturn(Optional.empty());

            // when
            NotFoundException exception = assertThrows(NotFoundException.class, ()
                    -> service.getRoomType(name));

            // then
            assertEquals(ERROR_CODE.ROOM_TYPE_NOT_FOUND.getValue(), exception.getErrorCode().getValue());
            assertEquals("The room type with name: " + name + " does not exist in the database.", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("Save room functions")
    class SaveRoomFunctions {

        @Test
        void save_room_type_success() {
            // given
            when(roomTypeRepo.findByTypeName(anyString()))
                    .thenReturn(Optional.empty());

            when(roomImageService.save(any(MultipartFile.class)))
                    .thenReturn(files[0].getOriginalFilename());

            when(roomTypeRepo.save(any(RoomType.class)))
                    .thenReturn(expectedRoomType);

            // when
            RoomTypeResponse actualRoomType = service.addRoomType(roomTypeRequest, files);

            // then
            assertThat(actualRoomType).usingRecursiveComparison().isEqualTo(expectedRoomType);
        }

        @Test
        void save_room_type_throw_duplicate_exception_when_type_name_is_exist_in_database() {
            // given
            when(roomTypeRepo.findByTypeName(anyString()))
                    .thenReturn(Optional.ofNullable(expectedRoomType));

            // when & then
            DuplicateDataException exception = assertThrows(DuplicateDataException.class,
                    () -> service.addRoomType(roomTypeRequest, files));

            assertEquals(ERROR_CODE.DUPLICATE_ROOM_TYPE.getValue(), exception.getErrorCode().getValue());
            assertEquals("The specify type name: " + roomTypeRequest.getTypeName() + " is already exist in the database."
                    , exception.getMessage());
        }

        @Test
        void save_room_type_throw_not_found_exception_when_images_is_empty() {
            // given
            MultipartFile[] emptyFiles = {};
            when(roomTypeRepo.findByTypeName(anyString()))
                    .thenReturn(Optional.empty());

            // when & then
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.addRoomType(roomTypeRequest, emptyFiles));

            assertEquals(ERROR_CODE.EMPTY_LIST.getValue(), exception.getErrorCode().getValue());
            assertEquals("Image array is empty", exception.getMessage());
        }

        @Test
        void add_room_type_with_room_success() {
            // given
            MultipleRoomTypeRequest request = MultipleRoomTypeRequest.builder()
                    .description(roomTypeRequest.getDescription())
                    .maxCapacity(roomTypeRequest.getMaxCapacity())
                    .typeName(roomTypeRequest.getTypeName())
                    .policy(roomTypeRequest.getPolicy())
                    .price(roomTypeRequest.getPrice())
                    .rooms(roomRequestList)
                    .build();

            Room expectedRoom = Room.builder()
                    .roomType(expectedRoomType)
                    .roomId(20)
                    .roomName(roomRequestList.get(0).getRoomName())
                    .createdAt(TimeUtils.now())
                    .build();

            when(roomTypeRepo.findByTypeName(anyString()))
                    .thenReturn(Optional.empty());
            when(roomImageService.save(any(MultipartFile.class)))
                    .thenReturn(files[0].getOriginalFilename());
            when(roomTypeRepo.save(any(RoomType.class)))
                    .thenReturn(expectedRoomType);
            when(roomRepo.findByRoomName(anyString()))
                    .thenReturn(Optional.empty());
            when(roomTypeRepo.findById(anyInt()))
                    .thenReturn(Optional.ofNullable(expectedRoomType));
            when(roomRepo.save(any(Room.class)))
                    .thenReturn(expectedRoom);

            // when
            SaveRoomTypeResponse actualResponse = service.addRoomTypeWithRooms(request, files);

            // then
            assertThat(actualResponse).usingRecursiveComparison().ignoringFieldsOfTypes(ArrayList.class).isEqualTo(expectedRoomType);

        }

        @Test
        void add_room_success() {
            // given
            RoomRequest request = roomRequestList.get(0);
            RoomType requestRoomType = roomTypeList.stream()
                    .filter(roomType -> roomType.getTypeId().equals(request.getTypeId()))
                    .findFirst().orElse(null);
            Room expectedRoom = Room.builder()
                    .roomId(20)
                    .roomName(request.getRoomName())
                    .createdAt(TimeUtils.now())
                    .roomType(requestRoomType)
                    .build();

            when(roomRepo.findByRoomName(anyString()))
                    .thenReturn(Optional.empty());
            when(roomTypeRepo.findById(request.getTypeId()))
                    .thenReturn(Optional.ofNullable(expectedRoom.getRoomType()));
            when(roomRepo.save(any(Room.class)))
                    .thenReturn(expectedRoom);

            // when
            RoomResponse actualResponse = service.addRoomOfExistingType(request);

            // then
            assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

        @Test
        void add_room_throw_duplicate_data_exception() {
            // given
            RoomRequest request = roomRequestList.get(0);

            when(roomRepo.findByRoomName(anyString()))
                    .thenReturn(Optional.ofNullable(roomList.get(0)));

            // when
            DuplicateDataException exception = assertThrows(DuplicateDataException.class, () -> service.addRoomOfExistingType(request));

            // then
            assertEquals(ERROR_CODE.DUPLICATE_ROOM_NAME.getValue(), exception.getErrorCode().getValue());
        }

        @Test
        void add_room_throw_room_type_not_found_exception() {
            // given
            RoomRequest request = roomRequestList.get(0);

            when(roomRepo.findByRoomName(anyString()))
                    .thenReturn(Optional.empty());

            when(roomTypeRepo.findById(anyInt()))
                    .thenReturn(Optional.empty());

            // when
            NotFoundException exception = assertThrows(NotFoundException.class, () -> service.addRoomOfExistingType(request));

            // then
            assertEquals(ERROR_CODE.ROOM_TYPE_NOT_FOUND.getValue(), exception.getErrorCode().getValue());
        }

        @Test
        void add_multiple_rooms_success() {
            // given
            RoomType requestRoomType = roomTypeList.get(0);
            Room expectedRoom = Room.builder()
                    .roomId(20)
                    .roomName(roomRequestList.get(0).getRoomName())
                    .createdAt(TimeUtils.now())
                    .roomType(requestRoomType)
                    .build();
            List<Room> expectedRoomList = new ArrayList<>();
            for (int i = 0; i < roomTypeList.size(); i++) {
                expectedRoomList.add(expectedRoom);
            }

            when(roomRepo.findByRoomName(anyString()))
                    .thenReturn(Optional.empty());
            when(roomTypeRepo.findById(anyInt()))
                    .thenReturn(Optional.ofNullable(expectedRoom.getRoomType()));
            when(roomRepo.save(any(Room.class)))
                    .thenReturn(expectedRoom);

            // when
            List<RoomResponse> actualResponse = service.addMultipleRoomOfExistingType(roomRequestList);

            // then
            assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedRoomList);
        }
    }

    @Nested
    @DisplayName("Update room functions")
    class UpdateRoomFunctions {

        @Test
        void update_room_type_success() {
            // given
            UpdateRoomTypeRequest request = UpdateRoomTypeRequest.builder()
                    .typeName("Updated Type Name")
                    .description("Updated Description")
                    .price(BigDecimal.valueOf(99999.99))
                    .maxCapacity(20)
                    .policy("Updated Policy")
                    .build();
            RoomType roomTypeBefore = SerializationUtils.clone(roomTypeList.get(0));
            roomTypeBefore.setImages(roomTypeImageList);
            RoomType roomTypeAfter = RoomType.builder()
                    .typeName(request.getTypeName())
                    .typeId(roomTypeBefore.getTypeId())
                    .createdAt(TimeUtils.now().minusDays(2))
                    .policy(request.getPolicy())
                    .price(request.getPrice())
                    .maxCapacity(request.getMaxCapacity())
                    .description(request.getDescription())
                    .images(roomTypeImageList)
                    .updatedAt(TimeUtils.now())
                    .build();

            when(roomTypeRepo.findById(roomTypeBefore.getTypeId()))
                    .thenReturn(Optional.of(roomTypeBefore));
            when(roomTypeRepo.save(any(RoomType.class)))
                    .thenReturn(roomTypeAfter);

            // when
            RoomTypeResponse actualResponse = service.updateRoomType(request, files, roomTypeBefore.getTypeId());

            // then
            assertThat(actualResponse).usingRecursiveComparison().isEqualTo(roomTypeAfter);
        }

        @Test
        void update_room_type_throw_duplicated_data() {
            // given
            UpdateRoomTypeRequest request = UpdateRoomTypeRequest.builder()
                    .typeName("Updated Type Name")
                    .description("Updated Description")
                    .price(BigDecimal.valueOf(99999.99))
                    .maxCapacity(20)
                    .policy("Updated Policy")
                    .build();

            when(roomTypeRepo.findRoomTypeByTypeIdNotAndTypeName(anyInt(), anyString()))
                    .thenReturn(Optional.of(roomTypeList.get(0)));

            // when
            DuplicateDataException exception = assertThrows(DuplicateDataException.class,
                    () -> service.updateRoomType(request, files, roomTypeList.get(0).getTypeId()));

            // then
            assertEquals(ERROR_CODE.DUPLICATE_ROOM_TYPE.getValue(), exception.getErrorCode().getValue());
        }

        @Test
        void update_room_type_throw_not_found_exception() {
            // given
            UpdateRoomTypeRequest request = UpdateRoomTypeRequest.builder()
                    .typeName("Updated Type Name")
                    .description("Updated Description")
                    .price(BigDecimal.valueOf(99999.99))
                    .maxCapacity(20)
                    .policy("Updated Policy")
                    .build();

            when(roomTypeRepo.findRoomTypeByTypeIdNotAndTypeName(anyInt(), anyString()))
                    .thenReturn(Optional.empty());
            when(roomTypeRepo.findById(anyInt()))
                    .thenReturn(Optional.empty());

            // when
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.updateRoomType(request, files, roomTypeList.get(0).getTypeId()));

            // then
            assertEquals(ERROR_CODE.ROOM_TYPE_NOT_FOUND.getValue(), exception.getErrorCode().getValue());
        }

        @Test
        void update_room_type_throw_not_found_empty_list_exception() {
            // given
            UpdateRoomTypeRequest request = UpdateRoomTypeRequest.builder()
                    .typeName("Updated Type Name")
                    .description("Updated Description")
                    .price(BigDecimal.valueOf(99999.99))
                    .maxCapacity(20)
                    .policy("Updated Policy")
                    .build();
            MultipartFile[] files = {};

            when(roomTypeRepo.findRoomTypeByTypeIdNotAndTypeName(anyInt(), anyString()))
                    .thenReturn(Optional.empty());
            when(roomTypeRepo.findById(anyInt()))
                    .thenReturn(Optional.ofNullable(roomTypeList.get(0)));

            // when
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.updateRoomType(request, files, roomTypeList.get(0).getTypeId()));

            // then
            assertEquals(ERROR_CODE.EMPTY_LIST.getValue(), exception.getErrorCode().getValue());
        }

        @Test
        void update_room_success() {
            // given
            RoomRequest request = RoomRequest.builder()
                    .roomName("ROOM666")
                    .typeId(roomTypeList.get(0).getTypeId())
                    .build();
            Room roomBefore = SerializationUtils.clone(roomList.get(0));
            Room expectedRoom = Room.builder()
                    .roomId(roomBefore.getRoomId())
                    .roomName(request.getRoomName())
                    .createdAt(TimeUtils.now().minusDays(2))
                    .updatedAt(TimeUtils.now())
                    .roomType(roomTypeList.get(0))
                    .build();

            when(roomTypeRepo.findById(request.getTypeId()))
                    .thenReturn(Optional.ofNullable(expectedRoom.getRoomType()));
            when(roomRepo.findById(roomBefore.getRoomId()))
                    .thenReturn(Optional.ofNullable(roomBefore));
            when(roomRepo.save(any(Room.class)))
                    .thenReturn(expectedRoom);

            // when
            RoomResponse actualResponse = service.updateRoom(request, roomBefore.getRoomId());

            // then
            assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedRoom);
        }

        @Test
        void update_room_throw_duplicate_data_exception() {
            // given
            RoomRequest request = RoomRequest.builder()
                    .roomName("ROOM666")
                    .typeId(roomTypeList.get(0).getTypeId())
                    .build();
            Room room = SerializationUtils.clone(roomList.get(0));

            when(roomRepo.findRoomByRoomIdNotAndRoomName(anyInt(), anyString()))
                    .thenReturn(Optional.ofNullable(room));

            // when
            DuplicateDataException exception = assertThrows(DuplicateDataException.class,
                    () -> service.updateRoom(request, room.getRoomId()));

            // then
            assertEquals(ERROR_CODE.DUPLICATE_ROOM_NAME.getValue(), exception.getErrorCode().getValue());
        }

        @Test
        void update_room_throw_not_found_exception() {
            // given
            RoomRequest request = RoomRequest.builder()
                    .roomName("ROOM666")
                    .typeId(roomTypeList.get(0).getTypeId())
                    .build();
            Room room = SerializationUtils.clone(roomList.get(0));

            when(roomRepo.findRoomByRoomIdNotAndRoomName(anyInt(), anyString()))
                    .thenReturn(Optional.empty());
            when(roomRepo.findById(anyInt()))
                    .thenReturn(Optional.empty());

            // when
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.updateRoom(request, room.getRoomId()));

            // then
            assertEquals(ERROR_CODE.ROOM_NOT_FOUND.getValue(), exception.getErrorCode().getValue());
        }

        @Test
        void update_room_throw_not_found_room_type_exception() {
            // given
            RoomRequest request = RoomRequest.builder()
                    .roomName("ROOM666")
                    .typeId(roomTypeList.get(0).getTypeId())
                    .build();
            Room room = SerializationUtils.clone(roomList.get(0));

            when(roomRepo.findRoomByRoomIdNotAndRoomName(anyInt(), anyString()))
                    .thenReturn(Optional.empty());
            when(roomRepo.findById(anyInt()))
                    .thenReturn(Optional.ofNullable(room));
            when(roomTypeRepo.findById(anyInt()))
                    .thenReturn(Optional.empty());

            // when
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.updateRoom(request, room.getRoomId()));

            // then
            assertEquals(ERROR_CODE.ROOM_TYPE_NOT_FOUND.getValue(), exception.getErrorCode().getValue());
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