package gob.regionancash.hr.escalafon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.escalafon.model.Merit;
import gob.regionancash.hr.escalafon.service.MeritFacade;

public interface MeritRepository 
    extends JpaRepository<Merit,Integer>, MeritFacade{

}