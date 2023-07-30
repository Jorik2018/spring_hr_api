package gob.regionancash.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.PensionSystem;
import gob.regionancash.hr.service.PensionSystemFacade;

public interface PensionSystemRepository 
    extends JpaRepository<PensionSystem,Short>,PensionSystemFacade{

}