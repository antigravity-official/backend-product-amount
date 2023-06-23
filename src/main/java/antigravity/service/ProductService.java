package antigravity.service;

import antigravity.domain.dto.ProductDto;
import antigravity.domain.dto.PromotionDto;
import antigravity.domain.dto.PromotionProductsDto;
import antigravity.exceptions.OmittedRequireFieldException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 제품의 연관된 프로모션을 조회하는 용도의 서비스
 * @author cyeji
 * @since 2023.06.23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final DiscountPolicy discountPolicy;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        if(!productRepository.existsProductById(request.getProductId())) {
            throw new OmittedRequireFieldException("요청하신 상품을 찾을 수 없습니다.");
        }

        List<PromotionProductsDto> productWithPromotions = productRepository.getProductWithPromotions(request.getProductId(), request.getCouponIds());
        Optional<ProductDto> optionalProductDto = productWithPromotions.stream().map(ProductDto::from).distinct().findFirst();

        if(optionalProductDto.isEmpty()){
            throw new OmittedRequireFieldException("상품에 적용된 프로모션이 없습니다.");
        }

        ProductDto productDto = optionalProductDto.get();
        validProduct(productDto);
        productDto.setPromotionList(productWithPromotions.stream().filter(p -> p.getProductId() == (productDto.getId())).map(PromotionDto::from).toList());
        validPromotions(productDto.getPromotionList(),request.getCouponIds());
        ProductAmountResponse productAmountResponse = ProductAmountResponse.of(productDto.getName(), productDto.getPrice());
        return discountPolicy.applyDiscountPolicy(productAmountResponse, productDto.getPrice(), productDto.getPromotionList());
    }

    private void validPromotions(List<PromotionDto> promotionList, int[] couponIds) {
        Date currentDate = Date.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if(couponIds.length != promotionList.size()){
            throw new OmittedRequireFieldException("적용할 수 없는 프로모션이 있습니다.");
        }
        promotionList.forEach(promotion ->{
            if(promotion.getUseStartedAt().after(currentDate)){
                throw new OmittedRequireFieldException("프로모션 적용기간이 아닙니다.");
            }
            if(promotion.getUseEndedAt().before(currentDate)){
                throw new OmittedRequireFieldException("프로모션 적용기간이 경과했습니다.");
            }
        });
    }

    private void validProduct(ProductDto productDto) {
        if(productDto.getPrice()<10000 || productDto.getPrice()>10000000){
            throw new OmittedRequireFieldException("상품 가격의 유효범위를 벗어났습니다.");
        }
    }

}
