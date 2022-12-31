package antigravity.service;

import static antigravity.error.dto.ErrorType.*;

import java.util.List;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.error.exception.BusinessException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    private final PromotionService promotionService;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        Product product = repository.getProduct(request.getProductId()).orElseThrow(() -> new BusinessException(
            NOT_FOUND_PRODUCT));

        if (!isValidPrice(product.getPrice())) { // 상품 가격 범위 체크
            throw new BusinessException(INVALID_PRODUCT_PRICE);
        }

        List<Promotion> promotions = promotionService.getPromotionList(request);

        int nowPrice = product.getPrice();
        for (Promotion promotion : promotions) {
            int nowDiscount = promotionService.getDiscountPrice(nowPrice, promotion);
            log.info("nowPrice: {}, nowDiscount:{}, promotion:{}", nowPrice, nowDiscount, promotion);
            nowPrice -= nowDiscount;
        }

        nowPrice = (nowPrice / 1000) * 1000; // 최종금액 천단위 절삭

        if (!isValidPrice(nowPrice)) {
            throw new BusinessException(INVALID_DISCOUNT_PRICE);
        }

        return ProductAmountResponse.builder()
            .name(product.getName())
            .originPrice(product.getPrice())
            .discountPrice(product.getPrice() - nowPrice)
            .finalPrice(nowPrice)
            .build();
    }

    public boolean isValidPrice(int price) {
        return price >= 10_000 && price <= 10_000_000;
    }
}
