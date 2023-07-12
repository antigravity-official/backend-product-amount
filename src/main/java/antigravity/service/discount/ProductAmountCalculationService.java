package antigravity.service.discount;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.global.dto.response.ProductAmountResponse;
import antigravity.service.product.ProductService;
import antigravity.service.promotion.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAmountCalculationService {

    private final PromotionService promotionService;
    private final DiscountService discountService;
    private final ProductService productService;

    public ProductAmountResponse getProductAmount(int productId, List<Integer> promotionIds) {
        // 1. promotionIds = Null 요청 검증
        promotionService.verifyPromotionRequestExistence(promotionIds);

        // 2. 상품 아이디로 상품 조회
        Product validProduct = productService.findProductById(productId);

        // 3. 해당 상품 아이디에 매핑되어, 적용할 수 있는 프로모션 아이디 리스트 조회
        List<Integer> mappedPromotionIds = promotionService.findMappedPromotionIdsByProductId(productId);

        // 4. 프로모션 아이디로, 해당 상품에 적용이 가능한 모든 프로모션 리스트 조회
        List<Promotion> availablePromotions = promotionService.findAllPromotionsByIds(mappedPromotionIds);

        // 5. availablePromotions 리스트에서 promotionIds 와 일치하는, 실제 적용해야 할 프로모션 리스트
        List<Promotion> validPromotions = promotionService.findApplicablePromotions(promotionIds, availablePromotions);

        return discountService.applyDiscount(validProduct, validPromotions);
    }
}
