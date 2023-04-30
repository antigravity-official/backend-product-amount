DROP TABLE product IF EXISTS;

CREATE TABLE product
(
    id    INTEGER NOT NULL,
    name  VARCHAR(255),
    price INTEGER,
    PRIMARY KEY (id)
);

DROP TABLE promotion IF EXISTS;

CREATE TABLE promotion
(
    id             INTEGER NOT NULL,
    promotion_type enum('COUPON', 'CODE') NOT NULL,
    name           VARCHAR(255) NOT NULL,
    discount_type  enum('WON', 'PERCENT') NOT NULL,
    discount_value INTEGER NOT NULL,
    use_started_at DATE NOT NULL,
    use_ended_at   DATE NOT NULL,
    PRIMARY KEY (id)
);


DROP TABLE promotion_products IF EXISTS;

CREATE TABLE promotion_products
(
    id           INTEGER NOT NULL,
    promotion_id INTEGER,
    product_id   INTEGER,
    PRIMARY KEY (id)
);

