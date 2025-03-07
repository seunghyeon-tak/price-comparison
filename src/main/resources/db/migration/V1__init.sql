CREATE TABLE IF NOT EXISTS `user`
(
    `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(100) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `nickname`   VARCHAR(45)  NULL,
    `alert_type` VARCHAR(45)  NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_card_information`
(
    `id`            INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    `name`          VARCHAR(45)    NOT NULL,
    `user_id`       INT UNSIGNED   NOT NULL,
    `discount_rate` DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (`id`),
    INDEX `fk_user_card_information_user_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_user_card_information_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `store`
(
    `id`   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45)  NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `category`
(
    `id`   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45)  NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `product`
(
    `id`           INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(45)  NOT NULL,
    `store_id`     INT UNSIGNED NOT NULL,
    `category_id`  INT UNSIGNED NOT NULL,
    `purchase_url` VARCHAR(255) NULL,
    `description`  TEXT         NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_product_store1_idx` (`store_id` ASC) VISIBLE,
    INDEX `fk_product_category1_idx` (`category_id` ASC) VISIBLE,
    CONSTRAINT `fk_product_store1`
        FOREIGN KEY (`store_id`)
            REFERENCES `store` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_product_category1`
        FOREIGN KEY (`category_id`)
            REFERENCES `category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `price_alerts`
(
    `id`           INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    `target_price` DECIMAL(10, 2) NOT NULL,
    `user_id`      INT UNSIGNED   NOT NULL,
    `product_id`   INT UNSIGNED   NOT NULL,
    `created_at`   TIMESTAMP      NULL     DEFAULT CURRENT_TIMESTAMP,
    `is_active`    TINYINT(1)     NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`),
    INDEX `fk_user_likes_user1_idx` (`user_id` ASC) VISIBLE,
    INDEX `fk_user_likes_product1_idx` (`product_id` ASC) VISIBLE,
    CONSTRAINT `fk_user_likes_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_user_likes_product1`
        FOREIGN KEY (`product_id`)
            REFERENCES `product` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `product_images`
(
    `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `url`        VARCHAR(255) NOT NULL,
    `product_id` INT UNSIGNED NOT NULL,
    `is_main`    TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `fk_product_images_product1_idx` (`product_id` ASC) VISIBLE,
    CONSTRAINT `fk_product_images_product1`
        FOREIGN KEY (`product_id`)
            REFERENCES `product` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `product_price`
(
    `id`         INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    `price`      DECIMAL(10, 2) NOT NULL,
    `product_id` INT UNSIGNED   NOT NULL,
    `crawled_at` TIMESTAMP      NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `fk_product_price_product1_idx` (`product_id` ASC) VISIBLE,
    CONSTRAINT `fk_product_price_product1`
        FOREIGN KEY (`product_id`)
            REFERENCES `product` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user_favorites`
(
    `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `created_at` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`    INT UNSIGNED NOT NULL,
    `product_id` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_user_favorites_user1_idx` (`user_id` ASC) VISIBLE,
    INDEX `fk_user_favorites_product1_idx` (`product_id` ASC) VISIBLE,
    CONSTRAINT `fk_user_favorites_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_user_favorites_product1`
        FOREIGN KEY (`product_id`)
            REFERENCES `product` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;
