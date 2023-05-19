package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.exception.CouponException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;
    private final CouponService couponService;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        Product product = repository.getProduct(request.getProductId());     

        ArrayList<HashMap<String, Integer>> couponList = new ArrayList<>();


        // 1. 쿠폰 적용 가능 여부 확인
        try {
            couponService.isCouponApplicable(request.getProductId(), request.getCouponIds(), couponList);
        } catch (CouponException e) {
            return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .errorCoupon(e.getCouponName())
                .errorMsg(e.getMessage())
                .build();
        }


        // 2. 최대 할인 금액 찾기
        int maxDiscountPrice = couponService.findMaxDiscountPrice(product.getPrice(), 0, couponList);


        return ProductAmountResponse.builder()
            .name(product.getName())
            .originPrice(product.getPrice())
            .discountPrice(product.getPrice() - maxDiscountPrice)
            .finalPrice(maxDiscountPrice)
            .build();
    }

    
}
