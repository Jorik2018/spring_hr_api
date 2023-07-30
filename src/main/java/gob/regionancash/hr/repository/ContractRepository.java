package gob.regionancash.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.Contract;
import gob.regionancash.hr.service.ContractFacade;

public interface ContractRepository 
    extends JpaRepository<Contract,Integer>, ContractFacade{

}
