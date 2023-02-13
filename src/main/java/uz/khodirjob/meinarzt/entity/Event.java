package uz.khodirjob.meinarzt.entity;

import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.khodirjob.meinarzt.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Event extends AbsEntity {
    private String summary;
    private String location;
    private String description;
    private String googleMeetUrl;
    private DateTime startDateTime;
    private DateTime endDateTime;
    private String timezone; //America/Los_Angele
    @OneToMany
    private Set<User> attendees;
}
