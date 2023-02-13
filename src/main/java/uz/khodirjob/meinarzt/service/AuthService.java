package uz.khodirjob.meinarzt.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.oauth2.model.Userinfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.khodirjob.meinarzt.entity.AuthProvider;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.payload.SignUpRequest;
import uz.khodirjob.meinarzt.repository.UserRepository;
import uz.khodirjob.meinarzt.service.UserService;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private GoogleClientSecrets clientSecrets;
    private GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;
    private Credential credential;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectURI;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${app.auth.tokenExpirationMsec}")
    private Long tokenExpirationMsec;

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    /**
     * Open google login page
     *
     * @return
     * @throws Exception
     */
    public String authorize() throws Exception {
        Collection<String> SCOPES = new ArrayList<String>();
        SCOPES.add(CalendarScopes.CALENDAR);
        SCOPES.add(Oauth2Scopes.USERINFO_EMAIL);
        SCOPES.add(Oauth2Scopes.USERINFO_PROFILE);

        if (googleAuthorizationCodeFlow == null) {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    SCOPES).setAccessType("offline").setApprovalPrompt("force").build();
        }
        AuthorizationCodeRequestUrl authorizationUrl = googleAuthorizationCodeFlow.newAuthorizationUrl().setRedirectUri(redirectURI);
        return authorizationUrl.build();
    }

    /**
     * To extract access token and user info after signin
     *
     * @param code
     * @return
     */
    public String extractAccessToken(String code) {
        try {
            TokenResponse response = googleAuthorizationCodeFlow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            credential = googleAuthorizationCodeFlow.createAndStoreCredential(response, "userID");
            Oauth2 oauth2Client =
                    new Oauth2.Builder(httpTransport, JSON_FACTORY, credential)
                            .setApplicationName("Oauth")
                            .build();

            Userinfo userInfo = oauth2Client.userinfo().get().execute();
            User user = userService.registerUser(response, userInfo);

            return generateToken(user);
        } catch (Exception e) {
            System.out.println(e);
            return "Could not save token";
        }
    }

    /**
     * Generate new access token from the refresh token saved in DB
     *
     * @return
     */
    public String getNewAccessTokenUsingRefreshToken() {
        try {
            User user = userService.getCurrentUser();
            String newToken = getNewAccessToken(user.getRefreshToken(), clientId, clientSecret);
            user.setAccessToken(newToken);
            userService.saveUser(user);
            return "Access token updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error encountered while updating access token";
        }
    }

    public String getNewAccessToken(String refreshToken, String clientId, String clientSecret) throws IOException {
        ArrayList<String> SCOPES = new ArrayList<>();

        SCOPES.add(CalendarScopes.CALENDAR);
        SCOPES.add(Oauth2Scopes.USERINFO_EMAIL);
        SCOPES.add(Oauth2Scopes.USERINFO_PROFILE);

        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                refreshToken, clientId, clientSecret).setScopes(SCOPES).setGrantType("refresh_token").execute();

        return tokenResponse.getAccessToken();
    }

    private String generateToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpirationMsec);

        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public ApiResponse<String> registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ApiResponse<>("This user already exists", false);
        }
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        String token = generateToken(result);
        return new ApiResponse<>("Succes", true, token);
    }
}
