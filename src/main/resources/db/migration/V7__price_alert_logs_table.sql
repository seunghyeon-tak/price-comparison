CREATE TABLE price_alert_logs (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    alert_id BIGINT UNSIGNED NOT NULL,
    notified_price INT NOT NULL,
    notified_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (alert_id) REFERENCES price_alerts(id)
);