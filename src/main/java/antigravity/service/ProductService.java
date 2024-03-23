package antigravity.service;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request, LocalDate now) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");
        return null;
    }
}
