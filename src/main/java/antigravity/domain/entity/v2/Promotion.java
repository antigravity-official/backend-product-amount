package antigravity.domain.entity.v2;

import antigravity.domain.entity.Product;

public interface Promotion {

    int apply(Product product);

    int apply(int productPrice);

    boolean isUsed(Promotion promotion);

}
