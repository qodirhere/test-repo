package uz.khodirjob.meinarzt.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import uz.khodirjob.meinarzt.entity.enums.RoleEnum;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Role implements GrantedAuthority{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleEnum name; //enum

//    @ElementCollection
//    @Enumerated(EnumType.STRING)
//    private Set<PermissionEnum> permissionEnumSet;

    @Override
    public String getAuthority() {
        return name.name();
    }

}
