package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final PromotionProductsRepository promotionProductsRepository;
	private final PromotionRepository promotionRepository;
	private final ValidationService validationService;

	public ProductAmountResponse getProductAmount(ProductInfoRequest request) throws Exception {

		List<Promotion> validPromotions = new ArrayList<>();
		Product product = null;

		// productId로 Product 조회
		try {
			product = productRepository.getProduct(request.getProductId());	
		} catch (Exception e) {
			if (product == null) {
				throw new Exception("상품이 존재하지 않습니다.");
			}
			e.printStackTrace();
		}

		// 1. 상품 최소, 최대 금액 validate
		validationService.minmaxValidation(product);

		// 2. 쿠폰 유효성 validate
		// promotion_product 조회
		List<PromotionProducts> promotionProductsList = promotionProductsRepository
				.getPromotionProducts(product.getId());

		// 쿠폰 유효성 체크 (사용 기간)
		for (PromotionProducts promotionProduct : promotionProductsList) {
			Promotion promotions = promotionRepository.getPromotions(promotionProduct.getPromotionId());

			// 날짜 체크
			boolean couponValidation = validationService.couponValidation(promotions.getUse_started_at(),
					promotions.getUse_ended_at());
			
			// 유효한 프로모션 값만 담기
			if (couponValidation == true) {

				// 쿼리로 담기
				Promotion validPromotion = Promotion.builder().id(promotions.getId())
						.promotion_type(promotions.getPromotion_type()).name(promotions.getName())
						.discount_type(promotions.getDiscount_type()).discount_value(promotions.getDiscount_value())
						.use_started_at(promotions.getUse_started_at()).use_ended_at(promotions.getUse_ended_at())
						.build();
				validPromotions.add(validPromotion);
			}
		}

		// 3. 확정 상품 가격 구하기
		// productAmountResponse 값 계산
		int originPrice = product.getPrice();
		int discountPrice = 0;
		int finalPrice = originPrice;
		

		if (!validPromotions.isEmpty()) {
			// 쿠폰이 1개인 경우
			if (validPromotions.size() == 1) {
				// 프로모션 타입 확인
				if (validPromotions.get(0).getPromotion_type().equals("COUPON")) {
					if (originPrice >= validPromotions.get(0).getDiscount_value()) {
						discountPrice = validPromotions.get(0).getDiscount_value();
					} else {
						throw new Exception("상품 금액이 쿠폰 할인 금액 이상이어야 합니다.");
					}
				} else {
					discountPrice = originPrice * validPromotions.get(0).getDiscount_value() / 100;
				}
			} else {
				validPromotions = validPromotions.stream().sorted(Comparator.comparing(Promotion::getPromotion_type))
						.collect(Collectors.toList());

				int codeDiscountPrice = originPrice * validPromotions.get(0).getDiscount_value() / 100;
				
				// 최초 금액이 쿠폰할인 금액보다 크거나 같은 경우
				if (originPrice >= validPromotions.get(1).getDiscount_value()) {
					discountPrice += validPromotions.get(1).getDiscount_value();
					finalPrice = originPrice - discountPrice;
				}

				if (finalPrice > 0 || finalPrice > validPromotions.get(1).getDiscount_value()) {
					discountPrice += codeDiscountPrice;
				}

			}
			finalPrice = originPrice - discountPrice;
		} else {
			throw new Exception("프로모션 중 적용가능한 것이 존재하지 않습니다.");
		}

		// 최종 금액 천단위 절삭
		finalPrice = (originPrice - discountPrice) / 10000 * 10000;

		ProductAmountResponse productAmountRepsonse = ProductAmountResponse.builder().name(product.getName())
				.originPrice(originPrice).discountPrice(discountPrice).finalPrice(finalPrice).build();

		return productAmountRepsonse;
	}

}
