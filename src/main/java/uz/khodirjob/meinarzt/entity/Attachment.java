package uz.khodirjob.meinarzt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.khodirjob.meinarzt.entity.template.AbsEntity;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Attachment extends AbsEntity {

    private String fileOriginalName;//pdp.jpg

    private long size;//1024000

    private String contentType;//image/png

    //bu file systemga saqlaganda kk boladi
    private String name;//papkani ichidan topish uchun

    public Attachment(String fileOriginalName, long size, String contentType) {
        this.fileOriginalName = fileOriginalName;
        this.size = size;
        this.contentType = contentType;
    }
}
