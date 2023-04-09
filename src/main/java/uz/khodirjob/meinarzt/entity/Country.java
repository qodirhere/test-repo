package uz.khodirjob.meinarzt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.khodirjob.meinarzt.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Country extends AbsEntity {
    private String name;
}
