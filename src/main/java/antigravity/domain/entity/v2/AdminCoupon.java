package antigravity.domain.entity.v2;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AdminCoupon {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String name;

    @Column
    private int discountAmount; // 할인 금액

    @OneToMany
    private List<UserCoupon> userCoupons;

}
