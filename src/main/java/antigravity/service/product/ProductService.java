package antigravity.service.product;

import antigravity.domain.product.Product;
import antigravity.domain.promotion.Promotion;
import antigravity.domain.product.ProductRepository;
import antigravity.service.product.dto.GetProductAmountDto;
import antigravity.service.product.exception.ProductNotFoundException;
import antigravity.service.product.resource.GetProductAmountResource;
import antigravity.service.promotion.PromotionService;
import antigravity.service.promotion.dto.GetAvailablePromotionsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final PromotionService promotionService;

    @Transactional(readOnly = true)
    public GetProductAmountResource getProductAmount(GetProductAmountDto dto) {
        Product product = repository.findById(dto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        List<Promotion> availablePromotions = getAvailablePromotions(dto);
        long appliedTotalPrice = getPromotionsAppliedPrice(product, availablePromotions);
        return new GetProductAmountResource(
                product.getName(),
                product.getPrice(),
                product.getPrice() - appliedTotalPrice,
                appliedTotalPrice
        );
    }

    private List<Promotion> getAvailablePromotions(GetProductAmountDto dto) {
        return promotionService.getAvailablePromotions(new GetAvailablePromotionsDto(dto.getProductId(), dto.getCouponIds(), LocalDateTime.now()));
    }

    private static long getPromotionsAppliedPrice(Product product, List<Promotion> availablePromotions) {
        long result = product.getPrice();
        for (Promotion promotion : availablePromotions) {
            result = promotion.apply(result);
            if (result < 10_000) {
                break;
            }
        }
        return result;
    }
}
