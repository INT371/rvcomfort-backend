/*
package sit.it.rvcomfort.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import sit.it.rvcomfort.model.entity.User;
import sit.it.rvcomfort.model.request.UserRegistrationRequest;
import sit.it.rvcomfort.model.response.FileResponse;
import sit.it.rvcomfort.service.FileService;
import sit.it.rvcomfort.service.impl.UserService;

import java.util.List;
import java.util.stream.Collectors;

TODO: Make this a controller for file / other stuff later. But for now just commented it
@AllArgsConstructor
@Controller
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileResponse>> getFileList() {
        List<FileResponse> fileInfos = this.fileService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();
            return new FileResponse(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(fileInfos);
    }

    @GetMapping(value = "/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = this.fileService.load(filename); // Get Resource File
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG).body(file); // Return Resource as IMAGE File
    }

}

*/
