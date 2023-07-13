# **🔖 안티그래비티 기술과제 2차 제출**

- 작성자 : 변해빈 (joker7011@naver.com)
- 1차 제출일 : 2023/06/05
- 2차 제출일 : 2023/06/13

---

## **✏️ 목차**

- [📝 테스트 전략](#-테스트-전략)
  - [1. Service Layer - Mock Stubbing Test](#1-service-layer---mock-stubbing-test)
  - [2. Repository Layer - DataJpaTest](#2-repository-layer---datajpatest)
  - [3. Controller Layer - SpringBoot Integration Test / Mock](#3-controller-layer---springboot-integration-test--mock)

- [🚀 비즈니스 로직](#-비즈니스-로직)
  - [1. ProductPriceController](#1-productpricecontroller-에서-서비스-메소드-조립)
  - [2. PromotionService](#2-promotionservice---프로모션-유효성-검증조회-로직)
  - [3. ProductService](#3-productservice---상품-검증조회-로직)
  - [4. DiscountService](#4-discountservice---상품-할인-계산-로직)
  - [5. ProductAmountDiscountFactory](#5-productamountdiscountfactory---정률정액-할인-구현체-매칭-로직)
  - [6. (Fix / Rate) DiscoutnedAmountService](#6-fix--rate-discountedamountservice---정률정액-할인액-계산-로직)

- [👷 회고](#-회고)

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

- 단위 테스트의 `F.I.R.S.T` 전략을 준수하며 구현했습니다.
  - Fast: 유닛 테스트는 빨라야 한다.
  - Isolated: 다른 테스트에 종속적인 테스트는 절대로 작성하지 않는다.
  - Repeatable: 테스트는 실행할 때마다 같은 결과를 만들어야 한다.
  - Self-validating: 테스트는 스스로 결과물이 옳은지 그른지 판단할 수 있어야 한다.
  - Timely: 테스트는 적시에 즉, 테스트하려는 실제 코드를 구현하기 직전에 구현해야 한다.

  - [\[Reference\] Writing Your F.I.R.S.T Unit Tests
    ](https://dzone.com/articles/writing-your-first-unit-tests)

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
- `ProductService`, `promotionService`, `DiscountService` 에서 각각 필요한 메소드를 호출하여, 인자 값을 매칭하고,<br>최종으로 할인이 적용된 상품의 가격을 리턴한다.

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

### 6. `(Fix / Rate) DiscountedAmountService` - 정률/정액 할인액 계산 로직

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

## 👷 회고

    [ 📄 테스트 코드에 대한 고찰 ]

    테스트 코드를 빡세게 작성하다보니 너무 당연한 결과를 내놓으라 하는 테스트가 굳이 필요할까? 생각이 들기도 했습니다. 
    그 '당연한 결과' 마저도 stubbing으로 직접 구현하면서 테스트를 짜는게 조금은 회의적이였습니다.

    하지만, 비즈니스 로직을 리팩토링하면서, 테스트가 빛을 발하는 순간을 보게 되었습니다.
    사소한 비즈니스 로직이 수정되었음에도, 잘 분리되어있는 서비스의 단위 테스트는 예전처럼 검증을 잘 수행하지 못했습니다.

    '당연히 그렇게 될 것이다' 라고 기대한 결과가 그렇지 못했을 때, 테스트 코드가 없었다면 어느 부분에 이상이 있어 발생한 것인지 눈치채기 어려웠을 겁니다. 
    특히나 초기에 만들었던 코드는 정상 동작했지만 리팩토링이나 최적화 등의 이유로 코드를 수정했더니 더 이상 올바르게 동작하지 않을 때.
    바로 그런 상황에서, 테스트 코드의 중요성을 제대로 체감하고 더욱더 신나게 임했던 과제였던 것 같습니다.
<br>
    
    [ 👽️ 여전히 공부중인 숙제 ]

    1. 할인 API의 한 번 호출에서 검증과, 실제 가격을 내리는 데 까지 많은 쿼리문이 날아가지 않나? DB 구조 변경 없이 더 효율적인 쿼리문 작성이 가능할까?
    2. Service / Controller 의 책임 분리는 제대로 이루어 졌을까? 컨트롤러 Layer에서 서비스를 호출하며 조립하는 로직이 어쩌면, 하나의 비즈니스 로직이 되지 않을까?
    3. Controller 테스트의 통합 테스트 적용은 적절했을까? 이 역시 Stubbing을 통해 DB와 고립된 환경에서 진행해야 했을까? 그렇다면, 통합테스트는 어떤 계층에서 진행해야할까?
<br>

    [ 🍻 값진 수업료 ]

    1. 통합, 단위테스트, 테스트더블의 필요성과 중요성에 대해 직접 체험할 수 있는 과제였습니다.
      - 개인적으로 디트로이트와 런던의 어느 중간에 있는 입장인 것 같아요. (적절한 통합 테스트로 신뢰성 보장 + 적절한 Mock과 Stubbing을 통한 효율 보장)
    2. 리턴타입을 행위 계산값으로 제한 / 검증 함수를 boolean으로 제한하기만 했는데도 클래스/함수의 책임과 관심사 분리가 쉬웠습니다.
    3. 이커머스 도메인에 대한 느낌을 맛 볼수 있는 과제였습니다. 정말 재밌는 도메인인 것 같아요 :)
    4. Mock/Stubbing/단위 및 통합테스트에 대해 최고의 동기부여로 공부할 수 있는 기회였습니다!
    5. 메소드에 대한 명명 is/has문, 테스트코드 메소드에 대한 명명에 대한 다양한 레퍼런스를 참조하며 공부할 수 있었습니다.
    6. 한 달 가까이 공부하고 연구했던 부분을, 이 과제에 녹여낼 수 있었던 것 같아, 성장곡선의 기울기를 가파르게 올릴 수 있었습니다.




