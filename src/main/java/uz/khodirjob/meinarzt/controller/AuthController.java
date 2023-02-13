package uz.khodirjob.meinarzt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;
import uz.khodirjob.meinarzt.entity.AuthProvider;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.exception.BadRequestException;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.payload.AuthResponse;
import uz.khodirjob.meinarzt.payload.LoginRequest;
import uz.khodirjob.meinarzt.payload.SignUpRequest;
import uz.khodirjob.meinarzt.repository.UserRepository;
import uz.khodirjob.meinarzt.security.CurrentUser;
import uz.khodirjob.meinarzt.security.TokenProvider;
import uz.khodirjob.meinarzt.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        System.out.println("\n\n\n\ntoken = " + token);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) throws Exception {
        String authorize = authService.authorize();

        var response = authService.registerUser(signUpRequest);

        return ResponseEntity.status(response.isSuccess() ? 200 : 401).body(response);
    }

    @RequestMapping(value = "/oauth", method = RequestMethod.GET)
    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
        String authorize = authService.authorize();
        System.out.println("\n\n\n\n\n\nauthorize = " + authorize);
        return new RedirectView(authorize);
    }

    @RequestMapping(value = "/oauth", method = RequestMethod.GET, params = "code")
    public ResponseEntity<?> oauth2Callback(@RequestParam(value = "code") String code) {
        String s = authService.extractAccessToken(code);

        return ResponseEntity.status(200).body(new ApiResponse<>("Succes", true, s));
    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    public ResponseEntity<String> refreshToken() {
        String response = authService.getNewAccessTokenUsingRefreshToken();
        return new ResponseEntity<>("Refreshed", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity me(@CurrentUser User user) {
        System.out.println("user = " + user);
        return ResponseEntity.ok(user);
    }
}