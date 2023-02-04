package uz.khodirjob.meinarzt.controller;//package com.example.springoauth2.controller;
//
//import com.example.springoauth2.service.AuthService1;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.view.RedirectView;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//
//@RestController
//public class AuthController1 {
//
//    @Autowired
//    private AuthService1 authService;
//
//    @RequestMapping(value = "/oauth", method = RequestMethod.GET)
//    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
//        String authorize = authService.authorize();
//        System.out.println("\n\n\n\n\n\nauthorize = " + authorize);
//        return new RedirectView(authorize);
//    }
//    @RequestMapping(value = "/login/oauth2/code/google", method = RequestMethod.GET, params = "code")
//    public String oauth2Callback(@RequestParam(value = "code") String code) throws IOException {
//        String s = authService.extractAccessToken(code);
//        System.out.println("\n\n\n\n\n\n\n\n"+s);
//        return "Ketmon ketmon";
//    }
////    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
////    public ResponseEntity<String> refreshToken() {
////        String response = authService.getNewAccessTokenUsingRefreshToken();
////        return new ResponseEntity<>("Refreshed", HttpStatus.OK);
////    }
//}
