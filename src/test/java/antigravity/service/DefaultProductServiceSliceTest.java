package antigravity.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import antigravity.domain.PromotionType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.NotFoundResourceException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductJpaRepository;
import antigravity.repository.PromotionJpaRepository;

@DisplayName("service slice 테스트")
public class DefaultProductServiceSliceTest {

    @Mock
    private ProductJpaRepository productRepository;

    @Mock
    private PromotionJpaRepository promotionJpaRepository;

    private DefaultProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productService = new DefaultProductService(productRepository, promotionJpaRepository);
    }

    @Test
    @DisplayName("testGetProductAmount 성공")
    public void testGetProductAmount() {
        // Given
        long productId = 1;
        long couponId = 1;
        int productPrice = 80000;
        int discountValue = 23; // 20% discount

        Product product = new Product(productId, "product", productPrice);
        Promotion promotion = Promotion.builder()
            .id(couponId)
            .name("promotion")
            .promotionType(PromotionType.CODE)
            .discountValue(discountValue)
            .useStartedAt(LocalDate.now())
            .useEndedAt(LocalDate.now())
            .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(promotionJpaRepository.findByIdAndProductId(productId, couponId)).thenReturn(Optional.of(promotion));

        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(new long[]{couponId})
            .build();

        // When
        ProductAmountResponse response = productService.getProductAmount(request);

        // Then
        Assertions.assertEquals("product", response.name());
        Assertions.assertEquals(productPrice, response.originPrice());
        Assertions.assertEquals(19000, response.discountPrice());
        Assertions.assertEquals(61000, response.finalPrice());
    }

    @Test
    @DisplayName("testGetProductAmount 성공 -  최소 금액")
    public void testGetProductAmount_minimumPrice() {
        // Given
        long productId = 1;
        long couponId = 1;
        int productPrice = 10000;
        int discountValue = 23; // 20% discount

        Product product = new Product(productId, "product", productPrice);
        Promotion promotion = Promotion.builder()
            .id(couponId)
            .name("promotion")
            .promotionType(PromotionType.CODE)
            .discountValue(discountValue)
            .useStartedAt(LocalDate.now())
            .useEndedAt(LocalDate.now())
            .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(promotionJpaRepository.findByIdAndProductId(productId, couponId)).thenReturn(Optional.of(promotion));

        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(new long[]{couponId})
            .build();

        // When
        ProductAmountResponse response = productService.getProductAmount(request);

        // Then
        Assertions.assertEquals("product", response.name());
        Assertions.assertEquals(productPrice, response.originPrice());
        Assertions.assertEquals(0, response.discountPrice());
        Assertions.assertEquals(10000, response.finalPrice());
    }

    @Test
    @DisplayName("testGetProductAmount 실패")
    public void testGetProductAmount_notExistProduct() {
        // Given
        long productId = 1;
        long couponId = 1;
        int discountValue = 23; // 20% discount

        Promotion promotion = Promotion.builder()
            .id(couponId)
            .name("promotion")
            .promotionType(PromotionType.CODE)
            .discountValue(discountValue)
            .useStartedAt(LocalDate.now())
            .useEndedAt(LocalDate.now())
            .build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        when(promotionJpaRepository.findByIdAndProductId(productId, couponId)).thenReturn(Optional.of(promotion));

        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(new long[]{couponId})
            .build();

        // When // Then
        Assertions.assertThrows(NotFoundResourceException.class, () ->
            productService.getProductAmount(request)
        );
    }

    @Test
    @DisplayName("testGetProductAmount 실패")
    public void testGetProductAmount_notExistPromotionProduct() {
        // Given
        long productId = 1;
        long couponId = 1;
        int productPrice = 80000;

        Product product = new Product(productId, "product", productPrice);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(promotionJpaRepository.findByIdAndProductId(productId, couponId)).thenReturn(Optional.empty());

        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(new long[]{couponId})
            .build();

        // When // Then
        Assertions.assertThrows(NotFoundResourceException.class, () ->
            productService.getProductAmount(request)
        );
    }
}
