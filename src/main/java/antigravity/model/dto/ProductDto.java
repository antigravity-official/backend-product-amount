package antigravity.model.dto;

import antigravity.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDto {

    private Integer id;
    private String name;
    private int price;

    public static ProductDto from(Product product) {
        return new ProductDto(
            product.getId(),
            product.getName(),
            product.getPrice()
        );
    }
    
}
