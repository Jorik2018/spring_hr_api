package edu.uns.remuneracion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import edu.uns.remuneracion.model.PlanillaConcept;
import gob.regionancash.remuneracion.model.PayrollConceptPK;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PlanillaConceptRepository extends JpaRepository<PlanillaConcept, PayrollConceptPK> {

    @Modifying
    @Transactional
    @Query("DELETE FROM PlanillaConcept c WHERE c.id.payrollId = :payrollId")
    void deleteAllByPayrollId(@Param("payrollId") int payrollId);

}