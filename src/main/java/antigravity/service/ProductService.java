package antigravity.service;

import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.service.dto.GetProductAmountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(GetProductAmountDto dto) {
//        Product product = repository.findBy(request.getProductId());
        return null;
    }
}
