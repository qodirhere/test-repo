package uz.khodirjob.meinarzt.controller;

import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping(value = "/yearlyEvents")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> fetchCalendarEvent(@RequestParam(value = "year") Integer year) {
        var response1 = calendarService.getYearlyEvents(year);
//        var response = calendarService.fetchCalendarEvents(startDate, endDate, null);
        return ResponseEntity.status(200).body(response1);
    }

    @GetMapping(value = "/dailyEvents")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> getEvent(@RequestParam(value = "year") Integer year, @RequestParam(value = "month") Integer month, @RequestParam(value = "day") Integer day) {
        var response1 = calendarService.getEventsDay(year, month, day);
        return ResponseEntity.status(200).body(response1);
    }


    @PostMapping(value = "/createEvent")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> createEvent(@RequestBody CreateEventRequestDTO createEventRequestDTO) {
        var response = calendarService.cretaeEvent(createEventRequestDTO);
        String url = calendarService.createGoogleCalendarEvent(createEventRequestDTO);
//        response.setObject(url);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @GetMapping("/datetime")
    public String dateTime() {
        return LocalDateTime.now().toString();
    }

}
