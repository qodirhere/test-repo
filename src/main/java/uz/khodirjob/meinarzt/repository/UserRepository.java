package uz.khodirjob.meinarzt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.khodirjob.meinarzt.entity.User;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);


    @Transactional
    @Modifying
    @Query("update User u set u.confirmationCode = :confirmationCode where u.email = :email")
    void changeConfirmationCode(@Param("confirmationCode") String confirmationCode, @Param("email") String email);

    @Query("select (count(u) > 0) from User u where u.email = :email and u.confirmationCode = :confirmationCode")
    Boolean verifyConfirmationCode(@Param("email") String email, @Param("confirmationCode") String confirmationCode);

}
