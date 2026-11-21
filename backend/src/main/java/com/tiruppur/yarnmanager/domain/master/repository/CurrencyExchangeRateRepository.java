package com.tiruppur.yarnmanager.domain.master.repository;

import com.tiruppur.yarnmanager.domain.master.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long> {
    List<CurrencyExchangeRate> findByActiveTrueOrderByEffectiveDateDesc();

    List<CurrencyExchangeRate> findByCalendarMonthAndActiveTrue(String calendarMonth);

    @Query("SELECT r FROM CurrencyExchangeRate r WHERE r.fromCurrency = :fromCurrency " +
           "AND r.toCurrency = :toCurrency " +
           "AND r.active = true " +
           "AND r.effectiveDate <= :date " +
           "ORDER BY r.effectiveDate DESC")
    List<CurrencyExchangeRate> findEffectiveRates(
            @Param("fromCurrency") String fromCurrency,
            @Param("toCurrency") String toCurrency,
            @Param("date") LocalDate date);

    Optional<CurrencyExchangeRate> findFirstByFromCurrencyAndToCurrencyAndActiveTrueOrderByEffectiveDateDesc(
            String fromCurrency, String toCurrency);
}
