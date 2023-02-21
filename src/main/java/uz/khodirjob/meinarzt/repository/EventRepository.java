package uz.khodirjob.meinarzt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.khodirjob.meinarzt.entity.Event;

import java.sql.Timestamp;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select distinct ev.* from event ev join event_attendees ea on ev.id = ea.event_id where (ea.attendees_id = :userId or ev.created_by = :userId) and (ev.start_time >= :startDate and ev.end_time <= :endDate)", nativeQuery = true)
    List<Event> getEvents(@Param("userId") Long userId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

}