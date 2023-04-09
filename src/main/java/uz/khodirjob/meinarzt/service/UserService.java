package uz.khodirjob.meinarzt.service;


import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.services.oauth2.model.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.khodirjob.meinarzt.dto.RegisterDTO;
import uz.khodirjob.meinarzt.entity.AuthProvider;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.entity.enums.RoleEnum;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.repository.RoleRepository;
import uz.khodirjob.meinarzt.repository.SpecialityRepository;
import uz.khodirjob.meinarzt.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SpecialityRepository specialityRepository;

    /**
     * Save user information in DB
     *
     * @param tokenResponse
     * @param userInfo
     * @return
     */
    public User registerUser(TokenResponse tokenResponse, Userinfo userInfo) {
        User user;
        Optional<User> byEmail = userRepository.findByEmail(userInfo.getEmail());
        if (!byEmail.isPresent()) {
            user = new User();
            user.setFirstName(userInfo.getName());
            user.setImageUrl(userInfo.getPicture());
            user.setRoles(roleRepository.findByName(RoleEnum.ROLE_USER).get());
        } else {
            user = byEmail.get();
        }
        user.setEmailVerified(userInfo.getVerifiedEmail());
        user.setEmail(userInfo.getEmail());
        user.setProvider(AuthProvider.google);
        user.setProviderId(userInfo.getId());
        user.setAccessToken(tokenResponse.getAccessToken());
        user.setRefreshToken(tokenResponse.getRefreshToken());
//        user.setGender();
        System.out.println("userInfo.getGender() = " + userInfo.getGender());
        System.out.println("userInfo.toString() = " + userInfo.toString());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("user.toString() = " + user.toString());
        userRepository.save(user);
        return user;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication.toString() = " + authentication.toString());
        String currentPrincipalName = authentication.getName();
        System.out.println("currentPrincipalName = " + currentPrincipalName);
        Optional<User> byEmail = userRepository.findByEmail(currentPrincipalName);
        return byEmail.orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public ApiResponse<?> editUser(RegisterDTO registerDTO) {
        User currentUser = this.getCurrentUser();
        currentUser.setEmail(registerDTO.getEmail());
        currentUser.setFirstName(registerDTO.getFirstName());
        currentUser.setGender(registerDTO.getGender());
        currentUser.setLastName(registerDTO.getLastName());
        currentUser.setSpeciality(specialityRepository.findById(registerDTO.getSpecialitysId()).get());
        User save = userRepository.save(currentUser);
        return new ApiResponse<>("Succes", true, save);
    }
}
