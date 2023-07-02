DROP TABLE product IF EXISTS;

CREATE TABLE product
(
    id    INTEGER NOT NULL,
    name  VARCHAR(255) NOT NULL ,
    price INTEGER NOT NULL ,
    created_at  DATE NOT NULL DEFAULT NOW(),
    updated_at DATE NULL DEFAULT NOW() ON UPDATE NOW(),
    PRIMARY KEY (id)
);

DROP TABLE promotion IF EXISTS;

CREATE TABLE promotion
(
    id             INTEGER NOT NULL,
    promotion_type VARCHAR(10) NOT NULL ,
    name           VARCHAR(255) NOT NULL ,
    discount_type  VARCHAR(15) NOT NULL ,
    discount_value INTEGER NOT NULL ,
    use_started_at DATE NOT NULL ,
    use_ended_at   DATE NOT NULL ,
    created_at DATE NOT NULL DEFAULT NOW(),
    updated_at DATE NULL DEFAULT NOW() ON UPDATE NOW(),
    PRIMARY KEY (id)
);


DROP TABLE promotion_products IF EXISTS;

CREATE TABLE promotion_products
(
    id           INTEGER NOT NULL,
    promotion_id INTEGER NOT NULL ,
    product_id   INTEGER NOT NULL ,
    created_at DATE NOT NULL DEFAULT NOW(),
    updated_at DATE NULL DEFAULT NOW() ON UPDATE NOW(),
    PRIMARY KEY (id)
);

