package gob.regionancash.hd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hd.model.PerDedicacion;
import gob.regionancash.hd.service.PerDedicacionFacade;

public interface PerDedicacionRepository
    extends JpaRepository<PerDedicacion,Integer>,PerDedicacionFacade{

}