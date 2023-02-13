package uz.khodirjob.meinarzt.service;


import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.services.oauth2.model.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.khodirjob.meinarzt.entity.AuthProvider;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.entity.enums.RoleEnum;
import uz.khodirjob.meinarzt.repository.RoleRepository;
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
        } else {
            user = byEmail.get();
        }
        user.setEmail(userInfo.getEmail());
        user.setFirstName(userInfo.getName());
        user.setEmailVerified(userInfo.getVerifiedEmail());
        user.setImageUrl(userInfo.getPicture());
        user.setProvider(AuthProvider.google);
        user.setProviderId(userInfo.getId());
        user.setAccessToken(tokenResponse.getAccessToken());
        user.setRefreshToken(tokenResponse.getRefreshToken());
        user.setRoles(roleRepository.findByName(RoleEnum.ROLE_USER).get());
//        user.setGender();
        System.out.println("userInfo.getGender() = " + userInfo.getGender());
        System.out.println("userInfo.toString() = " + userInfo.toString());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("user.toString() = " + user.toString());
        userRepository.save(user);
        return user;
    }

    public User getCurrentUser() {
        try {
            return userRepository.findById(1l).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
