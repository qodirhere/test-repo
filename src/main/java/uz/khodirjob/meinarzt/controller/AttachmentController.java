package uz.khodirjob.meinarzt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.security.CurrentUser;
import uz.khodirjob.meinarzt.security.UserPrincipal;
import uz.khodirjob.meinarzt.service.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {


    private final AttachmentService attachmentService;

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {

        var response = attachmentService.getInfo();

        return ResponseEntity.ok(response);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> upload(MultipartHttpServletRequest request) throws IOException {

        var response = attachmentService.uploadFiles(request);

        return ResponseEntity.status(response.isSuccess() ? 200 : 401).body(response);
    }

    @PostMapping("/upload/profilePhoto")
    public ResponseEntity<?> uploadProfilePhoto(@CurrentUser UserPrincipal userPrincipal, MultipartHttpServletRequest request) throws IOException {

        var response = attachmentService.uploadProfilePhoto(request, userPrincipal);

        return ResponseEntity.status(response.isSuccess() ? 200 : 401).body(response);
    }

    @GetMapping("/downloadFile/{id}")
    public void getFileToFileSystem(@PathVariable Long id, HttpServletResponse response) throws IOException {
        attachmentService.downloadFile(id, response);
    }

//    @GetMapping("/download/profilePhoto/")
//    public void downloadProfilePhoto(@CurrentUser User user, HttpServletResponse response) throws IOException {
//        attachmentService.downloadProfilePhoto(user, response);
//    }

    @GetMapping("/download/{name}")
    public void downloadProfilePhoto(@PathVariable String name, HttpServletResponse response) throws IOException {
        attachmentService.downloadPhoto(name, response);
    }

}


