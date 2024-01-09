package antigravity.service.impl;

import antigravity.domain.entity.Product;
import antigravity.exception.EntityNotFoundException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    @Transactional(readOnly = true)
    public ProductAmountResponse getProductAmount(final ProductInfoRequest request) {
        final Product product = repository
            .getProduct(request.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product with ID " +
                                                            request.getProductId() +
                                                            " not found."));

        //TODO: Promotion Calculation

        return null;
    }
}
