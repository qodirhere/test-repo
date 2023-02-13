package uz.khodirjob.meinarzt.controller;

import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.khodirjob.meinarzt.dto.CreateEventRequestDTO;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.security.CurrentUser;
import uz.khodirjob.meinarzt.security.UserPrincipal;
import uz.khodirjob.meinarzt.service.CalendarService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;


@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping(value = "/events")
    public ResponseEntity<?> fetchCalendarEvent(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @CurrentUser UserPrincipal userPrincipal) {
        var response = calendarService.getEvents(startDate, endDate, userPrincipal);
//        var response = calendarService.fetchCalendarEvents(startDate, endDate, null, userPrincipal);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping(value = "/createEvent")
    public ResponseEntity<?> createEvent(@CurrentUser UserPrincipal userPrincipal, @RequestBody CreateEventRequestDTO createEventRequestDTO) {
        ApiResponse<String> response = calendarService.cretaeEvent(createEventRequestDTO);
        String url = calendarService.createGoogleCalendarEvent(userPrincipal, createEventRequestDTO);
        response.setObject(url);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/datetime")
    public String dateTime(){
        return LocalDateTime.now().toString();
    }

}
