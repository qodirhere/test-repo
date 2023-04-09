package uz.khodirjob.meinarzt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.khodirjob.meinarzt.dto.RegisterDTO;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.exception.ResourceNotFoundException;
import uz.khodirjob.meinarzt.repository.UserRepository;
import uz.khodirjob.meinarzt.security.CurrentUser;
import uz.khodirjob.meinarzt.security.UserPrincipal;
import uz.khodirjob.meinarzt.service.UserService;

@RestController(value = "/user")
@RequiredArgsConstructor
public class UserController {


    private final UserRepository userRepository;

    private final UserService userService;


    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUser(RegisterDTO registerDTO) {
        var response = userService.editUser(registerDTO);
        return ResponseEntity.ok(response);
    }

}