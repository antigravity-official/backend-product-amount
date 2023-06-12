package antigravity.domain.entity.v2;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AdminCode {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String name;

    @Column
    private int discountRate; // 할인 요율

    @OneToMany
    private List<UserCode> userCodes;

}
