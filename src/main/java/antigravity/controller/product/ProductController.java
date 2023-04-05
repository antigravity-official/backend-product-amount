package antigravity.controller.product;

import antigravity.controller.product.request.GetProductAmountRequest;
import antigravity.controller.product.response.GetProductAmountResponse;
import antigravity.service.product.ProductService;
import antigravity.service.product.dto.GetProductAmountDto;
import antigravity.service.product.resource.GetProductAmountResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static java.util.Arrays.asList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    //상품 가격 추출 api
    @GetMapping("/amount")
    public ResponseEntity<GetProductAmountResponse> getProductAmount() {
        GetProductAmountRequest request = GetProductAmountRequest.builder()
                .productId(1L)
                .couponIds(asList(1L, 2L))
                .build();
        GetProductAmountDto dto = new GetProductAmountDto(request.getProductId(), request.getCouponIds(), LocalDateTime.now());
        GetProductAmountResource resource = service.getProductAmount(dto);
        return ResponseEntity.ok(new GetProductAmountResponse(resource));
    }
}
