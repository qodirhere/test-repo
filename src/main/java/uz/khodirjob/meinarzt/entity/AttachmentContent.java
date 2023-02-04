package uz.khodirjob.meinarzt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.khodirjob.meinarzt.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToOne;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)

public class AttachmentContent extends AbsEntity {

    private byte[] asosiyContent;//asosiy content

    @OneToOne
    private Attachment attachment;


}
