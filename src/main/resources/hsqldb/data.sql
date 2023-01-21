INSERT INTO promotion
VALUES (1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-11-01', '2023-03-01');
INSERT INTO promotion
VALUES (2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-11-01', '2023-03-01');
INSERT INTO promotion
VALUES (4, 'CODE', '20% 할인코드', 'PERCENT', 20, '2022-11-01', '2022-12-01');
INSERT INTO promotion
VALUES (5, 'CODE', '15% 할인코드', 'PERCENT', 15, '2023-02-01', '2023-03-01');
INSERT INTO promotion
VALUES (6, 'COUPON', '210000원 할인쿠폰', 'WON', 210000, '2022-11-01', '2023-03-01');
INSERT INTO promotion
VALUES (7, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-11-01', '2023-03-01');
INSERT INTO promotion
VALUES (8, 'COUPON', '20% 할인쿠폰', 'PERCENT', 20, '2022-11-01', '2023-03-01');

INSERT INTO product
VALUES (1, '피팅노드상품', 215000);


INSERT INTO promotion_products
VALUES (1, 1, 1);
INSERT INTO promotion_products
VALUES (2, 2, 1);
INSERT INTO promotion_products
VALUES (3, 4, 1);
INSERT INTO promotion_products
VALUES (4, 5, 1);
INSERT INTO promotion_products
VALUES (5, 6, 1);
INSERT INTO promotion_products
VALUES (6, 8, 1);
