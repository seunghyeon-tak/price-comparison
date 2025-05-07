CREATE TABLE IF NOT EXISTS `user_preferred_store`
(
    `id`       BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `user_id`  BIGINT UNSIGNED NOT NULL,
    `store_id` BIGINT UNSIGNED NOT NULL,
    CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`)
) ENGINE = InnoDB;
