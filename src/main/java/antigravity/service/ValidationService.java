package antigravity.service;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Service;

import antigravity.domain.entity.Product;

@Service
public class ValidationService {

	public void minmaxValidation(Product product) throws Exception {
		try {
			checkminMaxPrice(product.getPrice());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean couponValidation(Date useStartAt, Date useEndAt) {
		
		LocalDate today = LocalDate.now();
		LocalDate startAt = new java.sql.Date(useStartAt.getTime()).toLocalDate();
		LocalDate endAt = new java.sql.Date(useEndAt.getTime()).toLocalDate();
		
		if (!(today.isBefore(startAt)) && !(endAt.isBefore(today))) {
			return true;
		} else {
			return false;
		}
		
	}

	// 최소, 최대 금액 체크
	public void checkminMaxPrice(int price) throws Exception {
		if (price < 10000) {
			throw new Exception("최소 상품가격은 ₩ 10,000 입니다.");
		} else if (price > 10000000) {
			throw new Exception("최대 상품가격은 ₩ 10,000,000 입니다.");
		}
	}
	
}
