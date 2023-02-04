package uz.khodirjob.meinarzt.entity.template;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
@Data
public abstract class AbsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Create qilingan vaqti
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createAt;

    //Update qilingan vaqti
    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updateAt;

    //Create qilgan user id si
    @JoinColumn(updatable = false)
    @CreatedBy
    private Long createdBy;

    //Update qilgan user id si
    @LastModifiedBy
    private Long updatedBy;

    //Active yoki yo'qliigi
    private Boolean isActive = true;

}
