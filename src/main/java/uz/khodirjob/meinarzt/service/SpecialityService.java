package uz.khodirjob.meinarzt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.khodirjob.meinarzt.dto.PositionDto;
import uz.khodirjob.meinarzt.entity.Speciality;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.repository.SpecialityRepository;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialityService {
    final SpecialityRepository specialityRepository;
    private final AttachmentService attachmentService;

    public ApiResponse<Speciality> addPosition(PositionDto positionDto) {
        if (positionDto.getName().length() < 2) {
            return new ApiResponse<Speciality>("Speciality name is failed", false);
        }
        Speciality speciality = new Speciality();
        speciality.setName(positionDto.getName());
        specialityRepository.save(speciality);
        return new ApiResponse<>("Speciality succesfully saved", true);
    }

    public ApiResponse<List<Speciality>> getAllPosition() {
        List<Speciality> all = specialityRepository.findAll();
        return new ApiResponse<>("All Speciality", true, all);
    }

    public ApiResponse<?> editPosition(Long id, PositionDto positionDto) {
        Optional<Speciality> byId = specialityRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse<>("This id =  " + id + " Speciality not found", false);
        }

        if (positionDto.getName().length() < 2) {
            return new ApiResponse<Speciality>("Speciality name is failed", false);
        }

        Speciality speciality = byId.get();
        speciality.setName(positionDto.getName());
        specialityRepository.save(speciality);

        return new ApiResponse<>("Speciality succesfully edited", true);

    }

    public ApiResponse<?> deletePosition(Long id) {
        specialityRepository.deleteById(id);
        return new ApiResponse<>("Speciality succesfully deleted", true);
    }

    public ApiResponse<?> uploadPhoto(Long id, MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file != null) {
            String upload = attachmentService.upload(file);
            Optional<Speciality> byId = specialityRepository.findById(id);
            Speciality speciality = byId.get();
            speciality.setAttachmentPath(upload);
            specialityRepository.save(speciality);
            return new ApiResponse<>("Succesfuly saved avatar", true, speciality);
        }
        return new ApiResponse<>("Don't updated", false);
    }
}
