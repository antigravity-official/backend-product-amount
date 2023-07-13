# **🔖 안티그래비티 기술과제 2차 제출**

- 작성자 : 변해빈 (joker7011@naver.com)
- 1차 제출일 : 2023/06/05
- 2차 제출일 : 2023/06/13

---

## **📝 테스트 전략**

### **1. Service Layer - `Mock Stubbing Test`**

- Mock으로 Repository 계층의 조회/저장 기능을 Stubbing하여, DB 없이 빠르게 동작
- 모든 서비스 클래스는 `ServiceTestSupport`추상 클래스를 상속받아, 코드 중복 제거
- 달성 Coverage
    - class : 100% (7/7)
    - method : 100% (29/29)
    - Line : 100% (71/71)

```java

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTestSupport {

    @Mock
    protected ___QueryRepository ___QueryRepo;
```

---

### **2. Repository Layer - `DataJpaTest`**

- DataJpaTest로 `findByID` 메소드와 직접 구현한 쿼리문의 정상 작동 여부를 검증
- 모든 레포지토리 클래스는 `RepositoryTestSupport`추상 클래스를 상속받아, 코드 중복 제거
- JpaRepository 코드 구현 간 조회(`____QueryRepository`), 저장(`___Repository`) 기능 분리

```java

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public abstract class RepositoryTestSupport {
}
```

---

### **3. Controller Layer - `SpringBoot Integration Test / Mock`**

- Controller 계층에서 ErrorCode 기반, 발생할 수 있는 ExceptionCase에 대한 통합테스트 실시
- 모든 컨트롤러 클래스는 `ControllerTestSupport` 추상 클래스를 상속받아, 코드 중복 제거
- MockMvc를 활용해 MockRequest를 활용해, 실제 요청에 대한 응답 로직 정상 수행 여부 검증
- 편의 메소드를 추상 클래스 내부에 구현해, 컨트롤러 테스트 내부에서 코드 중복 최소화
- 달성 Coverage
  - class 100% (1/1)
  - method 100% (2/2)
  - line 100% (7/7)


```java
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ___Repository ___Repository;

    @Autowired
    protected ___QueryRepository ___QueryRepository;
```

---

## **🚀 비즈니스 로직**

### 1. `ProductPriceController` 에서 서비스 메소드 조립
- `ProductService`, `promotionService`, `DiscountService` 에서 각각 필요한 메소드를 호출하여, 인자 값을 매칭하고, 최종으로 할인이 적용된 상품의 가격을 리턴한다.

---

### 2. `PromotionService` - 프로모션 유효성 검증/조회 로직
- 해당 서비스에서 유효한 프로모션에 대한 검증을 실시합니다.
  - Validation Logic
    - 프로모션이 전혀 매핑되지 않은 상품에 어떠한 프로모션이라도 사용을 요청한다면 예외를 던진다.
    - 상품Id에 매칭되지 않은 프로모션 사용을 요청한다면 예외를 던진다. 
    - 프로모션의 기간이 도래하지 않았거나, 만료된 프로모션이 요청된다면 예외를 던진다.
    - 프로모션이 매핑되었더라도, 해당 프로모션이 존재하지 않는 프로모션이라면 예외를 던진다.
    - 할인 API에서 `promotionIds` 가 `emptyList`로 요청된다면 예외를 던진다.

    <br>

  - Public Functions
    - `findMappedPromotionIdsByProductId` <br>메소드로 해당 상품 아이디에 매핑되어, 적용할 수 있는 프로모션 아이디 리스트를 리턴합니다.
    - `findAllPromotionsByIds` <br>프로모션 아이디 리스트에 해당하는 모든 프로모션 리스트를 리턴합니다.
    - `findApplicablePromotions` <br>promotionIds를 기준으로, 검증이 완료된 프로모션 리스트를 리턴합니다.

---

### 3. `ProductService` - 상품 검증/조회 로직
- 해당 서비스에서 유효한 상품에 대한 검증을 실시합니다.
  - Validation Logic
    - 존재하지 않는 상품이 요청된다면 예외를 던진다.
  
  <br>

 - Public Functions
   - `findProductById` <br> 상품 아이디로, 해당 상품 엔티티를 리턴합니다.

---

### 4. `DiscountService` - 상품 할인 계산 로직
- 해당 서비스에서 상품에 대한 할인 가격 계산을 실시합니다.
  - Validation Logic
    - 최종 할인 가격이 `UPPER_BOUND(10,000,000) KRW` 초과라면 예외를 던진다. 
    - 최종 할인 가격이 `LOWER_BOUND(10,000) KRW` 미만이면 예외를 던진다.
  
  <br>

  - Public Functions
    - `applyDiscount` <br> 상품과, 프로모션 리스트를 바탕으로, 실제 할인을 적용한 `Response`를 리턴<br>

---

### 5. `ProductAmountDiscountFactory` - 정률/정액 할인 구현체 매칭 로직
- 해당 서비스에서 `DiscountType` 을 기준으로 분류해, <br>`DiscountedAmountUtil` 클래스 리턴값에  `DiscountType`에 따라 종류별로 구현체를 리턴합니다.
  - Validation Logic
    - `DiscountType`에 `WON`, `PERCENT`을 제외한 다른 타입이 요청된다면, 예외를 던진다.
  
  <br>

  - Public Functions
    - `calculateDiscountedAmount`<br> switch-case 문법을 통해 `DiscountType`에 맞게, 할인액 계산 구현체를 리턴합니다.
        - `case WON:` - return `FixDiscountedAmountService`
        - `case PERCENT:` - return `RateDiscoutnedAmountService`
    

  ---

### 6. `FixDiscountedAmountService / RateDiscountedAmountService` - 정률/정액 할인액 계산 로직

- discountService에서 호출되어, `할인해야 할 금액` 이 얼마인지, 정륧할인 및 정액할인 특성에 맞게 계산합니다.
  - Validation Logic
    - `FixDiscountedAmountService`
      - 정액할인 `discountedAmount` 인자가 `음수`, `ZERO` 라면 예외를 던진다.
    - `RateDiscountedAmountService`
      - 정률할인 `discountedAmount` 인자가 `음수`, `ZERO`, `100 초과의 양수` 라면 예외를 던진다.

  <br>

  - Public Functions
    ```java
    @Override // Override By DiscountedAmountUtil
    public int getDiscountedValue(int originPrice, Promotion promotion)
    ```
    - 정액 할인 - return `promotion.getDiscountValue()`
    - 정률 할인 - return `originPrice / 100 * promotion.getDiscountValue()`

---

