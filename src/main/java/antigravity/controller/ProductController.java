package antigravity.controller;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.PromotionProductsRepository;
import antigravity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PromotionProductsRepository promotionProductsRepository;

    @GetMapping("/amount")
    public ResponseEntity<ProductAmountResponse> getProductAmount(
            @RequestParam int productId
    ) {
        ProductAmountResponse response = productService.getProductAmount(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

