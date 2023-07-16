package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.reader.ProductReader;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductReader productReader;
    private final ProductRepository repository;


    @Transactional
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        Product product = productReader.getProduct(request.getProductId());

        return null;
    }
}
