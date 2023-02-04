package uz.khodirjob.meinarzt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.security.CurrentUser;
import uz.khodirjob.meinarzt.service.HompageService;

@RestController
@RequestMapping("/api/hompage")
@RequiredArgsConstructor
public class HompageContorller {

    private final HompageService hompageService;

    @GetMapping
    public ResponseEntity<?> getInfo(@CurrentUser User user) {
        var apiResponse = hompageService.getInfo(user);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

}
