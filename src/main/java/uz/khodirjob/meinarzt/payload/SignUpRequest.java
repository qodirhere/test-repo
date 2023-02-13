package uz.khodirjob.meinarzt.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private Long specialitysId;

    private Boolean isDoctor;

    private List<String> attachPaths;

    private Boolean gender;

    // Getters and Setters (Omitted for brevity)
}
