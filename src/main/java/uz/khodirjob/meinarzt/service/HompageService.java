package uz.khodirjob.meinarzt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.khodirjob.meinarzt.dto.HompageDTO;
import uz.khodirjob.meinarzt.entity.Speciality;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.repository.AttachmentRepository;
import uz.khodirjob.meinarzt.repository.SpecialityRepository;
import uz.khodirjob.meinarzt.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HompageService {

    private final UserRepository userRepository;
    private final SpecialityRepository specialityRepository;
    private final AttachmentRepository attachmentRepository;
    public ApiResponse<?> getInfo(User user) {
        List<Speciality> specialityList = specialityRepository.findAll();
        List<User> all = userRepository.findAll();
        HompageDTO hompageDTO = new HompageDTO();
        hompageDTO.setSpecialities(specialityList);
        hompageDTO.setUserList(all);
        System.out.println("\n\n\n\nhompageDTO.toString() = \n" +"\t\t\t\t"+ hompageDTO.toString());
        return new ApiResponse<>("All data", true, hompageDTO);
    }
}
