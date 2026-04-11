package edu.uns.remuneracion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.uns.remuneracion.model.PayrollPeriod;

@Repository
public interface PerPeriodoPlaRepository extends JpaRepository<PayrollPeriod, Integer> {

    Optional<PayrollPeriod> findByYearAndMonth(Short anio, Short mes);
    
}