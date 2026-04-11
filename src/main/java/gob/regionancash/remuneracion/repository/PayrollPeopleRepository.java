package gob.regionancash.remuneracion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gob.regionancash.remuneracion.model.PayrollPeople;
import gob.regionancash.remuneracion.model.PayrollPeoplePK;
import jakarta.transaction.Transactional;

@Repository
public interface PayrollPeopleRepository extends JpaRepository<PayrollPeople, PayrollPeoplePK>,
        JpaSpecificationExecutor<PayrollPeople> {


    @Modifying
    @Transactional
    @Query("DELETE FROM PayrollPeople p WHERE p.id.payrollId = :payrollId")
    void deleteAllByPayrollId(@Param("payrollId") int payrollId);

}