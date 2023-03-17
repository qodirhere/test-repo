package uz.khodirjob.meinarzt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.khodirjob.meinarzt.config.GmailConfig;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.repository.UserRepository;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GmailService {

    private final GmailConfig gmailConfig;

    private final UserRepository userRepository;

    public ApiResponse<?> sendPassword(String email) {
        String password = String.valueOf(Math.random()).substring(2, 6);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Hi,\n" +
                "\n" +
                "Your authorization code " + password);
        message.setSubject("GO GO");
        message.setSentDate(new Date());
        message.setTo(email);
        JavaMailSender mailSender = gmailConfig.send();
        mailSender.send(message);
        userRepository.changeConfirmationCode(password, email);

        return new ApiResponse<>("A code has been sent to your email", true, password);
    }


    public ApiResponse<?> verifyEmail(String email, String confirmationCode) {


        if (userRepository.verifyConfirmationCode(email, confirmationCode))
            return new ApiResponse<>("Succes", true);
        else if (userRepository.existsByEmail(email))
            return new ApiResponse<>("This password is failed", false);
        else
            return new ApiResponse<>("The user does not exist", false);
    }

    public ApiResponse<?> forgotPassword(String email) {
        return sendPassword(email);
    }
}
