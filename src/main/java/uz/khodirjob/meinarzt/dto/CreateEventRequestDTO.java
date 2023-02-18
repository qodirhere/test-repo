package uz.khodirjob.meinarzt.dto;

import com.google.api.client.util.DateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class CreateEventRequestDTO {
    private String summary;
    private String location;
    private String description;
    private String startDateTime;
    private String endDateTime;
    private String timezone; //America/Los_Angele
    private List<String> guests;
    private String meetUrl;
}
