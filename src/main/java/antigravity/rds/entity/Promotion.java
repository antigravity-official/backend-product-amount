package antigravity.rds.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
@Entity
@Table(name = "promotion")
@Alias("Promotion")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "promotion_type")
    private String promotionType;

    @Column(name = "name")
    private String name;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "discount_value")
    private Integer discountValue;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @Column(name = "use_started_at")
    private LocalDateTime useStartedAt;

    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @Column(name = "use_ended_at")
    private LocalDateTime useEndedAt;

}
