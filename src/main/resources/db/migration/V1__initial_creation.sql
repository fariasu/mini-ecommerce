CREATE TABLE tb_cart_items
(
    id         UUID NOT NULL,
    cart_id    UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity   INTEGER,
    unit_price DOUBLE PRECISION,
    subtotal   DOUBLE PRECISION,
    CONSTRAINT pk_tb_cart_items PRIMARY KEY (id)
);

CREATE TABLE tb_carts
(
    id          UUID NOT NULL,
    user_id     UUID,
    total_price DOUBLE PRECISION,
    status      SMALLINT,
    CONSTRAINT pk_tb_carts PRIMARY KEY (id)
);

CREATE TABLE tb_products
(
    id          UUID NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    price       DOUBLE PRECISION,
    stock       INTEGER,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tb_products PRIMARY KEY (id)
);

CREATE TABLE tb_users
(
    id         UUID         NOT NULL,
    name       VARCHAR(255),
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255),
    user_role  VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_tb_users PRIMARY KEY (id)
);

ALTER TABLE tb_users
    ADD CONSTRAINT uc_tb_users_email UNIQUE (email);

ALTER TABLE tb_cart_items
    ADD CONSTRAINT FK_TB_CART_ITEMS_ON_CART FOREIGN KEY (cart_id) REFERENCES tb_carts (id);

ALTER TABLE tb_cart_items
    ADD CONSTRAINT FK_TB_CART_ITEMS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES tb_products (id);