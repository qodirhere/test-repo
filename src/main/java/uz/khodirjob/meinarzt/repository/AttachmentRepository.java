package uz.khodirjob.meinarzt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.khodirjob.meinarzt.entity.Attachment;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    Optional<Attachment> findByName(String name);
}
