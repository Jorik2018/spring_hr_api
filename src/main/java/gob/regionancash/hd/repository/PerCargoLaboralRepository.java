package gob.regionancash.hd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hd.model.PerCargoLaboral;
import gob.regionancash.hd.service.PerCargoLaboralFacade;

public interface PerCargoLaboralRepository 
    extends JpaRepository<PerCargoLaboral,Integer>,PerCargoLaboralFacade{
    
}