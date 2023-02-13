package uz.khodirjob.meinarzt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.khodirjob.meinarzt.entity.Event;
public interface EventRepository extends JpaRepository<Event, Long> {

}