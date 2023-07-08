package antigravity.domain.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Product {
    private Integer id;
    private String name;
    private Money price;
}
