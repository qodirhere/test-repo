package uz.khodirjob.meinarzt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.khodirjob.meinarzt.entity.Event;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select * from event e join event_attendees ea on e.id = ea.event_id where (ea.attendees_id = :userId or e.created_by = :userId) and (e.start_time >= :startDate and e.start_time <= :endDate)", nativeQuery = true)
     List<Event> getEvents(@Param("userId") Long userId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "select * from event e join event_attendees ea on e.id = ea.event_id where (e.start_time >= :startDate and e.start_time <= :endDate)", nativeQuery = true)
     List<Event> getEvents1(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "select * from event e join event_attendees ea on e.id = ea.event_id where (ea.attendees_id = :userId or e.created_by = :userId)", nativeQuery = true)
     List<Event> getEvents2(@Param("userId") Long userId);

}