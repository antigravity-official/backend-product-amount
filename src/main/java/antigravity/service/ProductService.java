package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.mapper.ProductMapper;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.ServerException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final PromotionProductService promotionProductService;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) throws ServerException {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");
        log.info("Request: {}", request);
        Product product = productMapper.getProduct(request.getProductId());

        if (product == null) {
            throw new ServerException("가격 추출 할 상품이 존재하지 않습니다.");
        }

        int discountPrice = 0;
        double discountAmount = 0d;
        if (request.getCouponIds() != null && request.getCouponIds().length > 0) {
            discountAmount = promotionProductService.getPromotionsDiscountPriceByProductId(product.getId(), product.getPrice(), request.getCouponIds());
            discountPrice = (int) (product.getPrice() - discountAmount);
        }

        int finalPrice = discountPrice - (discountPrice % 1000);

        log.info("Product Origin Price: {}, Discount Amount: {}, Discount Price: {}, Final Price: {}"
                , product.getPrice(), discountAmount, discountPrice, finalPrice);

        if (finalPrice < 10000 || finalPrice > 10000000) {
            throw new ServerException("최종 가격이 ₩ 10,000 을 미달하거나, ₩ 10,000,000을 초과합니다.");
        }

        return ProductAmountResponse
                .builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
    }
}
