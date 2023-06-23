package antigravity.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 제품 별 프로모션을 한번에 볼 수 있는 Dto 객체
 * 데이터베이스에서 조회된 PromotionProductsDto 내용을 productId 기준으로 매핑하는 역할
 * @see PromotionProductsDto
 * @author cyeji
 * @since 2023.06.23
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {


    private int id; // 제품 식별자
    private String name; // 제품 이름
    private int price; // 가격
    List<PromotionDto> promotionList; // 제품에 적용된 프로모션들

    public static ProductDto from(PromotionProductsDto promotionProductsDto) {
        ProductDto productDto = new ProductDto();
        productDto.id = promotionProductsDto.getProductId();
        productDto .name = promotionProductsDto.getProductName();
        productDto.price = promotionProductsDto.getPrice();
        return productDto;
    }

}
