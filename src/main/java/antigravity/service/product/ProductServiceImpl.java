package antigravity.service.product;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;
import antigravity.enums.product.CutStandard;
import antigravity.enums.promotion.PromotionType;
import antigravity.exception.product.ProductNotFoundException;
import antigravity.exception.promotion.PromotionInvalidException;
import antigravity.model.request.product.service.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.util.CalculationUtil;
import antigravity.util.product.ProductPriceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private static final BigDecimal BIG_DECIMAL_HUNDRED = new BigDecimal(100);
	private final ProductRepository productRepo;
	private final PromotionRepository promotionRepo;
	private final ProductPriceUtil productPriceUtil;

	/**
	 * 상품 가격 조회
	 * @param request 조회할 상품 정보
	 * @return ProductAmountResponse 상품 가격 정보
	 */
	@Transactional(readOnly = true)
	public ProductAmountResponse getProductAmount(final ProductInfoRequest request) {
		final Product product = getProductById(request.getProductId());
		log.debug("상품 가격 조회 대상 상품: {}", product);
		final Integer[] couponIds = request.getCouponIds();
		final Timestamp now = new Timestamp(System.currentTimeMillis()); //현재
		//해당 쿠폰 ID의 프로모션이면서 해당 상품에 적용 가능한 프로모션 정보만 조회
		final List<Promotion> promotionList
			= promotionRepo.findAllByProductAndCouponIds(product, couponIds, now);
		log.debug("상품 가격 조회 적용 대상 상품 프로모션: {}", promotionList);
		//적용할 수 없는 쿠폰이 1개 이상 있는 경우
		if (couponIds != null && promotionList.size() != couponIds.length) {
			log.error("유효하지 않은 프로모션 존재 - request: {}", request);
			throw new PromotionInvalidException();
		}
		//case 1: 적용할 프로모션 미존재
		if (promotionList.isEmpty()) {
			return ProductAmountResponse.toDto(product);
		}
		//case 2: 적용할 프로모션 존재
		final Map<PromotionType, List<Promotion>> promotionMap
			= promotionList.stream().collect(groupingBy(Promotion::getPromotionType)); //프로모션 유형 별로 적용 프로모션 분류
		final int percentDiscountAmount = (promotionMap.containsKey(PromotionType.CODE)) ?
			getPercentDiscountAmount(new BigDecimal(product.getPrice()),
				promotionMap.get(PromotionType.CODE)) : 0; //비율 할인
		final int wontDiscountAmount = (promotionMap.containsKey(PromotionType.COUPON)) ?
			getWonDiscountAmount(promotionMap.get(PromotionType.COUPON)) : 0; //금액 할인
		final int totalDiscountAmount = productPriceUtil.adjustDiscountAmount(product,
			percentDiscountAmount + wontDiscountAmount); //조정된 총 할인 금액
		return ProductAmountResponse.toDto(product, totalDiscountAmount,
			CalculationUtil.cutAmount(product.getPrice() - totalDiscountAmount,
				CutStandard.THOUSANDS_CUT_STANDARD));
	}

	/**
	 * 상품 ID로 상품 정보 조회
	 * @param productId 상품 ID
	 * @return Product 상품 정보
	 */
	@Transactional(readOnly = true)
	public Product getProductById(final Integer productId) {
		log.debug("상품 조회 요청 - productId: {}", productId);
		final Product product = productRepo.findById(productId).orElseThrow(ProductNotFoundException::new);
		log.debug("상품 조회 완료 - product: {}", product);
		return product;
	}

	/**
	 * 총 금액 할인 금액 구하기
	 * @param couponPromotionList 금액 할인 대상 프로모션 리스트
	 * @return int 총 금액 할인 금액
	 */
	private static int getWonDiscountAmount(final List<Promotion> couponPromotionList) {
		//총 할인 금액 합산
		final Optional<Integer> sum = couponPromotionList.stream()
			.map(p -> p.getDiscountValue().intValue()).reduce(Integer::sum);
		return sum.orElse(0);
	}

	/**
	 * 총 비율 할인 금액 구하기
	 * @param productPrice 상품 가격
	 * @param codePromotionList 비율 할인 대상 프로모션 리스트
	 * @return int 총 비율 할인 금액
	 */
	private static int getPercentDiscountAmount(final BigDecimal productPrice,
		final List<Promotion> codePromotionList) {
		//할인 금액 계산 후, 합산하여 총 비율 할인금 구하기(할인 금액 구할 때 소숫점 이하 자리는 버림 처리)
		final Optional<Integer> sum = codePromotionList.stream()
			.map(p -> p.getDiscountValue()
				.divide(BIG_DECIMAL_HUNDRED, RoundingMode.FLOOR)
				.multiply(productPrice)
				.intValue())
			.reduce(Integer::sum);
		return sum.orElse(0); //비율 할인 금액 없으면 0
	}

}
