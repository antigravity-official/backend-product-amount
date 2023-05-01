DROP TABLE IF EXISTS product;

CREATE TABLE product
(
    id         INT                                              NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255)                                     NOT NULL,
    price      INT CHECK (price >= 10000 AND price <= 10000000) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP               NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP               NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS promotion;

CREATE TABLE promotion
(
    id             INT                                NOT NULL AUTO_INCREMENT,
    promotion_type ENUM ('COUPON', 'CODE')            NOT NULL,
    name           VARCHAR(255)                       NOT NULL,
    discount_type  ENUM ('WON', 'PERCENT')            NOT NULL,
    discount_value DECIMAL(8, 2)                      NOT NULL,
    use_started_at DATETIME                           NOT NULL,
    use_ended_at   DATETIME                           NULL,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);


DROP TABLE IF EXISTS promotion_products;

CREATE TABLE promotion_products
(
    id           INT                                NOT NULL AUTO_INCREMENT,
    promotion_id INT                                NOT NULL,
    product_id   INT                                NOT NULL,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_promotion_products_promotion FOREIGN KEY (promotion_id) REFERENCES promotion (id) ON DELETE CASCADE,
    CONSTRAINT fk_promotion_products_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    PRIMARY KEY (id),
    UNIQUE KEY uk_promotion_products_promotion_product (promotion_id, product_id)
);

