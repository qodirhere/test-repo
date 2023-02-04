package uz.khodirjob.meinarzt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.khodirjob.meinarzt.dto.PositionDto;
import uz.khodirjob.meinarzt.entity.Speciality;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.service.SpecialityService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/speciality")
@RequiredArgsConstructor
public class SpecialityController {

    private final SpecialityService specialityService;

    //yangi bolim yaratish
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addCaste(@RequestBody PositionDto positionDto) {
        ApiResponse<Speciality> apiResponse = specialityService.addPosition(positionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //barcha bolimlar
    @GetMapping
    public ResponseEntity<?> getAllPosition() {
        ApiResponse<List<Speciality>> allPosition = specialityService.getAllPosition();
        return ResponseEntity.ok().body(allPosition);
    }

    //bolimning nomini ozgartirish
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePositionName(@PathVariable Long id, @RequestBody PositionDto positionDto) {
        ApiResponse<?> apiResponse = specialityService.editPosition(id, positionDto);

        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //bolimni delet qilish
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> updatePositionName(@PathVariable Long id) {
        ApiResponse<?> apiResponse = specialityService.deletePosition(id);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/upload/photo/{id}")
    public ResponseEntity<?> upload(@PathVariable Long id,  MultipartHttpServletRequest request) throws IOException {

        var response = specialityService.uploadPhoto(id, request);

        return ResponseEntity.status(response.isSuccess() ? 200 : 401).body(response);
    }


}
