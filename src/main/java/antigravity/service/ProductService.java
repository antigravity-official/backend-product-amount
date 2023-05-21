package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        // 상품이 존재하는지
        Product product = getProduct(request.getProductId());

        // coupon ids를 돌면서 할인 적용
        for (int i=0 ; i < request.getCouponIds().length; i++){
            Promotion promotion = getProductPromotion(request.getProductId(),request.getCouponIds()[i]);

        }

        // 최종 가격
        // 최소 최대 가격 검증 및 천단위 절삭

        return null;
    }

    private Product getProduct(int productId){
        return productRepository.getProduct(productId);
    }

    private Promotion getProductPromotion(int productId, int couponId){
        return promotionRepository.findById(couponId);
    }
}
