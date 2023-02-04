package uz.khodirjob.meinarzt.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RegisterDTO {
    private String email;
    private String firstName;
    private String lastName;
    private Long specialitysId;
    private String password;
    private Boolean isDoctor;
    private List<String> attachPaths;
    private Boolean gender;
}
