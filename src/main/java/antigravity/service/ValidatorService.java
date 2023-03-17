package antigravity.service;

import antigravity.error.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Date;

import static antigravity.error.dto.ErrorType.*;

@Service
public class ValidatorService {

    /**
    * 프로모션의 기간 검증
    */
    public void validatePeriod(Date startDate, Date endDate) {
        Date currentDay = new Date();

        if ( startDate.after(currentDay) )
            throw new BusinessException(INVALID_PROMOTION_PERIOD_BEFORE);
        if ( endDate.before(currentDay) )
            throw new BusinessException(INVALID_PROMOTION_PERIOD_EXPIRED);

    }

    /**
     * 상품금액보다 할인금액이 더 큰지에 대한 검증
     */
    public void validatePrice(int productPrice, int discountPrice){

        if ( productPrice < discountPrice )
            throw new BusinessException(INVALID_DISCOUNT_PRICE);
    }

    /**
     * 상품금액에 대한 검증
     */
    public void isValidPrice(int price) {
        if ( price >= 10000 && price <= 10000000){

        } else {
            throw new BusinessException(INVALID_PRODUCT_PRICE);
        }

    }
}
