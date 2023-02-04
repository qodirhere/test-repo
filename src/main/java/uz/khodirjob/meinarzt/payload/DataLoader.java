package uz.khodirjob.meinarzt.payload;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.khodirjob.meinarzt.entity.AuthProvider;
import uz.khodirjob.meinarzt.entity.Role;
import uz.khodirjob.meinarzt.entity.Speciality;
import uz.khodirjob.meinarzt.entity.User;
import uz.khodirjob.meinarzt.entity.enums.RoleEnum;
import uz.khodirjob.meinarzt.repository.RoleRepository;
import uz.khodirjob.meinarzt.repository.SpecialityRepository;
import uz.khodirjob.meinarzt.repository.UserRepository;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
    @Value("${spring.sql.init.mode}")
    private String mode; //always

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl; //create

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final SpecialityRepository specialityRepository;

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("always") && ddl.equals("create")) {
            Role admin = new Role();
            admin.setName(RoleEnum.ROLE_ADMIN);
            Role superadmin = new Role();
            superadmin.setName(RoleEnum.ROLE_SUPER_ADMIN);
            Role user_role = new Role();
            user_role.setName(RoleEnum.ROLE_USER);

            Role save1 = roleRepository.save(admin);
            Role save2 = roleRepository.save(superadmin);
            Role save3 = roleRepository.save(user_role);


            Set<Role> roles = new HashSet<>();
            roles.add(admin);
            roles.add(superadmin);
            roles.add(user_role);


            Speciality speciality = new Speciality();
            speciality.setName("Dermotologist");
            specialityRepository.save(speciality);

            Speciality speciality1 = new Speciality();
            speciality1.setName("Pediatrician");
            specialityRepository.save(speciality1);

            Speciality speciality2 = new Speciality();
            speciality2.setName("Geriatric medicine doctor");
            specialityRepository.save(speciality2);

            User user = new User();
            user.setEmail("khodirjob@gmail.com");
            user.setRoles(admin);
            user.setSpeciality(speciality);
            user.setPassword(passwordEncoder.encode("1111"));
            user.setGender(true);
            user.setRoles(save3);
            user.setProvider(AuthProvider.local);
            user.setName("khodir");
            userRepository.save(user);

            User user1 = new User();
            user1.setEmail("khodirjob1@gmail.com");
            user1.setSpeciality(speciality);
            user1.setPassword(passwordEncoder.encode("1111"));
            user1.setGender(true);
            user1.setRoles(save1);
            user1.setProvider(AuthProvider.local);
            user1.setName("khodir");
            userRepository.save(user1);


            User user2 = new User();
            user2.setEmail("khodirjob2@gmail.com");
            user2.setSpeciality(speciality);
            user2.setPassword(passwordEncoder.encode("1111"));
            user2.setGender(true);
            user2.setRoles(save2);
            user2.setProvider(AuthProvider.local);
            user2.setName("khodir");
            userRepository.save(user2);


        }
    }
}
