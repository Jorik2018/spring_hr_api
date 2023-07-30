package gob.regionancash.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.EscTipoPersonal;
import gob.regionancash.hr.service.EscTipoPersonalFacade;

public interface EscTipoPersonalRepository 
extends JpaRepository<EscTipoPersonal,Short>,EscTipoPersonalFacade{

}