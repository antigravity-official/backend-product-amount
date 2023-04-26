package antigravity.service;

import antigravity.exception.BaseApiException;
import antigravity.exception.PromotionProductNotFoundException;
import antigravity.exception.ProductNotFoundException;
import antigravity.exception.PromotionNotFoundException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.rds.entity.Promotion;
import antigravity.rds.entity.PromotionProducts;
import antigravity.rds.mapper.ProductMapper;
import antigravity.rds.repository.ProductRepository;
import antigravity.rds.repository.PromotionProductRepository;
import antigravity.rds.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionProductRepository promotionProductRepository;
    private Integer MIN_RESULT_PRICE = 10000;
    @Transactional(rollbackFor = Exception.class)
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) throws BaseApiException {

        /**
         * 유효성 검사
         */
        validate(request);

        /**
         * 상품 최종금액 계산
         */
        ProductAmountResponse product = productMapper.price(request.getProductId(), request.getCouponIds())
                .orElseThrow(PromotionProductNotFoundException::new);

        /**
         * 최종 금액이 10,000원 보다 작은지 검사
         */
        Integer finalPrice = product.getFinalPrice();
        if(finalPrice < MIN_RESULT_PRICE)
            throw new BaseApiException(1004);

        /**
         * 1000단위 절삭
         */
        String priceStr = Integer.toString(finalPrice);
        priceStr = priceStr.substring(0, priceStr.length() - 3).concat("000");
        product.setFinalPrice(Integer.parseInt(priceStr));

        return product;
    }

    private void validate(ProductInfoRequest request) {
        /**
         * 상품이 존재하는지 검사
         */
        productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);

        List<Promotion> promotions = new ArrayList<>();
        Arrays.stream(request.getCouponIds()).forEach(promotionId -> {
            /**
             * 쿠폰이 존재하는지 검사
             */
            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(PromotionNotFoundException::new);
            promotions.add(promotion);

            /**
             * 쿠폰의 유효기간 검사
             */
            LocalDateTime startedAt = promotion.getUseStartedAt();
            LocalDateTime endedAt = promotion.getUseEndedAt();


            /**
             * 해당 쿠폰들이 상품과 매핑되어 있는지 검사
             */
            promotionProductRepository.findByPromotionIdAndProductId(promotionId, request.getProductId())
                    .orElseThrow(PromotionProductNotFoundException::new);

        });

        /**
         * 한 상품에 적용하는 쿠폰 중 같은 promotion_type이 2개이상 있는지 검사
         * (%할일 쿠폰을 중복해서 적용 못함)
         * (금액 할일 쿠폰을 중복해서 적용 못함)
         * (%할인, 금액할인 쿠폰은 동시 적용 가능)
         */
        promotions.stream().collect(Collectors.groupingBy(Promotion::getPromotionType))
                .entrySet().forEach(it -> isOne(it.getValue()));
    }


    private void isOne(List<Promotion> value) {
        if(value.size() > 1)
            new BaseApiException(1003);
    }
}
