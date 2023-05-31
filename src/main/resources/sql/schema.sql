DROP TABLE IF EXISTS product;

CREATE TABLE product
(
    id    INTEGER NOT NULL,
    name  VARCHAR(255),
    price INTEGER,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS promotion;

CREATE TABLE promotion
(
    id             INTEGER NOT NULL,
    promotion_type VARCHAR(10),
    name           VARCHAR(255),
    discount_type  VARCHAR(15),
    discount_value INTEGER,
    use_started_at DATE,
    use_ended_at   DATE,
    PRIMARY KEY (id)
);


DROP TABLE IF EXISTS promotion_products;

CREATE TABLE promotion_products
(
    id           INTEGER NOT NULL,
    promotion_id INTEGER,
    product_id   INTEGER,
    PRIMARY KEY (id)
);

