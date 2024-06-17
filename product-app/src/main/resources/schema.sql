CREATE TABLE IF NOT EXISTS Brand (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Category (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Product (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       price INT NOT NULL,
                                       brand_id BIGINT,
                                       category_id BIGINT,
                                       FOREIGN KEY (brand_id) REFERENCES Brand(id),
                                       FOREIGN KEY (category_id) REFERENCES Category(id)
);

CREATE TABLE category_lowest_price_product (
                                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               product_id BIGINT NOT NULL,
                                               FOREIGN KEY (product_id) REFERENCES Product(id)
);

CREATE TABLE brand_with_category_lowest_price (
                                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               brand_id BIGINT NOT NULL,
                                               total_category_min_price INT NOT NULL,
                                               FOREIGN KEY (brand_id) REFERENCES Brand(id)
);
