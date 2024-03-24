package antigravity.domain.entity.product;

import antigravity.domain.entity.promotion.Promotion;
import antigravity.domain.entity.promotionproducts.PromotionProducts;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
//@ToString(exclude = )
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    @OneToMany(mappedBy = "product")
    private List<PromotionProducts> promotionProducts = new ArrayList<>();

    @Transient
    private Set<Promotion> applicablePromotions = new HashSet<>();
    @Transient
    private int finalPrice;


    public int getDiscountsByPromotions(List<Promotion> promotionsFromRequests, LocalDate date) {
        return promotionsFromRequests.stream()
                .filter(promotion -> this.validatePromotion(promotion, date))
                .mapToInt(promotion -> promotion.getDiscountFrom(this.getPrice()))
                .sum();
    }

    public void finalizePrice(int discountPrice) {
        this.finalPrice = ((this.price - discountPrice)/ 1000) * 1000;
    }

    private boolean validatePromotion(Promotion p, LocalDate date) {
        p.isAvailableOn(date);
        this.isApplicable(p);
        return true;
    }

    private boolean isApplicable(Promotion promotion) {
        Set<Promotion> applicablePromotions = this.getApplicablePromotions();

        if(!applicablePromotions.contains(promotion)) {
            throw new RuntimeException("some promotions are not allowed for this product");
        }
        return true;
    }

    private Set<Promotion> getApplicablePromotions() {
        if(this.applicablePromotions.isEmpty()) {
            this.applicablePromotions = this.getPromotionProducts().stream()
                    .map(PromotionProducts::getPromotion)
                    .collect(Collectors.toSet());
        }
        return this.applicablePromotions;
    }

    @Builder
    private Product(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
