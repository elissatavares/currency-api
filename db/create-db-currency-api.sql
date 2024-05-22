\c currency;

CREATE TABLE currency (
                          id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(255)
);

-- Criação da tabela exchanges
CREATE TABLE exchanges (
                           currency_id BIGINT,
                           FOREIGN KEY (currency_id) REFERENCES currency(id) ON DELETE CASCADE,
                           currency_name VARCHAR(255) PRIMARY KEY,
                           exchange_rate DECIMAL(19, 4)
);
