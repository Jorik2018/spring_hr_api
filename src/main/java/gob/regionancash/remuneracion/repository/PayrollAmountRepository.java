package gob.regionancash.remuneracion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gob.regionancash.remuneracion.model.PayrollAmount;

@Repository
public interface PayrollAmountRepository extends JpaRepository<PayrollAmount, Integer> {

}