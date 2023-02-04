package uz.khodirjob.meinarzt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.khodirjob.meinarzt.service.GmailService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gmail")
public class GmailController {

    private final GmailService gmailService;

    @PostMapping("/sendPassword")
    public ResponseEntity<?> sendPassword(@RequestParam String email) {
        var response = gmailService.sendPassword(email);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, String confirmationCode) {
        var response = gmailService.verifyEmail(email, confirmationCode);
        return ResponseEntity.status(200).body(response);
    }
}
