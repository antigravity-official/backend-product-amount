DROP TABLE product IF EXISTS;

CREATE TABLE product
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255),
    price INTEGER
);

DROP TABLE promotion IF EXISTS;

CREATE TABLE promotion
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    promotion_type VARCHAR(10),
    name           VARCHAR(255),
    discount_type  VARCHAR(15),
    discount_value INTEGER,
    use_started_at DATE,
    use_ended_at   DATE
);


DROP TABLE promotion_products IF EXISTS;

CREATE TABLE promotion_products
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    promotion_id INTEGER,
    product_id   INTEGER
);

