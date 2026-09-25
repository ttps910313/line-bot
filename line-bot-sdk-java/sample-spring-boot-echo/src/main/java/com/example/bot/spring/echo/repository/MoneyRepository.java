package com.example.bot.spring.echo.repository;

import com.example.bot.spring.echo.domain.Money;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyRepository extends JpaRepository<Money, Long> {

    Money findByName(String name);
}
