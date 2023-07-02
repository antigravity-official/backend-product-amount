INSERT INTO promotion(id, promotion_type, name, discount_type, discount_value, use_started_at, use_ended_at, created_at, updated_at)
VALUES (1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-01-01', '2025-03-01', now(), now());
INSERT INTO promotion(id, promotion_type, name, discount_type, discount_value, use_started_at, use_ended_at, created_at, updated_at)
VALUES (2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-01-01', '2024-03-01', now(), now());

INSERT INTO product(id, name, price, created_at, updated_at)
VALUES (1, '피팅노드상품', 215000, now(), now());

INSERT INTO promotion_products(id, promotion_id, product_id, created_at, updated_at)
VALUES (1, 1, 1, now()), now();
INSERT INTO promotion_products(id, promotion_id, product_id, created_at, updated_at)
VALUES (2, 2, 1, now(), now());
