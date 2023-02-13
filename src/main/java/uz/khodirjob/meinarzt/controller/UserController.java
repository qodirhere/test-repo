package uz.khodirjob.meinarzt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.exception.ResourceNotFoundException;
import uz.khodirjob.meinarzt.repository.UserRepository;
import uz.khodirjob.meinarzt.security.CurrentUser;
import uz.khodirjob.meinarzt.security.UserPrincipal;

@RestController(value = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

}