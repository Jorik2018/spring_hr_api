package edu.uns.remuneracion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.uns.remuneracion.model.PayrollPeriod;
import edu.uns.remuneracion.model.PerPlanilla;

@Repository
public interface PlanillaRepository extends JpaRepository<PerPlanilla, Integer> {

    List<PerPlanilla> findByPeriod(PayrollPeriod periodo);

}