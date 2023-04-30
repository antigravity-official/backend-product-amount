INSERT INTO promotion
VALUES (1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2023-04-30 00:00:00.000', '2023-05-30 00:00:00.000');
INSERT INTO promotion
VALUES (2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2023-04-30 00:00:00.000', '2023-06-30 00:00:00.000');

INSERT INTO product
VALUES (1, '피팅노드상품', 215000);

INSERT INTO promotion_products
VALUES (1, 1, 1);
INSERT INTO promotion_products
VALUES (2, 2, 1);