package antigravity.controller;

import antigravity.global.dto.response.ProductAmountResponse;
import antigravity.service.discount.ProductAmountCalculationService;
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

    private final ProductAmountCalculationService productAmountCalculationService;

    /**
     * @param productId    - 할인 적용을 원하는 상품 [ 단일 값 ]
     * @param promotionIds - 프로모션 적용을 원하는 promotion의 Ids [ Not Null, 다중 값 가능 ]
     */
    @GetMapping("/amount")
    public ResponseEntity<ProductAmountResponse> getProductAmount(
            @RequestParam int productId,
            @RequestParam List<Integer> promotionIds
    ) {
        return new ResponseEntity<>(productAmountCalculationService.getProductAmount(productId, promotionIds), OK);
    }
}
