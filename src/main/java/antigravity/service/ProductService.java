package antigravity.service;

import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        ProductAmountResponse productAmountResponse = repository.getProductAmountResponse(request.getProductId());

        int[] couponIds = request.getCouponIds();

        Date nowDate = new Date();
        int discountPrice = 0;

        //쿠폰 유효성 체크 시작
        for (int i = 0; i < couponIds.length; i++) {
            Promotion promotion = repository.getPromotion(couponIds[i]);
            //유효기간 체크
            if((nowDate.equals(promotion.getUse_started_at())|| nowDate.after(promotion.getUse_started_at())) && (nowDate.equals(promotion.getUse_ended_at()) || nowDate.before(promotion.getUse_ended_at()))) {

                //금액 할인
                if(promotion.getPromotion_type().equals("COUPON")){
                    discountPrice += promotion.getDiscount_value();
                }else{//퍼센트 할인
                    discountPrice += productAmountResponse.getOriginPrice() * promotion.getDiscount_value() / 100;
                }

            }
        }//쿠폰 유효성 체크 종료

        //할인금액 확정
        productAmountResponse.setDiscountPrice(discountPrice);

        //최소 상품가격: 10,000
        if(productAmountResponse.getOriginPrice() - productAmountResponse.getDiscountPrice() < 10000){
            productAmountResponse.setFinalPrice(10000);

            //최대 상품가격 10,000,000
        }else if(productAmountResponse.getOriginPrice() - productAmountResponse.getDiscountPrice() > 10000000){
            productAmountResponse.setFinalPrice(10000000);

        }else{ //최종 상품금액 천단위 절사
            int settlePrice = productAmountResponse.getOriginPrice() - productAmountResponse.getDiscountPrice();
            settlePrice = Math.round(settlePrice/10000)*10000;
            productAmountResponse.setFinalPrice(settlePrice);
        }

        return productAmountResponse;
    }//getProductAmount 종료

}
