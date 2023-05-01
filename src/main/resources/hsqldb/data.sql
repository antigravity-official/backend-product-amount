INSERT INTO promotion(id, created_at, updated_at, promotion_type, name, discount_type, discount_value, use_started_at,
                      use_ended_at)
VALUES (1, NOW(), NOW(), 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2023-04-30 00:00:00.000', '2023-05-30 00:00:00.000'),
       (2, NOW(), NOW(), 'CODE', '15% 할인코드', 'PERCENT', 15, '2023-04-30 00:00:00.000', '2023-06-30 00:00:00.000');

INSERT INTO product(id, created_at, updated_at, name, price)
VALUES (1, NOW(), NOW(), '피팅노드상품', 215000),
       (2, NOW(), NOW(), '피팅노드상품2', 43000);

INSERT INTO promotion_products(id, promotion_id, product_id, created_at)
VALUES (1, 1, 1, NOW()),
       (2, 2, 1, NOW()),
       (3, 1, 2, NOW()),
       (4, 2, 2, NOW());
