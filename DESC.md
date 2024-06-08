# 안티그래비티 백엔드 기술과제

## 요구사항

하나의 상품에 쿠폰과 할인코드를 적용하여 가격을 조회하는 api를 완성해주세요.

- 최종 Request, Response 는 제공된 객체를 활용합니다.
- 쿠폰이 적용되는지 검증 로직이 필요합니다.
- 최소 상품가격은 ₩ 10,000 입니다.
- 최대 상품가격은 ₩ 10,000,000 입니다.
- 최종 상품 금액은 천단위 절삭합니다.

## 구현 내용

| Class/Component              | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **ProductService**           | <ul><li> productId와 couponIds를 받아 productId로 상품 정보를 조회한 뒤, </li><li> 해당 상품에 적용할 수 있는 프로모션 정보를 조회하고 </li><li> 요청으로 들어온 couponIds에 해당하는 프로모션을 적용하여 상품의 가격 정보를 반환하는 기능을 제공합니다. </li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **ProductRepository**        | <ul><li> 기존에 productId를 통해 Product 정보를 조회하는 메서드인 getProduct() 가 존재하였는데 </li><li> 조회된 Product가 없는 경우 ***ProductNotFoundException*** 을 발생시키도록 수정하였습니다. </li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| **PromotionReader**          | <ul><li> product를 기반으로 해당 Product에 적용되어 있는 Promotion 목록을 찾아 하나의 객체로 만들어 반환하는 기능을 제공합니다. </li><li> 적용된 promotion이 없는 경우 product 정보와 빈 리스트를 포함한 객체를 반환합니다. </li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| **ProductWithPromotions**    | <ul><li> product 정보와 해당 product에 적용된 promotion 리스트 정보를 가지고 있습니다. </li><li> couponIds 목록을 받아 해당 promotion을 적용한 상품의 가격 정보를 반환하는 ***getAmountApplied()*** 메서드를 제공합니다. <ul><li> 내부적으로 ***canApply()*** 메서드를 통해 parameter로 전달 받은 couponIds 중 적용할 수 없는 id가 포함되어 있는 경우라면 ***NotSupportedPromotionException*** 을 발생시킵니다. </li><ul><li> 에러 대신 가능한 promotion만 적용한 뒤 상품의 가격 정보를 반환하여도 되지만 현재 응답 객체에 어떤 promotion이 적용이 되었는지 확인할 수 없기 때문에 혼란을 줄 것 같아 우선 에러 처리하였습니다. </li></ul></li><li> 내부적으로 ***getDiscountedPrices()*** 를 호출하여 couponIds에 해당하는 모든 프로모션을 적용한 뒤 할인 받을 수 있는 금액을 구하고 </li><li> 각 금액들을 모두 적용시켜 상품 금액 객체인 ```ProductAmount```를 반환합니다. </li></ul></li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| **DiscountPriceCalculator**  | <ul><li> Product와 Promotion을 입력 받아 해당 product에 promotion 을 적용하여 할인된 금액을 반환하는 ***apply()*** 메서드를 제공합니다. </li><li> parameter로 전달된 promotion의 promotionType이 미리 지정한 COUPON 이나 CODE 가 아닌 경우 ***PromotionTypeNotFoundException*** 을 발생시킵니다. </li><li> 또한, parameter로 전달된 promotion의 discountValue가 음수인 경우 ***InvalidDiscountPriceException*** 을 발생시킵니다. </li><li> promotionType에 따라 그에 맞는 계산 로직을 수행하여 할인된 금액을 반환합니다. </li></ul>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| **ProductAmount**            | <ul><li> id(프로덕트 아이디), name(프로덕트 이름), originalPrice(프로덕트 원래 가격), discountPrice(총 할인 금액), finalPrice(최종 금액) 을 멤버 변수로 가지고 있습니다. </li><li> 최초 생성 시 product를 매개변수로 받아 기본 정보를 세팅합니다. </li><li> ***discount()*** 를 통해 할인된 금액을 받아 discountPrice와 finalPrice를 업데이트 합니다. </li><ul><li> discountPrice의 최대값은 originalPrice로 지정하였습니다. (아무리 많이 할인 받아도 원래 가격보다 할인 받을 수 없기 때문) </li><li> finalPrice의 최소값은 0으로 지정하였습니다. (원래 가격만큼 최대 할인을 받았다면 상품의 가격은 0이기 때문) </li></ul><li> 최종 응답을 생성할 때 별도로 ***displayDiscountPrice, displayFinalPrice***를 사용하도록 하였습니다. </li><ul><li> displayFinalPrice는 다음과 같습니다. </li><ul><li> 계산된 finalPrice를 천원 단위로 절삭한 뒤, </li><li> 가격의 최소값인 1000원보다 작다면 1000을 반환하고 최대값인 10000000원 보다 크다면 10000000을 반환하고 </li><li> 두 경우 모두 아니라면 천원 단위 절삭된 값을 반환합니다. </li></ul><li> displayDiscountPrice는 다음과 같습니다. </li><ul><li> originalPrice에서 displayFinalPrice를 뺀 가격을 반환합니다. </li></ul><li> 최종 응답으로 전달되는 discountPrice, finalPrice를 별도로 둔 이유는 다음과 같습니다. </li><ul><li> 실제로 계산된 discountPrice와 finalPrice를 따로 사용하거나 저장하는 경우가 있을 것이라 생각했습니다. </li><li> 최종 응답의 finalPrice만 주어진 조건에 따라 천원 절삭 후 최대/최소 금액을 맞춰 반환하는 경우 응답을 받는 입장에서 originalPrice, discountPrice, finalPrice의 차이가 생기기 때문에 혼란을 줄 수 있을 것이라 생각했고 가공된 finalPrice에 맞춰 discountPrice도 가공하여 응답하는 것이 혼란을 줄일 수 있을 것이라 생각했습니다. </li></ul></li></ul> |

### 참고 - 구현에서 제외한 부분

  + API 문서
    - API 가 현재 하나이고 요청 값을 받는 부분도 존재하지 않기 때문에 작성에서 제외하였습니다.
    - 작성한다면 Swagger 또는 Restdocs 중 선택할 예정입니다.
  + 컨트롤러 테스트
    - 문서와 동일하게 API가 하나이고 요청 값을 받고 있지 않으므로 특별히 테스트할 내용이 없다고 생각하여 제외하였습니다.
  + 레포지토리 테스트
  + 공통된 정상/에러 응답
  + 도메인별 패키지 분리

## API 흐름

1. productId와 couponIds를 가진 Request 요청이 들어옵니다.
2. 요청 받은 productId를 통해 해당하는 Product를 조회합니다. 
   1. 만약 주어진 productId에 해당하는 Product가 존재하지 않는 경우 ***ProductNotFoundException*** 을 발생시킵니다.
3. 조회된 Product를 가지고 해당 Product에 적용할 수 있는 Promotion 목록을 조회한 뒤, 해당 Product와 Promotion 목록을 가진 객체를 생성합니다.
4. 위에서 생성한 객체를 통해 요청으로 주어진 couponIds에 해당하는 Promotion을 적용한 상품의 가격 정보를 구합니다.
   1. 주어진 couponIds 중 해당 상품에 적용할 수 없는 promotionId를 가지고 요청한 경우 ***NotSupportedPromotionException*** 을 발생시킵니다.
   2. couponIds 배열을 하나씩 확인하면서 해당하는 Promotion을 찾아 DiscountPriceCalculator를 통해 할인된 금액을 구합니다.
      - 찾은 Promotion의 type이 기존에 정의한 COUPON 또는 CODE 가 아닌 경우 ***PromotionTypeNotFoundException*** 을 발생시킵니다.
      - 찾은 Promotion의 discountValue가 음수인 경우에는 ***InvalidDiscountPriceException*** 을 발생시킵니다.
   3. 기본 Product 정보를 가지고 ProductAmount 객체를 생성한 뒤, 할인된 금액 리스트를 하나씩 확인하면서 생성한 ProductAmount 객체의 discount() 메서드를 호출해 할인 금액과 최종 금액을 업데이트합니다.
   4. 모든 할인 금액이 반영된 ProductAmount 객체를 반환합니다.
5. 가격 정보를 바탕으로 최종 가격 응답을 생성하여 반환합니다.
   1. 천원 절삭, 최대/최소 금액 맞추기와 같은 조건에 맞게 finalPrice, discountPrice를 가공하여 반환합니다.

## API Response
GET http://localhost:8089/products/amount

1. 정상 응답
```json
{
  "name": "피팅노드상품",
  "originPrice": 215000,
  "discountPrice": 63000,
  "finalPrice": 152000
}
```
2. productId에 해당하는 Product가 없는 경우
```json
{
  "code": "PRODUCT_NOT_FOUND",
  "message": "존재하지 않는 상품입니다. id = 2"
}
```
3. 해당 Product에서 지원하지 않는 promotion의 id를 요청한 경우
```json
{
  "code": "NOT_SUPPORTED_PROMOTION",
  "message": "3 프로모션은 적용할 수 없습니다."
}
```
4. promotion의 type이 기존에 정의한 COUPON 또는 CODE 가 아닌 경우
```json
{
  "code": "PROMOTION_TYPE_NOT_FOUND",
  "message": "지원하지 않는 프로모션 코드입니다. TYPE: COUPON1"
}
```
5. promotion의 discountValue가 음수인 경우
```json
{
  "code": "INVALID_DISCOUNT_PRICE",
  "message": "유효하지 않은 할인 금액입니다. price = -30000"
}
```
6. 알 수 없는 에러가 발생한 경우
```json
{
  "code": "INTERNAL_SERVER_ERROR",
  "message": "알 수 없는 서버 에러가 발생하였습니다."
}
```

