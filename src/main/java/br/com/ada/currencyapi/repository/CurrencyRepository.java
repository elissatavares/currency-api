package br.com.ada.currencyapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ada.currencyapi.domain.Currency;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByName(String name);
}
