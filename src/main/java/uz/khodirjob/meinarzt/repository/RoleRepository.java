package uz.khodirjob.meinarzt.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.khodirjob.meinarzt.entity.Role;
import uz.khodirjob.meinarzt.entity.enums.RoleEnum;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
