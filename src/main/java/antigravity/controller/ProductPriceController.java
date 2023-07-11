package antigravity.controller;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.service.product.ProductPriceService;
import antigravity.service.product.ProductService;
import antigravity.service.promotion.PromotionService;
import antigravity.service.promotion.PromotionVerfiyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductPriceController {

    private final ProductPriceService productPriceService;
    private final PromotionService promotionService;
    private final ProductService productService;
    private final PromotionVerfiyService promotionVerfiyService;

    /**
     * @param productId    - 할인 적용을 원하는 상품 [ 단일 값 ]
     * @param promotionIds - 프로모션 적용을 원하는 promotion의 Ids [ Not Null, 다중 값 가능 ]
     */
    @GetMapping("/amount")
    public ResponseEntity<ProductAmountResponse> getProductAmount(
            @RequestParam int productId,
            @RequestParam List<Integer> promotionIds
    ) {
        boolean requestExistence = promotionVerfiyService.isPromotionRequestExistence(promotionIds);
        Product validProduct = productService.findProductById(productId);
        List<Promotion> validPromotions = promotionService.getValidMappedPromotionsByProductId(productId, promotionIds);
        return new ResponseEntity<>(productPriceService.determineToApplyDiscount(validProduct, validPromotions, requestExistence), OK);
    }
}
