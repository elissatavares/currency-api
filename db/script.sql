CREATE TABLE IF NOT EXISTS currency (
                          id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS exchanges (
                           currency_id BIGINT,
                           FOREIGN KEY (currency_id) REFERENCES currency(id) ON DELETE CASCADE,
                           currency_name VARCHAR(255) PRIMARY KEY,
                           exchange_rate DECIMAL(19, 4)
);
