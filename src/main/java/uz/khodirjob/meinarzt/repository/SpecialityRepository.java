package uz.khodirjob.meinarzt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.khodirjob.meinarzt.entity.Speciality;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
}