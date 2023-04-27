package antigravity.controller;

import antigravity.exception.BaseApiException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ExceptionResponse;
import antigravity.model.response.ProductAmountResponse;
import antigravity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    //상품 가격 추출 api
    @GetMapping("/amount")
    public ResponseEntity<Object> getProductAmount() {
        try {
            ProductAmountResponse response = service.getProductAmount(getParam());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BaseApiException e) {
            return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    private ProductInfoRequest getParam() {
        int[] couponIds = {3, 4};

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        return request;
    }
}
