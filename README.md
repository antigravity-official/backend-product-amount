- 사용자 요구사항
  - 하나의 상품에 다수의 쿠폰, 할인코드를 적용한 최종 가격을 조회하는 API
  - 가정) 클라이언트의 API 요청 `GET /products/amount?productId={productId}`
    + promotion_products 테이블에서 productID로 매핑된 적용해야할 promotion_ids 추출한다.
    + 해당 product에 매핑된 모든 프로모션을 적용하고, 다양한 예외처리를 적용한다.

- 비즈니스 로직 
  + ProductController -> getMapping
    - `ProductController` 에서 쿼리 파라미터로 productId를 인자로 전달한다.
    - return `ProductService.getProductAmount(productId)`
    
  + ProductService.getProductAmount
    - `findByProductId`메서드로 Product 객체화
      - 해당 Id로 상품을 찾을 수 없다면 `NOT_EXIST_PRODUCT` 예외를 던진다.
    - `findByPromotionIds(findAllCouponIdsByProductId(productId))`
      - 해당 상품 아이디에 매핑되어있는 모든 프로모션 아이디를 조회하고, 조회된 아이디 리스트를 바탕으로, 프로모션 리스트를 객체화한다.
      - 해당 Id로 프로모션을 찾을 수 없다면 `NOT_EXIST_PROMOTION` 예외를 던진다.
    - `isPromotionValid` 함수를 통해, 실행일(LocalDate)를 기준으로 쿠폰의 유효성(사용가능 여부)을 검증한다.
      - 해당 쿠폰이 유효하지 않는 쿠폰이라면 `INVALID_COUPON` 예외를 던진다.
    - return `discountService.calculateProductAmountResponse(product, promotions)`

  + DiscountService.calculateProductAmountResponse
    - `calculateFinalDiscountPrice` 함수로 최종 할인 가격 산정한다.
      - 최종 할인 가격은 `DiscountPolicyFactory`에서 쿠폰, 할인코드 할인으로 분류한다.
      - 분류된 할인은 각각 `FixDiscountPolicy`, `RateDiscountPolicy` 구현체에서 계산된다.
      - 해당 함수에서, ( 판매가 - 할인가 ) % 1000 으로 할인 가격에 절삭가를 가산한다.
    - `calculateFinalPrice` 함수로 최종 판매 가격을 산정한다.
      - 해당 함수에서, 판매 최종 가격(10,000 ~ 10,000,000) normalizePrice 함수를 호출한다.
      - 최종가격이 최소 가격보다 낮을 경우 `BELOW_LOWER_LIMIT` 예외를 던진다.
      - 최종가격이 최대 가격보다 높을 경우 `EXCEEDS_UPPER_LIMIT` 예외를 던진다.
    - `ProductAmountResponse` 객체에 응답을 고려하여 빌더 패턴으로 리턴한다.


- 이건 꼭 확인해주세요!
  - 최소 상품가격, 최대 상품가격에 대한 정의가, 단순히 도메인에 대한 제약 조건인지, 최종 가격에 대한 제약조건인지 불분명하여, 최종 가격이 상한 또는 하한을 벗어날 경우, 예외를 던지는 방식으로 처리했습니다.
  - 해당 과제의 핵심은 요청한 productId에 대한 쿠폰 정보, 상품 정보를 조회하고 알맞는 결과를 리턴해야한다고 판단했습니다.
    - 상품 테이블에 존재하지 않는 상품을 조회할 경우 예외를 던집니다.
    - 쿠폰 테이블에 존재하지 않는 쿠폰이 매핑되어 사용하려고 할 경우 예외를 던집니다.
    - 상품에 매핑된 쿠폰, 할인코드 중 단 한 개라도 유효하지 않다면(유효기간 이슈) 예외를 던집니다.
    - 지문에 제시된 `WON`, `PERCENT`를 제외한 다른 프로모션 타입을 가진 쿠폰이 매핑되어 사용하려고 할 경우 예외를 던집니다.
    - 최종 가격(할인가 및 쿠폰 미적용가)이 상한 초과, 하한 미만이라면 예외를 던집니다.
