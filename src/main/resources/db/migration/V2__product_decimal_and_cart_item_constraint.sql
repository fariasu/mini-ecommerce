ALTER TABLE tb_carts
    ADD CONSTRAINT uc_0881685eb1a75ddf9adef296a UNIQUE (user_id, status);

ALTER TABLE tb_carts
DROP
COLUMN total_price;

ALTER TABLE tb_carts
    ADD total_price DECIMAL;