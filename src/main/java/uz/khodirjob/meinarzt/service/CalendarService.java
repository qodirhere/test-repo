package uz.khodirjob.meinarzt.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.khodirjob.meinarzt.dto.CreateEventRequestDTO;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.exception.ResourceNotFoundException;
import uz.khodirjob.meinarzt.payload.ApiResponse;
import uz.khodirjob.meinarzt.repository.EventRepository;
import uz.khodirjob.meinarzt.repository.UserRepository;
import uz.khodirjob.meinarzt.security.UserPrincipal;
//import uz.khodirjob.meinarzt.entity.Event;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CalendarService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EventRepository eventRepository;

    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "GoogleCalendar";
    private static Calendar client;


    /**
     * Fetch google calendar events from caledar API
     *
     * @param startDate
     * @param endDate
     * @param search
     * @param userPrincipal
     * @return
     */
    public ApiResponse<?> fetchCalendarEvents(String startDate, String endDate, String search) {

        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ApiResponse<>("User not found", false);
        }

        Set<uz.khodirjob.meinarzt.entity.Event> eventSet = new HashSet<>();
        Events eventList = null;
        Set<User> userSet = new HashSet<>();
        String token = currentUser.getAccessToken();

        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(token);

            final DateTime startDateTime = new DateTime(startDate + "T00:00:00");
            final DateTime endDateTime = new DateTime(endDate + "T23:59:59");


            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            Calendar.Events events = client.events();

            if (search == null || search.trim().equals("")) {
                eventList = events.list("primary").setTimeZone("Asia/Kolkata").setTimeMin(startDateTime).setTimeMax(endDateTime).execute();
            } else {
                eventList = events.list("primary").setTimeZone("Asia/Kolkata").setTimeMin(startDateTime).setTimeMax(endDateTime).setQ(search).execute();
            }
            if (!eventList.isEmpty()) {
                for (Event item : eventList.getItems()) {
                    System.out.println("==================================================================================");
                    System.out.println("\n\nitem.toString() = " + item.toString());
                    System.out.println("item.toPrettyString() = " + item.toPrettyString());
                    System.out.println("==================================================================================");
                }


            }
        } catch (Exception e) {
            System.out.println("Exception = " + e);
        }

        return new ApiResponse<>("This events", true, eventList.getItems());
    }

    /**
     * Create a Google Calendar event using calendar API
     *
     * @param userPrincipal
     * @param createEventRequestDTO
     * @return
     */
    public String createGoogleCalendarEvent(CreateEventRequestDTO createEventRequestDTO) {
        try {
            String token = userService.getCurrentUser().getAccessToken();

            System.out.println(token);
            GoogleCredential credential = new GoogleCredential().setAccessToken(token);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            Event event = new Event()
                    .setSummary(createEventRequestDTO.getSummary())
                    .setLocation(createEventRequestDTO.getLocation())
                    .setDescription(createEventRequestDTO.getDescription());


            DateTime startDateTime = new DateTime(createEventRequestDTO.getStartDateTime());
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone(createEventRequestDTO.getTimezone());
            event.setStart(start);

            DateTime endDateTime = new DateTime(createEventRequestDTO.getEndDateTime());
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone(createEventRequestDTO.getTimezone());
            event.setEnd(end);

            event.setLocked(true);

            List<EventAttendee> eventAttendees = new ArrayList<>();
            for (String guest : createEventRequestDTO.getGuests()) {
                eventAttendees.add(new EventAttendee().setEmail(guest));
            }

            event.setAttendees(eventAttendees);

            EventReminder[] reminderOverrides = new EventReminder[]{
                    new EventReminder().setMethod("email").setMinutes(24 * 60),
                    new EventReminder().setMethod("popup").setMinutes(10),
            };
            Event.Reminders reminders = new Event.Reminders()
                    .setUseDefault(false)
                    .setOverrides(Arrays.asList(reminderOverrides));
            event.setReminders(reminders);

            event.setVisibility("default");

            String calendarId = "primary";

            event = client.events().insert(calendarId, event).execute();

            return event.getHtmlLink();
        } catch (Exception e) {
            return "Error encountered while crating event: " + e.getMessage();
        }
    }

    public ApiResponse<String> cretaeEvent(CreateEventRequestDTO createEventRequestDTO) {
        uz.khodirjob.meinarzt.entity.Event event = new uz.khodirjob.meinarzt.entity.Event();
        event.setDescription(createEventRequestDTO.getDescription());
        event.setLocation(createEventRequestDTO.getLocation());
        event.setSummary(createEventRequestDTO.getSummary());
        event.setStartTime(new Timestamp(new DateTime(createEventRequestDTO.getStartDateTime()).getValue()));
        event.setEndTime(new Timestamp(new DateTime(createEventRequestDTO.getStartDateTime()).getValue()));
//        event.setEndDateTime(new DateTime(createEventRequestDTO.getEndDateTime()));
        event.setGoogleMeetUrl(createEventRequestDTO.getMeetUrl());
        event.setTimezone(createEventRequestDTO.getTimezone());

        Set<User> attendees = new HashSet<>();

        for (String guest : createEventRequestDTO.getGuests()) {
            Optional<User> byEmail = userRepository.findByEmail(guest);
            byEmail.ifPresent(attendees::add);
        }
        event.setAttendees(attendees);
        eventRepository.save(event);

        return new ApiResponse<>("Succes", true);
    }

    public ApiResponse<?> getEvents(String year, String month) {
        Timestamp startTime = Timestamp.valueOf( year.trim()+"-"+month.trim()+"-01 00:00:00.000000000");
        Timestamp endTime = Timestamp.valueOf( year.trim()+"-"+month.trim()+"-31 00:00:00.000000000");
        List<uz.khodirjob.meinarzt.entity.Event> events = eventRepository.getEvents(userService.getCurrentUser().getId(), startTime, endTime);
        return new ApiResponse<>("This events", true, events);
    }
}
