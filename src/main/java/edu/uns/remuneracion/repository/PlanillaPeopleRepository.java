package edu.uns.remuneracion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.uns.remuneracion.model.PlanillaPeople;
import gob.regionancash.remuneracion.model.PayrollPeoplePK;
import jakarta.transaction.Transactional;

@Repository
public interface PlanillaPeopleRepository extends JpaRepository<PlanillaPeople, PayrollPeoplePK>,
        JpaSpecificationExecutor<PlanillaPeople> {


    @Modifying
    @Transactional
    @Query("DELETE FROM PlanillaPeople p WHERE p.id.payrollId = :payrollId")
    void deleteAllByPayrollId(@Param("payrollId") int payrollId);

}