package antigravity.domain.entity.product;

import antigravity.domain.entity.promotionproducts.PromotionProducts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    @OneToMany(mappedBy = "product")
    private List<PromotionProducts> promotionProducts = new ArrayList<>();

    @Builder
    private Product(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
