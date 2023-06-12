package antigravity.domain.entity.v2;

import antigravity.domain.entity.Product;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class UserCoupon implements Promotion {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private UUID uuid;

    @Column
    private boolean isUsed;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @ManyToOne
    private AdminCoupon adminCoupon;


    @Override
    public int apply(Product product) {
        return 0;
    }

    @Override
    public int apply(int productPrice) {
        return 0;
    }

    @Override
    public boolean isUsed(Promotion promotion) {
        return false;
    }
}
