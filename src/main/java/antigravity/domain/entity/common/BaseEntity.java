package antigravity.domain.entity.common;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt; //생성일시

    @LastModifiedBy
    @Column(nullable = false)
    private LocalDate updatedAt; //수정일시

}
