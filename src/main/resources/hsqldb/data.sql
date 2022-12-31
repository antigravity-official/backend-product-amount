INSERT INTO promotion
VALUES (1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-11-01', '2023-03-01');
INSERT INTO promotion
VALUES (2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-11-01', '2023-03-01');
INSERT INTO promotion
VALUES (3, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2023-03-01', '2023-03-01');
INSERT INTO promotion
VALUES (4, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-11-01', '2022-11-01');

INSERT INTO product
VALUES (1, '피팅노드상품', 215000);
INSERT INTO product
VALUES (2, '피팅노드상품2', 9000);
INSERT INTO product
VALUES (3, '피팅노드상품3', 10001000);
INSERT INTO product
VALUES (4, '피팅노드상품4', 30000);

INSERT INTO promotion_products
VALUES (1, 1, 1);
INSERT INTO promotion_products
VALUES (2, 2, 1);
INSERT INTO promotion_products
VALUES (3, 3, 1);
INSERT INTO promotion_products
VALUES (4, 4, 1);
INSERT INTO promotion_products
VALUES (5, 1, 4);