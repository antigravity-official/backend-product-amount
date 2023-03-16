package antigravity.service;

import antigravity.config.error.CustomException;
import org.springframework.stereotype.Service;

import java.util.Date;

import static antigravity.config.error.CustomErrorCode.*;

@Service
public class ValidateService {

    /**
     * 유효 기간 내의 쿠폰인지 확인
     *
     * 오늘 날짜가 쿠폰 사용 가능 시작 기간 보다 전 이거나
     *           쿠폰 사용 가능 종료 기간 보다 후 일 경우 예외 발생
     */
    public void validatePeriod(Date startedAt, Date endedAt) {
        Date today = new Date();

        if(startedAt.after(today))
            throw new CustomException(INVALID_PROMOTION_BEFORE_DATE);

        if(endedAt.before(today))
            throw new CustomException(INVALID_PROMOTION_EXPIRATION);
    }

    /**
     * 할인 금액이 상품 금액을 초과 하는 지 확인
     *
     * @param price
     * @param discountPrice
     */
    public void validatePrice(int price, int discountPrice){

        if(price < discountPrice)
            throw new CustomException(INVALID_PROMOTION_PRICE);
    }
}
