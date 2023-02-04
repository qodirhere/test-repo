package uz.khodirjob.meinarzt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.khodirjob.meinarzt.entity.Attachment;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.repository.AttachmentRepository;
import uz.khodirjob.meinarzt.repository.UserRepository;
import uz.khodirjob.meinarzt.security.UserPrincipal;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AttachmentService {

    //    private static final String uploadDirectory = "https://frantic-picture-production.up.railway.app/";
    private static final String uploadDirectory = "src/main/resources/attachments";

            private static final String down = "http://localhost:8080/api/attachment/download/";
//    private static final String down = "https://meinarzt-production.up.railway.app/api/attachment/download/";
    private final AttachmentRepository attachmentRepository;

    private final UserRepository userRepository;

    @Value(value = "${spring.datasource.url}")
    private String url;


    public ApiResponse<?> uploadFiles(MultipartHttpServletRequest request) throws IOException {

        List<String> attachPath = new ArrayList<>();

        //faqat nomlari
        Iterator<String> fileNames = request.getFileNames();
        //realniy fayllar

        while (fileNames.hasNext()) {
            List<MultipartFile> files = request.getFiles(fileNames.next());
            for (MultipartFile file : files) {
                if (file != null) {
                    String upload = upload(file);
                    attachPath.add(upload);
                }
            }
        }
        return new ApiResponse<>("Mana", true, attachPath);
    }


    public ApiResponse<?> uploadProfilePhoto(MultipartHttpServletRequest request, UserPrincipal userPrincipal) throws IOException {

        Optional<User> byId = userRepository.findById(userPrincipal.getId());
        if (byId.isEmpty())
            return new ApiResponse<>("User not found", false);

        User user = byId.get();
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file != null) {
            String upload = upload(file);
            user.setImageUrl(upload);
            userRepository.save(user);
            return new ApiResponse<>("File succesfully saved", true, upload);
        }
        return new ApiResponse<>("File not svae", false);
    }

    //    public void downloadProfilePhoto(User user, HttpServletResponse response) throws IOException {
//        String profilePhoto = user.getProfilePhotoPath();
//        Optional<Attachment> byName = attachmentRepository.findByName(profilePhoto);
//        if (byName.isPresent()) {
//            Attachment attachment = byName.get();
//            response.setHeader("Content-Disposition",
//                    "attachment; filename=" + attachment.getFileOriginalName());
//            response.setContentType(attachment.getContentType());
//
//            FileInputStream fileInputStream = new FileInputStream(uploadDirectory + "/" + attachment.getName());
//
//            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
//        }
//    }
    public void downloadFile(Long id, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + attachment.getFileOriginalName());
            response.setContentType(attachment.getContentType());

            FileInputStream fileInputStream = new FileInputStream(uploadDirectory + "/" + attachment.getName());

            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
        }
    }

    public void downloadPhoto(String photoName, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findByName(photoName);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + attachment.getFileOriginalName());
            response.setContentType(attachment.getContentType());

            FileInputStream fileInputStream = new FileInputStream(uploadDirectory + "/" + attachment.getName());

            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
        }
    }


    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        Attachment attachment = new Attachment();

        attachment.setFileOriginalName(originalFilename);
        attachment.setSize(file.getSize());
        attachment.setContentType(file.getContentType());

        String[] split = originalFilename.split("\\.");

        String name = UUID.randomUUID().toString() + "." + split[split.length - 1];

        attachment.setName(name);

        attachmentRepository.save(attachment);

        Path path = Paths.get(uploadDirectory + "/" + name);
        Files.copy(file.getInputStream(), path);
        return down + attachment.getName();
    }

    public List<Attachment> getInfo() {
        List<Attachment> all = attachmentRepository.findAll();
        return all;
    }
}
