package antigravity.service.product;

import antigravity.domain.product.Product;
import antigravity.domain.product.ProductRepository;
import antigravity.domain.promotion.Promotion;
import antigravity.service.product.dto.GetProductAmountDto;
import antigravity.service.product.exception.ProductNotFoundException;
import antigravity.service.product.resource.GetProductAmountResource;
import antigravity.service.promotion.PromotionService;
import antigravity.service.promotion.dto.GetAvailablePromotionsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final PromotionService promotionService;
    private final FinalProductAmountCalculator finalProductAmountCalculator;

    @Transactional(readOnly = true)
    public GetProductAmountResource getProductAmount(GetProductAmountDto dto) {
        Product product = getProduct(dto.getProductId());
        List<Promotion> availablePromotions = getAvailablePromotions(dto);
        long appliedTotalPrice = finalProductAmountCalculator.getPromotionsAppliedPrice(product, availablePromotions);
        return new GetProductAmountResource(
                product.getName(),
                product.getPrice(),
                product.getPrice() - appliedTotalPrice,
                appliedTotalPrice
        );
    }

    private Product getProduct(long productId) {
        return repository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    private List<Promotion> getAvailablePromotions(GetProductAmountDto dto) {
        GetAvailablePromotionsDto getAvailablePromotionsDto = new GetAvailablePromotionsDto(
                dto.getProductId(),
                dto.getCouponIds(),
                dto.getRequestAt()
        );
        return promotionService.getAvailablePromotions(getAvailablePromotionsDto);
    }
}
