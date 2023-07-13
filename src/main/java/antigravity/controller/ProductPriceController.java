package antigravity.controller;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.global.dto.response.ProductAmountResponse;
import antigravity.service.discount.DiscountService;
import antigravity.service.product.ProductService;
import antigravity.service.promotion.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductPriceController {

    private final PromotionService promotionService;
    private final ProductService productService;
    private final DiscountService discountService;

    /**
     * @param productId    - 할인 적용을 원하는 상품 [ 단일 값 ]
     * @param promotionIds - 프로모션 적용을 원하는 promotion의 Ids [ Not Null, 다중 값 가능 ]
     */
    @GetMapping("/amount")
    public ResponseEntity<ProductAmountResponse> getProductAmount(
            @RequestParam int productId,
            @RequestParam List<Integer> promotionIds
    ) {
        promotionService.verifyPromotionRequestExistence(promotionIds);

        // 2. 상품 아이디로 상품 조회
        Product validProduct = productService.findProductById(productId);

        // 3. 해당 상품 아이디에 매핑되어, 적용할 수 있는 프로모션 아이디 리스트 조회
        List<Integer> mappedPromotionIds = promotionService.findMappedPromotionIdsByProductId(productId);

        // 4. 프로모션 아이디로, 해당 상품에 적용이 가능한 모든 프로모션 리스트 조회
        List<Promotion> availablePromotions = promotionService.findAllPromotionsByIds(mappedPromotionIds);

        // 5. availablePromotions 리스트에서 promotionIds 와 일치하는, 실제 적용해야 할 프로모션 리스트
        List<Promotion> validPromotions = promotionService.findApplicablePromotions(promotionIds, availablePromotions);
인
        return new ResponseEntity<>(discountService.applyDiscount(validProduct, validPromotions), OK);
    }
}
