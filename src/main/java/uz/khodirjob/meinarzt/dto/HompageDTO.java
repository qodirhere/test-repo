package uz.khodirjob.meinarzt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.khodirjob.meinarzt.entity.Speciality;
import uz.khodirjob.meinarzt.entity.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HompageDTO {
    private List<User> userList;
    private List<Speciality> specialities;
}
