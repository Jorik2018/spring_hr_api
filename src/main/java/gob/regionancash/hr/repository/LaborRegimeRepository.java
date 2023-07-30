package gob.regionancash.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.LaborRegime;
import gob.regionancash.hr.service.LaborRegimeFacade;

public interface LaborRegimeRepository 
    extends JpaRepository<LaborRegime,Integer>,LaborRegimeFacade{

}