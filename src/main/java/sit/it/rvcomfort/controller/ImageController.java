package sit.it.rvcomfort.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import sit.it.rvcomfort.model.response.FileResponse;
import sit.it.rvcomfort.service.impl.file.RoomImageService;
import sit.it.rvcomfort.service.impl.file.UserImageService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/image")
public class ImageController {

    private final RoomImageService roomService;
    private final UserImageService userService;

    @GetMapping("/room")
    public ResponseEntity<List<FileResponse>> getRoomImageList() {
        List<FileResponse> fileInfos = this.roomService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ImageController.class, "getRoomImageFile", path.getFileName().toString()).build().toString();
            return new FileResponse(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    @GetMapping(value = "/room/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getRoomImageFile(@PathVariable String filename) {
        Resource file = this.roomService.load(filename); // Get Resource File
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG).body(file); // Return Resource as IMAGE File
    }

    @GetMapping("/user")
    public ResponseEntity<List<FileResponse>> getUserImageList() {
        List<FileResponse> fileInfos = this.userService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ImageController.class, "getUserImageFile", path.getFileName().toString()).build().toString();
            return new FileResponse(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    @GetMapping(value = "/user/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getUserImageFile(@PathVariable String filename) {
        Resource file = this.userService.load(filename); // Get Resource File
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG).body(file); // Return Resource as IMAGE File
    }


}
