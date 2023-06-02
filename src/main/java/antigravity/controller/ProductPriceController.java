package antigravity.controller;

import antigravity.dto.response.ProductAmountResponse;
import antigravity.service.product.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductPriceController {

    private final ProductPriceService productPriceService;

    @GetMapping("/amount")
    public ResponseEntity<ProductAmountResponse> getProductAmount(
            @RequestParam int productId
    ) {
        ProductAmountResponse response = productPriceService.getProductAmount(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

